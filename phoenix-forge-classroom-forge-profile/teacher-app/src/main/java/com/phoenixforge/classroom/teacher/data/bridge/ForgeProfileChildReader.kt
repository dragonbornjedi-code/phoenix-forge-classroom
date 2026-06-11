package com.phoenixforge.classroom.teacher.data.bridge

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import com.phoenixforge.classroom.teacher.data.forgeprofile.TeacherForgeProfileContract
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class ForgeChildProfileRow(
    val uid: String,
    val forgeName: String,
    val currentStage: String?,
    val currentTitle: String?,
    val profileRole: String?,
)

data class ForgeLinkedStudentRow(
    val profileUid: String,
    val displayName: String,
    val linkedAtEpochMillis: Long?,
    val notes: String?,
)

data class ForgeEventRow(
    val eventId: String,
    val eventType: String,
    val scope: String,
    val actorApp: String,
    val logicalClock: Long,
    val epochMs: Long,
)

data class ForgeTimelineRow(
    val title: String,
    val type: String,
    val timestampEpochMillis: Long,
)

@Singleton
class ForgeProfileChildReader @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun canReadForgeProfile(): Boolean =
        context.checkSelfPermission(TeacherForgeProfileContract.READ_PERMISSION) ==
            PackageManager.PERMISSION_GRANTED

    fun listChildProfiles(): List<ForgeChildProfileRow> {
        if (!canReadForgeProfile()) return emptyList()
        return queryRows(
            uri = Uri.parse("content://${TeacherForgeProfileContract.AUTHORITY}/child_profiles"),
            columns = arrayOf(
                TeacherForgeProfileContract.Columns.UID,
                TeacherForgeProfileContract.Columns.FORGE_NAME,
                TeacherForgeProfileContract.Columns.CURRENT_STAGE,
                TeacherForgeProfileContract.Columns.CURRENT_TITLE,
                TeacherForgeProfileContract.Columns.PROFILE_ROLE,
            ),
        ) { cursor ->
            ForgeChildProfileRow(
                uid = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.UID).orEmpty(),
                forgeName = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.FORGE_NAME).orEmpty(),
                currentStage = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.CURRENT_STAGE),
                currentTitle = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.CURRENT_TITLE),
                profileRole = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.PROFILE_ROLE),
            )
        }
    }

    fun listLinkedStudents(): List<ForgeLinkedStudentRow> {
        if (!canReadForgeProfile()) return emptyList()
        return queryRows(
            uri = Uri.parse("content://${TeacherForgeProfileContract.AUTHORITY}/linked_students"),
            columns = arrayOf("uid", "display_name", "linked_at_epoch_millis", "notes"),
        ) { cursor ->
            ForgeLinkedStudentRow(
                profileUid = cursor.getStringOrNull("uid").orEmpty(),
                displayName = cursor.getStringOrNull("display_name").orEmpty(),
                linkedAtEpochMillis = cursor.getLongOrNull("linked_at_epoch_millis"),
                notes = cursor.getStringOrNull("notes"),
            )
        }
    }

    fun readChildProfile(studentUid: String): ForgeChildProfileRow? {
        if (!canReadForgeProfile()) return null
        val uri = Uri.parse(
            "content://${TeacherForgeProfileContract.AUTHORITY}/child_profile/$studentUid/profile",
        )
        return querySingle(uri, arrayOf(
            TeacherForgeProfileContract.Columns.UID,
            TeacherForgeProfileContract.Columns.FORGE_NAME,
            TeacherForgeProfileContract.Columns.CURRENT_STAGE,
            TeacherForgeProfileContract.Columns.CURRENT_TITLE,
            TeacherForgeProfileContract.Columns.PROFILE_ROLE,
        )) { cursor ->
            ForgeChildProfileRow(
                uid = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.UID).orEmpty(),
                forgeName = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.FORGE_NAME).orEmpty(),
                currentStage = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.CURRENT_STAGE),
                currentTitle = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.CURRENT_TITLE),
                profileRole = cursor.getStringOrNull(TeacherForgeProfileContract.Columns.PROFILE_ROLE),
            )
        }
    }

    fun readForgeEvents(studentUid: String): List<ForgeEventRow> {
        if (!canReadForgeProfile()) return emptyList()
        val uri = Uri.parse(
            "content://${TeacherForgeProfileContract.AUTHORITY}/child_profile/$studentUid/events",
        )
        return queryRows(
            uri = uri,
            columns = arrayOf(
                "event_id",
                "event_type",
                "event_scope",
                "actor_app",
                "logical_clock",
                "epoch_ms",
            ),
        ) { cursor ->
            ForgeEventRow(
                eventId = cursor.getStringOrNull("event_id").orEmpty(),
                eventType = cursor.getStringOrNull("event_type").orEmpty(),
                scope = cursor.getStringOrNull("event_scope").orEmpty(),
                actorApp = cursor.getStringOrNull("actor_app").orEmpty(),
                logicalClock = cursor.getLongOrNull("logical_clock") ?: 0L,
                epochMs = cursor.getLongOrNull("epoch_ms") ?: 0L,
            )
        }
    }

    fun readChildTimeline(studentUid: String): List<ForgeTimelineRow> {
        if (!canReadForgeProfile()) return emptyList()
        val uri = Uri.parse(
            "content://${TeacherForgeProfileContract.AUTHORITY}/child_profile/$studentUid/timeline",
        )
        return queryRows(
            uri = uri,
            columns = arrayOf("title", "type", "timestamp"),
        ) { cursor ->
            ForgeTimelineRow(
                title = cursor.getStringOrNull("title").orEmpty(),
                type = cursor.getStringOrNull("type").orEmpty(),
                timestampEpochMillis = cursor.getLongOrNull("timestamp") ?: 0L,
            )
        }
    }

    private fun <T> querySingle(
        uri: Uri,
        columns: Array<String>,
        mapRow: (Cursor) -> T,
    ): T? = context.contentResolver.query(uri, columns, null, null, null)?.use { cursor ->
        if (!cursor.moveToFirst()) return null
        mapRow(cursor)
    }

    private fun <T> queryRows(
        uri: Uri,
        columns: Array<String>,
        mapRow: (Cursor) -> T,
    ): List<T> = context.contentResolver.query(uri, columns, null, null, null)?.use { cursor ->
        buildList {
            while (cursor.moveToNext()) {
                add(mapRow(cursor))
            }
        }
    }.orEmpty()

    private fun Cursor.getStringOrNull(column: String): String? {
        val index = getColumnIndex(column)
        return if (index < 0 || isNull(index)) null else getString(index)
    }

    private fun Cursor.getLongOrNull(column: String): Long? {
        val index = getColumnIndex(column)
        return if (index < 0 || isNull(index)) null else getLong(index)
    }
}
