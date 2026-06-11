package com.phoenixforge.profile.data.projection

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.phoenixforge.profile.data.local.ProfileDatabase
import com.phoenixforge.profile.data.local.entity.EventRecordEntity
import com.phoenixforge.profile.data.serialization.ForgeProfileJson
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StateProjectorTest {

    private lateinit var database: ProfileDatabase
    private lateinit var projector: StateProjector
    private val studentUid = "f4b1376b-9afc-459d-b84d-8b69116597ed"

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, ProfileDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        projector = StateProjector(database.eventRecordDao, ForgeProfileJson())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun projectEvents_returnsTurnStateAlignedEnvelope() = runBlocking {
        database.eventRecordDao.insertEvent(
            EventRecordEntity(
                eventId = "EVT_dev_1_0001",
                eventType = "QUEST_STARTED",
                scope = "PUBLIC",
                actorApp = "student_edition",
                actorDeviceId = "dev",
                studentUid = studentUid,
                logicalClock = 1,
                epochMs = 1000,
                payloadJson = """{"questId":"tile_a"}""",
                sourcePath = "/tmp/EVT_dev_1_0001.json",
            ),
        )

        val projection = projector.projectEvents(studentUid)

        assertEquals(TURN_STATE_SCHEMA_VERSION, projection.schemaVersion)
        assertEquals(CLASSROOM_LANE, projection.lane)
        assertEquals(studentUid, projection.studentUid)
        assertEquals(1, projection.eventCount)
        assertTrue(projection.syncPaths.eventsDir.contains(studentUid))
        assertEquals("QUEST_STARTED", projection.events.single().eventType)
    }
}
