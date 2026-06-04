package com.phoenixforge.student.data.forgeimport

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import com.phoenixforge.student.domain.model.ImportedProfileSnapshot
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Client-side Forge Profile ContentProvider contract.
 * Does NOT depend on forge-profile-core — URI/column mirror only.
 */
object ForgeProfileContract {
    const val AUTHORITY = "com.phoenixforge.profile.provider"
    const val READ_PERMISSION = "com.phoenixforge.profile.READ"

    val PROFILE_URI: Uri = Uri.parse("content://$AUTHORITY/profile")
    val AVATAR_URI: Uri = Uri.parse("content://$AUTHORITY/avatar")
    val TIMELINE_URI: Uri = Uri.parse("content://$AUTHORITY/timeline")

    object Columns {
        const val UID = "uid"
        const val FORGE_NAME = "forge_name"
        const val CURRENT_STAGE = "current_stage"
        const val CURRENT_TITLE = "current_title"
        const val AVATAR_HAIR = "hair_type"
        const val AVATAR_EYES = "eye_color"
        const val AVATAR_SKIN = "skin_tone"
        const val AVATAR_CLOTHING = "clothing_id"
        const val AVATAR_VERSION = "avatar_version"
        const val EVENT_TITLE = "title"
        const val EVENT_TYPE = "type"
        const val EVENT_TIMESTAMP = "timestamp"
    }
}

data class ForgeProfilePreview(
    val uid: String,
    val forgeName: String,
    val currentStage: String?,
    val currentTitle: String?,
    val avatarSummary: String?,
    val timelineEventCount: Int,
    val isAvailable: Boolean,
    val errorMessage: String?
)

