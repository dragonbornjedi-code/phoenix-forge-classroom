package com.phoenixforge.profile.data.sync

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.phoenixforge.profile.data.local.ProfileDatabase
import com.phoenixforge.profile.data.serialization.ForgeProfileJson
import com.phoenixforge.profile.domain.model.EventType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class EventIngesterTest {

    private lateinit var database: ProfileDatabase
    private lateinit var ingester: EventIngester
    private val forgeProfileJson = ForgeProfileJson()
    private val studentUid = "f4b1376b-9afc-459d-b84d-8b69116597ed"

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, ProfileDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        ingester = EventIngester(
            eventRecordDao = database.eventRecordDao,
            eventTimelineProjector = EventTimelineProjector(database.dao, forgeProfileJson),
            publicStateSnapshotWriter = PublicStateSnapshotWriter(
                PublicStateEventProjector(database.eventRecordDao, forgeProfileJson),
                forgeProfileJson,
            ),
            forgeProfileJson = forgeProfileJson,
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun ingestFile_persistsForgeEventAndTimeline() = runBlocking {
        val dir = File.createTempFile("events", "").apply { delete(); mkdirs() }
        val file = File(dir, "EVT_test_1000_0001.json")
        file.writeText(
            """
            {
              "schemaVersion": 1,
              "eventId": "EVT_test_1000_0001",
              "eventType": "QUEST_STARTED",
              "scope": "PUBLIC",
              "actorApp": "student_edition",
              "actorDeviceId": "test",
              "studentUid": "$studentUid",
              "logicalClock": 1,
              "epochMs": 1000,
              "payload": {"questId": "morning_circle"}
            }
            """.trimIndent(),
        )

        val outcome = ingester.ingestFile(file, studentUid)
        assertEquals(EventIngestStatus.INGESTED, outcome.status)
        assertEquals(1, database.eventRecordDao.countEventsForStudent(studentUid))

        val rows = database.dao.getTimelineEvents(studentUid).first()
        assertEquals(1, rows.size)
        assertTrue(rows.first().title.contains("morning_circle"))
        assertEquals(EventType.SYNC_EVENT, rows.first().type)
    }

    @Test
    fun ingestFile_skipsProtectedScope() = runBlocking {
        val dir = File.createTempFile("events2", "").apply { delete(); mkdirs() }
        val file = File(dir, "EVT_bad.json")
        file.writeText(
            """
            {
              "schemaVersion": 1,
              "eventId": "EVT_bad_1",
              "eventType": "QUEST_STARTED",
              "scope": "PROTECTED",
              "actorApp": "student_edition",
              "actorDeviceId": "test",
              "studentUid": "$studentUid",
              "logicalClock": 1,
              "epochMs": 1000,
              "payload": {}
            }
            """.trimIndent(),
        )

        val outcome = ingester.ingestFile(file, studentUid)
        assertEquals(EventIngestStatus.SKIPPED, outcome.status)
        assertEquals(0, database.eventRecordDao.countEventsForStudent(studentUid))
    }
}
