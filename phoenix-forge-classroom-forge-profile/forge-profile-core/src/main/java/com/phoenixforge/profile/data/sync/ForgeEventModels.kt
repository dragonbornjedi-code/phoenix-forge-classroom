package com.phoenixforge.profile.data.sync

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

object ForgeEventScopes {
    const val PUBLIC = "PUBLIC"
    const val LESSON = "LESSON"
    const val PROTECTED = "PROTECTED"
}

@Serializable
data class ForgeEventRecord(
    val schemaVersion: Int = 1,
    val eventId: String,
    val eventType: String,
    val scope: String,
    val actorApp: String,
    val actorDeviceId: String,
    val studentUid: String,
    val logicalClock: Long,
    val epochMs: Long,
    val payload: JsonElement,
)

sealed class ForgeEventParseResult {
    data class Accepted(val record: ForgeEventRecord) : ForgeEventParseResult()
    data class Skipped(val reason: String) : ForgeEventParseResult()
    data class Quarantined(val reason: String) : ForgeEventParseResult()
}