@Singleton
class ForgeProfileImporter @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val json = Json { ignoreUnknownKeys = true }

    fun canAccessProvider(): Boolean =
        context.checkSelfPermission(ForgeProfileContract.READ_PERMISSION) == PackageManager.PERMISSION_GRANTED

    fun probeProfile(): ForgeProfilePreview {
        if (!canAccessProvider()) {
            return ForgeProfilePreview(
                uid = "",
                forgeName = "",
                currentStage = null,
                currentTitle = null,
                avatarSummary = null,
                timelineEventCount = 0,
                isAvailable = false,
                errorMessage = "Forge Profile not accessible. Install Forge Profile or grant read permission."
            )
        }
        return try {
            val profile = readProfileRow()
            if (profile == null) {
                ForgeProfilePreview(
                    uid = "",
                    forgeName = "",
                    currentStage = null,
                    currentTitle = null,
                    avatarSummary = null,
                    timelineEventCount = 0,
                    isAvailable = false,
                    errorMessage = "No Forge Profile found on this device."
                )
            } else {
                ForgeProfilePreview(
                    uid = profile.uid,
                    forgeName = profile.forgeName,
                    currentStage = profile.currentStage,
                    currentTitle = profile.currentTitle,
                    avatarSummary = readAvatarSummary(),
                    timelineEventCount = readTimelineCount(),
                    isAvailable = true,
                    errorMessage = null
                )
            }
        } catch (e: SecurityException) {
            ForgeProfilePreview(
                uid = "",
                forgeName = "",
                currentStage = null,
                currentTitle = null,
                avatarSummary = null,
                timelineEventCount = 0,
                isAvailable = false,
                errorMessage = e.message ?: "Unauthorized Forge Profile access"
            )
        }
    }

    fun importSnapshot(): ImportedProfileSnapshot? {
        val preview = probeProfile()
        if (!preview.isAvailable) return null
        val timeline = readTimelineEvents()
        return ImportedProfileSnapshot(
            id = UUID.randomUUID().toString(),
            uid = preview.uid,
            forgeName = preview.forgeName,
            currentStage = preview.currentStage,
            currentTitle = preview.currentTitle,
            avatarSummary = preview.avatarSummary,
            timelineSummary = json.encodeToString(timeline),
            importedAtEpochMillis = System.currentTimeMillis()
        )
    }

    private data class ProfileRow(
        val uid: String,
        val forgeName: String,
        val currentStage: String?,
        val currentTitle: String?
    )

    private fun readProfileRow(): ProfileRow? =
        context.contentResolver.query(
            ForgeProfileContract.PROFILE_URI,
            arrayOf(
                ForgeProfileContract.Columns.UID,
                ForgeProfileContract.Columns.FORGE_NAME,
                ForgeProfileContract.Columns.CURRENT_STAGE,
                ForgeProfileContract.Columns.CURRENT_TITLE
            ),
            null,
            null,
            null
        )?.use { cursor ->
            if (!cursor.moveToFirst()) return null
            ProfileRow(
                uid = cursor.getString(cursor.col(ForgeProfileContract.Columns.UID)),
                forgeName = cursor.getString(cursor.col(ForgeProfileContract.Columns.FORGE_NAME)),
                currentStage = cursor.getStringOrNull(ForgeProfileContract.Columns.CURRENT_STAGE),
                currentTitle = cursor.getStringOrNull(ForgeProfileContract.Columns.CURRENT_TITLE)
            )
        }

    private fun readAvatarSummary(): String? =
        context.contentResolver.query(
            ForgeProfileContract.AVATAR_URI,
            arrayOf(
                ForgeProfileContract.Columns.AVATAR_HAIR,
                ForgeProfileContract.Columns.AVATAR_EYES,
                ForgeProfileContract.Columns.AVATAR_SKIN,
                ForgeProfileContract.Columns.AVATAR_CLOTHING,
                ForgeProfileContract.Columns.AVATAR_VERSION
            ),
            null,
            null,
            null
        )?.use { cursor ->
            if (!cursor.moveToFirst()) return null
            listOf(
                cursor.getString(cursor.col(ForgeProfileContract.Columns.AVATAR_HAIR)),
                cursor.getString(cursor.col(ForgeProfileContract.Columns.AVATAR_EYES)),
                cursor.getString(cursor.col(ForgeProfileContract.Columns.AVATAR_SKIN)),
                cursor.getString(cursor.col(ForgeProfileContract.Columns.AVATAR_CLOTHING)),
                "v${cursor.getInt(cursor.col(ForgeProfileContract.Columns.AVATAR_VERSION))}"
            ).joinToString("|")
        }

    private fun readTimelineCount(): Int =
        context.contentResolver.query(
            ForgeProfileContract.TIMELINE_URI,
            arrayOf(ForgeProfileContract.Columns.EVENT_TITLE),
            null,
            null,
            null
        )?.use { it.count } ?: 0

    private fun readTimelineEvents(): List<Map<String, String>> =
        context.contentResolver.query(
            ForgeProfileContract.TIMELINE_URI,
            arrayOf(
                ForgeProfileContract.Columns.EVENT_TITLE,
                ForgeProfileContract.Columns.EVENT_TYPE,
                ForgeProfileContract.Columns.EVENT_TIMESTAMP
            ),
            null,
            null,
            null
        )?.use { cursor -> cursor.toTimelineMaps() }.orEmpty()

    private fun Cursor.toTimelineMaps(): List<Map<String, String>> {
        val titleCol = col(ForgeProfileContract.Columns.EVENT_TITLE)
        val typeCol = col(ForgeProfileContract.Columns.EVENT_TYPE)
        val tsCol = col(ForgeProfileContract.Columns.EVENT_TIMESTAMP)
        val events = mutableListOf<Map<String, String>>()
        while (moveToNext()) {
            events += mapOf(
                "title" to getString(titleCol),
                "type" to getString(typeCol),
                "timestamp" to getLong(tsCol).toString()
            )
        }
        return events
    }

    private fun Cursor.col(name: String): Int {
        val index = getColumnIndex(name)
        require(index >= 0) { "Column not found: $name" }
        return index
    }

    private fun Cursor.getStringOrNull(column: String): String? {
        val index = getColumnIndex(column)
        return if (index >= 0 && !isNull(index)) getString(index) else null
    }
}
