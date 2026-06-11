package com.phoenixforge.student.sync

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class EventWriterLogicTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `event id format matches master spec`() {
        val id = EventIdFactory.formatEventId("student-tablet", 1_749_470_400_000L, 3)
        assertEquals("EVT_student-tablet_1749470400000_0003", id)
    }

    @Test
    fun `sequence resets when epoch changes`() {
        val state = EventSequenceState()
        val first = state.nextEventId("dev", 1000L)
        val second = state.nextEventId("dev", 1000L)
        val third = state.nextEventId("dev", 2000L)

        assertTrue(first.endsWith("_0001"))
        assertTrue(second.endsWith("_0002"))
        assertTrue(third.endsWith("_0001"))
    }

    @Test
    fun `logical clock advances past seen events in directory`() {
        val dir = createTempDir()
        val eventsDir = File(dir, "events").apply { mkdirs() }
        File(eventsDir, "EVT_dev_1_0001.json").writeText(
            """
            {
              "schemaVersion": 1,
              "eventId": "EVT_dev_1_0001",
              "eventType": "QUEST_STARTED",
              "scope": "PUBLIC",
              "actorApp": "student_edition",
              "actorDeviceId": "dev",
              "studentUid": "uid-1",
              "logicalClock": 7,
              "epochMs": 1,
              "payload": {"questId": "q1"}
            }
            """.trimIndent(),
        )

        val seen = LogicalClockLogic.scanMaxLogicalClock(eventsDir, json)
        assertEquals(7L, seen)
        assertEquals(8L, maxOf(5L, seen) + 1)
    }

    @Test
    fun `student scope guard rejects protected`() {
        assertTrue(EventScope.studentMayWrite(EventScope.PUBLIC))
        assertTrue(EventScope.studentMayWrite(EventScope.LESSON))
        assertFalse(EventScope.studentMayWrite(EventScope.PROTECTED))
    }

    @Test
    fun `encoded quest started payload omits syncVersion`() {
        val encoded = json.encodeToString(QuestStartedPayload(questId = "stub_tile_abc"))
        assertTrue(encoded.contains("stub_tile_abc"))
        assertFalse(encoded.contains("syncVersion"))
    }
}
