package com.phoenixforge.profile.data.sync

import com.phoenixforge.profile.data.local.dao.MessageDao
import com.phoenixforge.profile.data.local.entity.MessageEntity
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

enum class MessageIngestStatus { INGESTED, ALREADY_PRESENT, QUARANTINED }

data class MessageIngestOutcome(
    val messageId: String?,
    val status: MessageIngestStatus,
    val detail: String,
)

@Singleton
class MessageIngester @Inject constructor(
    private val messageDao: MessageDao,
) {
    suspend fun ingestDirectory(messagesDir: File, expectedStudentUid: String): List<MessageIngestOutcome> {
        if (!messagesDir.isDirectory) return emptyList()
        return messagesDir.listFiles()
            ?.filter { it.isFile && it.name.startsWith("MSG_") && it.name.endsWith(".json") }
            ?.sortedBy { it.name }
            ?.map { ingestFile(it, expectedStudentUid) }
            .orEmpty()
    }

    suspend fun ingestFile(file: File, expectedStudentUid: String): MessageIngestOutcome {
        val parsed = ForgeMessageParser.parse(file.readText()).getOrElse {
            return MessageIngestOutcome(null, MessageIngestStatus.QUARANTINED, it.message ?: "invalid json")
        }
        return persist(parsed, file.absolutePath, expectedStudentUid)
    }

    suspend fun ingestRecord(record: ForgeMessageRecord, sourcePath: String): MessageIngestOutcome =
        persist(record, sourcePath, record.toStudentUid)

    private suspend fun persist(
        record: ForgeMessageRecord,
        sourcePath: String,
        expectedStudentUid: String,
    ): MessageIngestOutcome {
        if (record.toStudentUid != expectedStudentUid) {
            return MessageIngestOutcome(
                record.messageId,
                MessageIngestStatus.QUARANTINED,
                "studentUid mismatch",
            )
        }
        if (messageDao.exists(record.messageId)) {
            return MessageIngestOutcome(record.messageId, MessageIngestStatus.ALREADY_PRESENT, "duplicate")
        }
        messageDao.insert(
            MessageEntity(
                messageId = record.messageId,
                threadId = record.threadId,
                direction = record.direction,
                fromDeviceId = record.fromDeviceId,
                fromDisplayName = record.fromDisplayName,
                toStudentUid = record.toStudentUid,
                targetApp = record.targetApp,
                epochMs = record.epochMs,
                logicalClock = record.logicalClock,
                subject = record.subject,
                bodyMarkdown = record.bodyMarkdown,
                readEpochMs = record.readEpochMs,
                replyToMessageId = record.replyToMessageId,
                sourcePath = sourcePath,
            ),
        )
        return MessageIngestOutcome(record.messageId, MessageIngestStatus.INGESTED, "ok")
    }
}
