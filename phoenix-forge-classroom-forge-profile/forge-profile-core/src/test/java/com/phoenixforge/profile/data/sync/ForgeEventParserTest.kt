package com.phoenixforge.profile.data.sync

import com.phoenixforge.profile.data.serialization.ForgeProfileJson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ForgeEventParserTest {

    private val forgeProfileJson = ForgeProfileJson()

    @Test
    fun `accepts public quest started event`() {
        val parsed = ForgeEventParser.parse(QUEST_STARTED_JSON, forgeProfileJson)
        assertTrue(parsed is ForgeEventParseResult.Accepted)
        val record = (parsed as ForgeEventParseResult.Accepted).record
        assertEquals("QUEST_STARTED", record.eventType)
        assertEquals(ForgeEventScopes.PUBLIC, record.scope)
    }

    @Test
    fun `skips protected scope`() {
        val json = QUEST_STARTED_JSON.replace("\"PUBLIC\"", "\"PROTECTED\"")
        val parsed = ForgeEventParser.parse(json, forgeProfileJson)
        assertTrue(parsed is ForgeEventParseResult.Skipped)
    }

    @Test
    fun `accepts lesson scope for forge profile ingest`() {
        val json = QUEST_STARTED_JSON.replace("\"PUBLIC\"", "\"LESSON\"")
        val parsed = ForgeEventParser.parse(json, forgeProfileJson)
        assertTrue(parsed is ForgeEventParseResult.Accepted)
    }

    @Test
    fun `quarantines invalid event id`() {
        val json = QUEST_STARTED_JSON.replace("EVT_dev_1_0001", "BAD_ID")
        val parsed = ForgeEventParser.parse(json, forgeProfileJson)
        assertTrue(parsed is ForgeEventParseResult.Quarantined)
    }

    private companion object {
        val QUEST_STARTED_JSON = """
            {
              "schemaVersion": 1,
              "eventId": "EVT_dev_1_0001",
              "eventType": "QUEST_STARTED",
              "scope": "PUBLIC",
              "actorApp": "student_edition",
              "actorDeviceId": "dev",
              "studentUid": "child-uuid-1",
              "logicalClock": 1,
              "epochMs": 1000,
              "payload": {"questId": "tile_a"}
            }
        """.trimIndent()
    }
}
