package com.phoenixforge.profile.data.sync

import android.util.Log
import com.phoenixforge.profile.data.local.dao.EventRecordDao
import com.phoenixforge.profile.data.local.entity.EventRecordEntity
import com.phoenixforge.profile.data.serialization.ForgeProfileJson
import kotlinx.serialization.json.JsonElement
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

data class EventIngestOutcome(
    val eventId: String?,
    val status: EventIngestStatus,
    val detail: String,
)

enum class EventIngestStatus {
    INGESTED,
    ALREADY_PRESENT,
    SKIPPED,
    QUARANTINED,
}

@Singleton
class EventIngester @Inject constructor(
    private val eventRecordDao: EventRecordDao,
    private val eventTimelineProjector: EventTimelineProjector,
    private val publicStateSnapshotWriter: PublicStateSnapshotWriter,
    private val forgeProfileJson: ForgeProfileJson,
) {
    suspend fun ingestDirectory(eventsDir: File, expectedStudentUid: String): List<EventIngestOutcome> {
        if (!eventsDir.isDirectory) return emptyList()
        return eventsDir.listFiles()
            ?.filter { it.isFile && it.name.startsWith("EVT_") && it.name.endsWith(".json") }
            ?.sortedBy { it.name }
            ?.map { ingestFile(it, expectedStudentUid) }
            .orEmpty()
    }

    suspend fun ingestFile(file: File, expectedStudentUid: String): EventIngestOutcome {
        val parsed = ForgeEventParser.parse(file.readText(), forgeProfileJson)
        return when (parsed) {
            is ForgeEventParseResult.Quarantined -> EventIngestOutcome(
                eventId = null,
                status = EventIngestStatus.QUARANTINED,
                detail = parsed.reason,
            )
            is ForgeEventParseResult.Skipped -> EventIngestOutcome(
                eventId = null,
                status = EventIngestStatus.SKIPPED,
                detail = parsed.reason,
            )
            is ForgeEventParseResult.Accepted -> persistAccepted(parsed.record, file, expectedStudentUid)
        }
    }

    private suspend fun persistAccepted(
        record: ForgeEventRecord,
        file: File,
        expectedStudentUid: String,
    ): EventIngestOutcome {
        if (record.studentUid != expectedStudentUid) {
            return EventIngestOutcome(
                eventId = record.eventId,
                status = EventIngestStatus.QUARANTINED,
                detail = "studentUid mismatch: expected $expectedStudentUid got ${record.studentUid}",
            )
        }

        val row = EventRecordEntity(
            eventId = record.eventId,
            eventType = record.eventType,
            scope = record.scope,
            actorApp = record.actorApp,
            actorDeviceId = record.actorDeviceId,
            studentUid = record.studentUid,
            logicalClock = record.logicalClock,
            epochMs = record.epochMs,
            payloadJson = forgeProfileJson.json.encodeToString(JsonElement.serializer(), record.payload),
            sourcePath = file.absolutePath,
        )

        val inserted = eventRecordDao.insertEvent(row)
        return if (inserted == -1L) {
            EventIngestOutcome(
                eventId = record.eventId,
                status = EventIngestStatus.ALREADY_PRESENT,
                detail = "duplicate eventId",
            )
        } else {
            runCatching { eventTimelineProjector.projectIfAbsent(record) }
                .onFailure { /* timeline projection is best-effort in 1.04 */ }
            runCatching { publicStateSnapshotWriter.projectAndWrite(expectedStudentUid) }
                .onFailure { /* public_state projection is best-effort until 0.68 verified */ }
            Log.i(TAG, "ingested ${record.eventType} ${record.eventId} clock=${record.logicalClock}")
            EventIngestOutcome(
                eventId = record.eventId,
                status = EventIngestStatus.INGESTED,
                detail = record.eventType,
            )
        }
    }

    private companion object {
        const val TAG = "EventIngester"
    }
}

object ForgeEventParser {
    fun parse(raw: String, forgeProfileJson: ForgeProfileJson): ForgeEventParseResult {
        if (raw.isBlank()) {
            return ForgeEventParseResult.Quarantined("empty file")
        }

        val record = runCatching {
            forgeProfileJson.json.decodeFromString(ForgeEventRecord.serializer(), raw)
        }.getOrElse {
            return ForgeEventParseResult.Quarantined(it.message ?: "invalid json")
        }

        if (record.schemaVersion != 1) {
            return ForgeEventParseResult.Quarantined("unsupported schemaVersion ${record.schemaVersion}")
        }
        if (!record.eventId.startsWith("EVT_")) {
            return ForgeEventParseResult.Quarantined("invalid eventId ${record.eventId}")
        }
        if (record.studentUid.isBlank()) {
            return ForgeEventParseResult.Quarantined("missing studentUid")
        }

        return when (record.scope) {
            ForgeEventScopes.PUBLIC,
            ForgeEventScopes.LESSON,
            -> ForgeEventParseResult.Accepted(record)
            ForgeEventScopes.PROTECTED ->
                ForgeEventParseResult.Skipped("scope PROTECTED not ingested from sync folder")
            else -> ForgeEventParseResult.Skipped("unknown scope ${record.scope}")
        }
    }
}
