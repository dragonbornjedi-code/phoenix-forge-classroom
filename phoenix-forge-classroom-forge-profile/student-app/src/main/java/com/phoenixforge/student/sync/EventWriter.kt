package com.phoenixforge.student.sync

import android.content.Context
import com.phoenixforge.student.domain.session.StudentSessionStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.encodeToJsonElement
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Append-only PUBLIC event writer for the Syncthing sync tree (master spec P2).
 * Never writes PROTECTED scope.
 */
@Singleton
class EventWriter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionStore: StudentSessionStore,
    private val logicalClockStore: LogicalClockStore,
    private val deviceIdStore: StudentDeviceIdStore,
) {
    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    private val seqState = EventSequenceState()

    suspend fun writeQuestStarted(questId: String): Result<EventWriteResult> =
        writeEvent(
            eventType = ForgeEventTypes.QUEST_STARTED,
            scope = EventScope.PUBLIC,
            payload = json.encodeToJsonElement(QuestStartedPayload(questId = questId.trim())),
        )

    suspend fun writeQuestCompleted(questId: String, score: Int? = null): Result<EventWriteResult> =
        writeEvent(
            eventType = ForgeEventTypes.QUEST_COMPLETED,
            scope = EventScope.PUBLIC,
            payload = json.encodeToJsonElement(
                QuestCompletedPayload(questId = questId.trim(), score = score),
            ),
        )

    suspend fun writeXpEarned(xpAmount: Int, reason: String): Result<EventWriteResult> =
        writeEvent(
            eventType = ForgeEventTypes.XP_EARNED,
            scope = EventScope.PUBLIC,
            payload = json.encodeToJsonElement(XpEarnedPayload(xpAmount, reason)),
        )

    suspend fun writeLevelUp(newLevel: Int, previousLevel: Int): Result<EventWriteResult> =
        writeEvent(
            eventType = ForgeEventTypes.LEVEL_UP,
            scope = EventScope.PUBLIC,
            payload = json.encodeToJsonElement(LevelUpPayload(newLevel, previousLevel)),
        )

    suspend fun writeRoomUnlocked(roomId: String): Result<EventWriteResult> =
        writeEvent(
            eventType = ForgeEventTypes.ROOM_UNLOCKED,
            scope = EventScope.PUBLIC,
            payload = json.encodeToJsonElement(RoomUnlockedPayload(roomId)),
        )

    suspend fun writeUnlockFlag(flagId: String, value: Boolean = true): Result<EventWriteResult> =
        writeEvent(
            eventType = ForgeEventTypes.UNLOCK_FLAG_SET,
            scope = EventScope.PUBLIC,
            payload = json.encodeToJsonElement(UnlockFlagSetPayload(flagId, value)),
        )

    suspend fun writeCurrencyEarned(currencyId: String, amount: Int, reason: String): Result<EventWriteResult> =
        writeEvent(
            eventType = ForgeEventTypes.CURRENCY_EARNED,
            scope = EventScope.PUBLIC,
            payload = json.encodeToJsonElement(CurrencyEarnedPayload(currencyId, amount, reason)),
        )

    suspend fun writeCurrencySpent(
        currencyId: String,
        amount: Int,
        itemId: String,
        merchantId: String,
    ): Result<EventWriteResult> =
        writeEvent(
            eventType = ForgeEventTypes.CURRENCY_SPENT,
            scope = EventScope.PUBLIC,
            payload = json.encodeToJsonElement(
                CurrencySpentPayload(currencyId, amount, itemId, merchantId),
            ),
        )

    suspend fun writeInventoryItemAdded(
        itemId: String,
        category: String = "garden",
        placementZone: String = DigitalHomeWire.Rooms.GARDEN,
        meshHint: String = "",
    ): Result<EventWriteResult> =
        writeEvent(
            eventType = ForgeEventTypes.INVENTORY_ITEM_ADDED,
            scope = EventScope.PUBLIC,
            payload = json.encodeToJsonElement(
                InventoryItemAddedPayload(itemId, category, placementZone, meshHint),
            ),
        )

    suspend fun writeEvent(
        eventType: String,
        scope: EventScope,
        payload: JsonElement,
    ): Result<EventWriteResult> = withContext(Dispatchers.IO) {
        if (!EventScope.studentMayWrite(scope)) {
            return@withContext Result.failure(
                IllegalStateException("Student Edition cannot write scope ${scope.wireName}"),
            )
        }

        val studentUid = sessionStore.getActiveImportUid()?.trim().orEmpty()
        if (studentUid.isEmpty()) {
            return@withContext Result.failure(IllegalStateException("Not signed in — import child profile first."))
        }

        val deviceId = deviceIdStore.getOrCreate()
        val primaryEventsDir = resolvePrimaryEventsDir(studentUid)
        val logicalClock = logicalClockStore.nextClock(primaryEventsDir)
        val epochMs = System.currentTimeMillis()
        val eventId = seqState.nextEventId(deviceId, epochMs)

        val record = ForgeEventRecord(
            eventId = eventId,
            eventType = eventType,
            scope = scope.wireName,
            actorApp = ACTOR_APP,
            actorDeviceId = deviceId,
            studentUid = studentUid,
            logicalClock = logicalClock,
            epochMs = epochMs,
            payload = payload,
        )

        val encoded = json.encodeToString(record)
        val written = writeToAllSyncRoots(studentUid, eventId, encoded)

        if (written.isEmpty()) {
            return@withContext Result.failure(
                IllegalStateException("Could not write event file under PhoenixForge/sync/profiles/"),
            )
        }

        Result.success(
            EventWriteResult(
                record = record,
                writtenPaths = written,
                message = "Wrote ${record.eventType} (${record.eventId}) clock=${record.logicalClock}",
            ),
        )
    }

    private fun resolvePrimaryEventsDir(studentUid: String): File? {
        val relative = ManifestSyncPaths.eventsRelativePath(studentUid)
        ManifestSyncPaths.PUBLIC_SYNC_ROOTS.forEach { root ->
            val dir = File(root, relative)
            if (dir.exists() || dir.mkdirs()) return dir
        }
        return null
    }

    private fun writeToAllSyncRoots(studentUid: String, eventId: String, payload: String): List<String> {
        val relative = "${ManifestSyncPaths.eventsRelativePath(studentUid)}/${ManifestSyncPaths.eventFileName(eventId)}"
        val written = PublicSyncWriter.writeRelative(relative, payload).toMutableList()

        runCatching {
            val base = context.getExternalFilesDir(null) ?: return@runCatching
            val dir = File(base, ManifestSyncPaths.eventsRelativePath(studentUid)).apply { mkdirs() }
            val file = File(dir, ManifestSyncPaths.eventFileName(eventId))
            file.writeText(payload)
            written += file.absolutePath
        }

        return written
    }

    private companion object {
        const val ACTOR_APP = "student_edition"
    }
}

/** Per-process sequence counter; resets when epochMs changes (master spec burst writes). */
class EventSequenceState {
    @Volatile
    private var lastEpochMs: Long = -1L
    private var seqInEpoch: Int = 0

    @Synchronized
    fun nextEventId(deviceId: String, epochMs: Long): String {
        if (epochMs != lastEpochMs) {
            lastEpochMs = epochMs
            seqInEpoch = 0
        }
        seqInEpoch += 1
        return EventIdFactory.formatEventId(deviceId, epochMs, seqInEpoch)
    }
}

object EventIdFactory {
    fun formatEventId(deviceId: String, epochMs: Long, sequence: Int): String =
        "EVT_${deviceId}_${epochMs}_${sequence.toString().padStart(4, '0')}"
}
