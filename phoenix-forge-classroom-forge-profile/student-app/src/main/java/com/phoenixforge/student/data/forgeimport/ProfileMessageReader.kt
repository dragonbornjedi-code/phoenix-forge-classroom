package com.phoenixforge.student.data.forgeimport

import android.content.Context
import android.net.Uri
import com.phoenixforge.student.domain.model.StudentInboxMessage
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileMessageReader @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun readStudentMessages(): List<StudentInboxMessage> {
        val uri = Uri.parse("content://com.phoenixforge.profile.provider/messages")
            .buildUpon()
            .appendQueryParameter("targetApp", "student_edition")
            .build()
        return runCatching {
            context.contentResolver.query(uri, null, null, null, "epoch_ms DESC")?.use { cursor ->
                val idCol = cursor.getColumnIndex("message_id")
                val subjectCol = cursor.getColumnIndex("subject")
                val bodyCol = cursor.getColumnIndex("body_markdown")
                val fromCol = cursor.getColumnIndex("from_display_name")
                val epochCol = cursor.getColumnIndex("epoch_ms")
                val readCol = cursor.getColumnIndex("read_epoch_ms")
                val directionCol = cursor.getColumnIndex("direction")
                if (idCol < 0 || subjectCol < 0 || bodyCol < 0) return@use emptyList()
                buildList {
                    while (cursor.moveToNext()) {
                        add(
                            StudentInboxMessage(
                                messageId = cursor.getString(idCol),
                                subject = cursor.getString(subjectCol),
                                body = cursor.getString(bodyCol),
                                fromDisplayName = cursor.getString(fromCol).orEmpty().ifBlank { "Grown-up" },
                                epochMs = cursor.getLong(epochCol),
                                isRead = !cursor.isNull(readCol),
                                direction = cursor.getString(directionCol).orEmpty(),
                            ),
                        )
                    }
                }
            }.orEmpty()
        }.getOrDefault(emptyList())
    }
}
