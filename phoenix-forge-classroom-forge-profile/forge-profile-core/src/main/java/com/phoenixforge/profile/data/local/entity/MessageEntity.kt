package com.phoenixforge.profile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forge_messages")
data class MessageEntity(
    @PrimaryKey val messageId: String,
    val threadId: String,
    val direction: String,
    val fromDeviceId: String,
    val fromDisplayName: String,
    val toStudentUid: String,
    val targetApp: String,
    val epochMs: Long,
    val logicalClock: Long,
    val subject: String,
    val bodyMarkdown: String,
    val readEpochMs: Long?,
    val replyToMessageId: String?,
    val sourcePath: String,
    val ingestedAtMs: Long = System.currentTimeMillis(),
)
