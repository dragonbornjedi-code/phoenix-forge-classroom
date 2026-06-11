package com.phoenixforge.profile.data.projection

import com.phoenixforge.profile.data.local.dao.MessageDao
import com.phoenixforge.profile.data.local.entity.MessageEntity
import com.phoenixforge.profile.data.serialization.ForgeProfileJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class MessageProjectionItem(
    @SerialName("messageId") val messageId: String,
    @SerialName("threadId") val threadId: String,
    val direction: String,
    @SerialName("fromDeviceId") val fromDeviceId: String,
    @SerialName("fromDisplayName") val fromDisplayName: String,
    @SerialName("toStudentUid") val toStudentUid: String,
    @SerialName("targetApp") val targetApp: String,
    @SerialName("epochMs") val epochMs: Long,
    val subject: String,
    @SerialName("bodyMarkdown") val bodyMarkdown: String,
    @SerialName("readEpochMs") val readEpochMs: Long? = null,
    @SerialName("replyToMessageId") val replyToMessageId: String? = null,
)

@Serializable
data class MessagesProjectionResponse(
    @SerialName("schema_version") val schemaVersion: String = TURN_STATE_SCHEMA_VERSION,
    @SerialName("studentUid") val studentUid: String,
    @SerialName("message_count") val messageCount: Int,
    val messages: List<MessageProjectionItem>,
)

@Singleton
class MessageProjector @Inject constructor(
    private val messageDao: MessageDao,
    private val forgeProfileJson: ForgeProfileJson,
) {
    suspend fun projectMessages(
        studentUid: String,
        targetApp: String? = null,
    ): MessagesProjectionResponse = withContext(Dispatchers.IO) {
        val normalizedUid = studentUid.trim()
        val rows = if (targetApp.isNullOrBlank()) {
            messageDao.listForStudent(normalizedUid)
        } else {
            messageDao.listForStudentAndApp(normalizedUid, targetApp.trim())
        }
        MessagesProjectionResponse(
            studentUid = normalizedUid,
            messageCount = rows.size,
            messages = rows.map { it.toProjection() },
        )
    }

    fun projectMessagesBlocking(studentUid: String, targetApp: String? = null): MessagesProjectionResponse =
        runBlocking { projectMessages(studentUid, targetApp) }

    private fun MessageEntity.toProjection() = MessageProjectionItem(
        messageId = messageId,
        threadId = threadId,
        direction = direction,
        fromDeviceId = fromDeviceId,
        fromDisplayName = fromDisplayName,
        toStudentUid = toStudentUid,
        targetApp = targetApp,
        epochMs = epochMs,
        subject = subject,
        bodyMarkdown = bodyMarkdown,
        readEpochMs = readEpochMs,
        replyToMessageId = replyToMessageId,
    )
}
