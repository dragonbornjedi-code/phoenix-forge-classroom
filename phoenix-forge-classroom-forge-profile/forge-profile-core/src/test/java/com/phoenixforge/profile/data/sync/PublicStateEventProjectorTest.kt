package com.phoenixforge.profile.data.sync

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
class PublicStateEventProjectorTest {
    private lateinit var database: ProfileDatabase
    private lateinit var projector: PublicStateEventProjector
    private val studentUid = "ezra-test-uid"

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, ProfileDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        projector = PublicStateEventProjector(database.eventRecordDao, ForgeProfileJson())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun questRewards_unlockGardenAtLevel3() = runBlocking {
        val base = projector.project(studentUid)
        assertTrue(base.unlockedRooms.contains("BEDROOM"))
        assertEquals(1, base.level)

        database.eventRecordDao.insertEvent(
            event("XP_EARNED", """{"xpAmount":200,"reason":"quest:test"}""", 1),
        )
        database.eventRecordDao.insertEvent(
            event("CURRENCY_EARNED", """{"currencyId":"forge_tokens","amount":5,"reason":"quest:test"}""", 2),
        )
        database.eventRecordDao.insertEvent(
            event("UNLOCK_FLAG_SET", """{"flagId":"whisp.lumen","value":true}""", 3),
        )

        val projected = projector.project(studentUid)
        assertTrue(projected.xp >= 200)
        assertTrue(projected.level >= 3)
        assertTrue(projected.unlockedRooms.contains("GARDEN"))
        assertEquals(true, projected.unlockFlags["whisp.lumen"])
        assertEquals(5, projected.currency["forge_tokens"])
    }

    private fun event(type: String, payloadJson: String, clock: Long) = EventRecordEntity(
        eventId = "EVT_test_${type}_$clock",
        eventType = type,
        scope = ForgeEventScopes.PUBLIC,
        actorApp = "student_edition",
        actorDeviceId = "dev",
        studentUid = studentUid,
        logicalClock = clock,
        epochMs = clock,
        payloadJson = payloadJson,
        sourcePath = "/tmp/test.json",
    )
}
