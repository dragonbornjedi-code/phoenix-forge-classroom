package com.phoenixforge.profile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phoenixforge.profile.data.local.entity.MessageEntity

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: MessageEntity): Long

    @Query("SELECT EXISTS(SELECT 1 FROM forge_messages WHERE messageId = :messageId)")
    suspend fun exists(messageId: String): Boolean

    @Query(
        """
        SELECT * FROM forge_messages
        WHERE toStudentUid = :studentUid
        ORDER BY epochMs DESC
        """,
    )
    suspend fun listForStudent(studentUid: String): List<MessageEntity>

    @Query(
        """
        SELECT * FROM forge_messages
        WHERE toStudentUid = :studentUid
          AND (targetApp = :targetApp OR targetApp = 'all')
        ORDER BY epochMs DESC
        """,
    )
    suspend fun listForStudentAndApp(studentUid: String, targetApp: String): List<MessageEntity>

    @Query("UPDATE forge_messages SET readEpochMs = :readAt WHERE messageId = :messageId")
    suspend fun markRead(messageId: String, readAt: Long)
}
