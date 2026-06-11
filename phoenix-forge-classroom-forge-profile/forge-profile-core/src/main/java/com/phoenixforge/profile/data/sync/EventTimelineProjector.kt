package com.phoenixforge.profile.data.sync

import com.phoenixforge.profile.data.local.dao.ProfileDao
import com.phoenixforge.profile.data.local.entity.TimelineEventEntity
import com.phoenixforge.profile.data.serialization.ForgeProfileJson
import com.phoenixforge.profile.domain.model.EventType
import com.phoenixforge.profile.domain.model.TimelineMetadata
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Projects ingested forge_events into childhood timeline rows (master spec 1.04).
 */
@Singleton
class EventTimelineProjector @Inject constructor(
    private val profileDao: ProfileDao,
    private val forgeProfileJson: ForgeProfileJson,
) {
    suspend fun projectIfAbsent(record: ForgeEventRecord): Boolean {
        val title = titleFor(record)
        val metadata = listOf(
            TimelineMetadata("sync", "eventType", record.eventType),
            TimelineMetadata("sync", "actorApp", record.actorApp),
            TimelineMetadata("sync", "actorDeviceId", record.actorDeviceId),
            TimelineMetadata("sync", "scope", record.scope),
            TimelineMetadata("sync", "logicalClock", record.logicalClock.toString()),
            TimelineMetadata("sync", "payloadSummary", payloadSummary(record.payload)),
        )
        val entity = TimelineEventEntity(
            id = record.eventId,
            profileUid = record.studentUid,
            title = title,
            type = EventType.SYNC_EVENT,
            timestamp = record.epochMs,
            metadataJson = forgeProfileJson.encodeTimelineMetadata(metadata),
        )
        profileDao.insertTimelineEvent(entity)
        return true
    }

    private fun titleFor(record: ForgeEventRecord): String {
        val payload = record.payload
        return when (record.eventType) {
            "QUEST_STARTED" -> {
                val quest = payload.stringField("questId") ?: "a quest"
                "Started quest · $quest"
            }
            "QUEST_COMPLETED" -> {
                val quest = payload.stringField("questId") ?: "a quest"
                "Finished quest · $quest"
            }
            "LEVEL_UP" -> {
                val level = payload.intField("newLevel") ?: payload.intField("level")
                if (level != null) "Leveled up to $level" else "Leveled up"
            }
            "COMPANION_EARNED" -> {
                val name = payload.stringField("companionName") ?: payload.stringField("companionId")
                if (name != null) "Companion joined · $name" else "Earned a companion"
            }
            "LESSON_STARTED" -> {
                val title = payload.stringField("title") ?: payload.stringField("manifestId")
                if (title != null) "Lesson began · $title" else "Lesson began"
            }
            else -> when (record.actorApp) {
                "forge_world" -> "Forge World · ${record.eventType.replace('_', ' ').lowercase()}"
                "student_edition" -> "Student · ${record.eventType.replace('_', ' ').lowercase()}"
                else -> record.eventType.replace('_', ' ').lowercase().replaceFirstChar { it.titlecase() }
            }
        }
    }

    private fun payloadSummary(payload: JsonElement): String =
        when (payload) {
            is JsonObject -> payload.entries
                .take(4)
                .joinToString(", ") { (key, value) ->
                    "$key=${value.primitiveOrNull()?.contentOrNull ?: value.toString()}"
                }
            else -> payload.toString()
        }

    private fun JsonElement.stringField(key: String): String? =
        (this as? JsonObject)?.get(key)?.primitiveOrNull()?.contentOrNull

    private fun JsonElement.intField(key: String): Int? =
        stringField(key)?.toIntOrNull()
            ?: (this as? JsonObject)?.get(key)?.primitiveOrNull()?.contentOrNull?.toIntOrNull()

    private fun JsonElement.primitiveOrNull(): JsonPrimitive? = this as? JsonPrimitive
}
