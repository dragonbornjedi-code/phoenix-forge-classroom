package com.phoenixforge.student.sync

import com.phoenixforge.student.domain.model.MemoryEventDraft
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object MemoryEventContractParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parseContractLine(line: String): MemoryEventParseResult = runCatching {
        val root = json.parseToJsonElement(line).jsonObject
        validateAndMap(root, line)
    }.getOrElse { MemoryEventParseResult.Quarantined(it.message ?: "invalid json") }

    private fun validateAndMap(root: JsonObject, rawLine: String): MemoryEventParseResult {
        val missing = REQUIRED_ROOT_KEYS.filter { key -> key !in root.keys }
        if (missing.isNotEmpty()) {
            return MemoryEventParseResult.Quarantined("missing keys: ${missing.joinToString()}")
        }
        val narrative = root["narrative"]?.jsonObject
            ?: return MemoryEventParseResult.Quarantined("narrative missing")
        val narrativeMissing = REQUIRED_NARRATIVE_KEYS.filter { key -> key !in narrative.keys }
        if (narrativeMissing.isNotEmpty()) {
            return MemoryEventParseResult.Quarantined("narrative missing keys: ${narrativeMissing.joinToString()}")
        }
        val context = root["context"]?.jsonObject
            ?: return MemoryEventParseResult.Quarantined("context missing")
        val eventId = root["eventId"]?.jsonPrimitive?.contentOrNull.orEmpty()
        if (eventId.length < 8) {
            return MemoryEventParseResult.Quarantined("eventId too short")
        }
        val sourceShell = root["sourceShell"]?.jsonPrimitive?.contentOrNull.orEmpty()
        if (sourceShell != MemoryEventImporter.FORGE_WORLD_SOURCE_SHELL) {
            return MemoryEventParseResult.Quarantined("unexpected sourceShell: $sourceShell")
        }
        return MemoryEventParseResult.Valid(
            MemoryEventDraft(
                eventId = eventId,
                version = root["version"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                capturedAt = root["capturedAt"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                sourceShell = sourceShell,
                eventType = root["eventType"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                title = narrative["title"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                summary = narrative["summary"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                childMood = narrative["childMood"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                locationId = context["locationId"]?.jsonPrimitive?.contentOrNull.orEmpty(),
                contractJson = rawLine,
                importedAtEpochMillis = System.currentTimeMillis(),
                importSource = MemoryEventImporter.EXPORT_PATHS.first(),
            )
        )
    }

    private val REQUIRED_ROOT_KEYS = listOf(
        "eventId",
        "version",
        "capturedAt",
        "sourceShell",
        "eventType",
        "narrative",
        "context",
    )

    private val REQUIRED_NARRATIVE_KEYS = listOf("title", "summary", "childMood")
}
