package com.phoenixforge.profile.data.sync

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ForgeMessageRecord(
    @SerialName("schemaVersion") val schemaVersion: Int = 1,
    @SerialName("messageId") val messageId: String,
    @SerialName("threadId") val threadId: String,
    @SerialName("direction") val direction: String,
    @SerialName("fromDeviceId") val fromDeviceId: String,
    @SerialName("fromDisplayName") val fromDisplayName: String = "",
    @SerialName("toStudentUid") val toStudentUid: String,
    @SerialName("targetApp") val targetApp: String = TARGET_ALL,
    @SerialName("epochMs") val epochMs: Long,
    @SerialName("logicalClock") val logicalClock: Long = 0,
    @SerialName("subject") val subject: String,
    @SerialName("bodyMarkdown") val bodyMarkdown: String,
    @SerialName("readEpochMs") val readEpochMs: Long? = null,
    @SerialName("replyToMessageId") val replyToMessageId: String? = null,
) {
    companion object {
        const val TARGET_ALL = "all"
        const val TARGET_STUDENT = "student_edition"
        const val TARGET_PROFILE = "forge_profile"
        const val DIRECTION_TO_STUDENT = "TO_STUDENT"
        const val DIRECTION_TO_STEWARD = "TO_STEWARD"
    }
}

object ForgeMessageParser {
    private val json = Json { ignoreUnknownKeys = true }

    fun parse(raw: String): Result<ForgeMessageRecord> = runCatching {
        json.decodeFromString(ForgeMessageRecord.serializer(), raw)
    }
}
