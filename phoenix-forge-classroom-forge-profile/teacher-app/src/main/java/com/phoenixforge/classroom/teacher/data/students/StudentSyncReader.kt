package com.phoenixforge.classroom.teacher.data.students

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import javax.inject.Inject
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class StudentSyncReader @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun canAccessProvider(): Boolean =
        context.checkSelfPermission(StudentSyncContract.READ_PERMISSION) == PackageManager.PERMISSION_GRANTED

    suspend fun readProfileSnapshot(): StudentProfileSnapshot? = withContext(Dispatchers.IO) {
        if (!canAccessProvider()) return@withContext null
        querySingle(
            uri = StudentSyncContract.PROFILE_SNAPSHOT_URI,
            columns = arrayOf(
                StudentSyncContract.Columns.UID,
                StudentSyncContract.Columns.FORGE_NAME,
                StudentSyncContract.Columns.CURRENT_STAGE,
                StudentSyncContract.Columns.CURRENT_TITLE,
                StudentSyncContract.Columns.AVATAR_SUMMARY,
                StudentSyncContract.Columns.TIMELINE_SUMMARY,
                StudentSyncContract.Columns.IMPORTED_AT_EPOCH_MILLIS
            )
        ) { cursor ->
            StudentProfileSnapshot(
                uid = cursor.getStringOrNull(StudentSyncContract.Columns.UID).orEmpty(),
                forgeName = cursor.getStringOrNull(StudentSyncContract.Columns.FORGE_NAME).orEmpty(),
                currentStage = cursor.getStringOrNull(StudentSyncContract.Columns.CURRENT_STAGE),
                currentTitle = cursor.getStringOrNull(StudentSyncContract.Columns.CURRENT_TITLE),
                avatarSummary = cursor.getStringOrNull(StudentSyncContract.Columns.AVATAR_SUMMARY),
                timelineSummary = cursor.getStringOrNull(StudentSyncContract.Columns.TIMELINE_SUMMARY),
                importedAtEpochMillis = cursor.getLongOrNull(StudentSyncContract.Columns.IMPORTED_AT_EPOCH_MILLIS)
            )
        }
    }

    suspend fun readProgress(): StudentProgressSnapshot? = withContext(Dispatchers.IO) {
        if (!canAccessProvider()) return@withContext null
        querySingle(
            uri = StudentSyncContract.PROGRESS_URI,
            columns = arrayOf(
                StudentSyncContract.Columns.XP,
                StudentSyncContract.Columns.LEVEL,
                StudentSyncContract.Columns.STREAK_DAYS,
                StudentSyncContract.Columns.LAST_VISIT_EPOCH_MILLIS,
                StudentSyncContract.Columns.UNLOCK_FLAGS_JSON,
                StudentSyncContract.Columns.ACHIEVEMENT_IDS_JSON
            )
        ) { cursor ->
            val unlockFlagsJson = cursor.getStringOrNull(StudentSyncContract.Columns.UNLOCK_FLAGS_JSON).orEmpty()
            val achievementIdsJson = cursor.getStringOrNull(StudentSyncContract.Columns.ACHIEVEMENT_IDS_JSON).orEmpty()
            StudentProgressSnapshot(
                xp = cursor.getIntOrNull(StudentSyncContract.Columns.XP) ?: 0,
                level = cursor.getIntOrNull(StudentSyncContract.Columns.LEVEL) ?: 1,
                streakDays = cursor.getIntOrNull(StudentSyncContract.Columns.STREAK_DAYS) ?: 0,
                lastVisitEpochMillis = cursor.getLongOrNull(StudentSyncContract.Columns.LAST_VISIT_EPOCH_MILLIS),
                unlockFlagsJson = unlockFlagsJson,
                achievementIdsJson = achievementIdsJson,
                achievements = decodeJsonStringArray(achievementIdsJson)
            )
        }
    }

    suspend fun readBehaviorSignals(): StudentBehaviorSignalsSnapshot? = withContext(Dispatchers.IO) {
        if (!canAccessProvider()) return@withContext null
        querySingle(
            uri = StudentSyncContract.BEHAVIOR_SIGNALS_URI,
            columns = arrayOf(
                StudentSyncContract.Columns.PHOTOS_THIS_WEEK,
                StudentSyncContract.Columns.QUESTS_COMPLETED_THIS_WEEK,
                StudentSyncContract.Columns.RETURNS_THIS_WEEK,
                StudentSyncContract.Columns.LAST_PHOTO_EPOCH_MILLIS,
                StudentSyncContract.Columns.LAST_QUEST_EPOCH_MILLIS,
                StudentSyncContract.Columns.LAST_VISIT_EPOCH_MILLIS,
                StudentSyncContract.Columns.WEEK_ANCHOR_EPOCH_MILLIS
            )
        ) { cursor ->
            StudentBehaviorSignalsSnapshot(
                photosThisWeek = cursor.getIntOrNull(StudentSyncContract.Columns.PHOTOS_THIS_WEEK) ?: 0,
                questsCompletedThisWeek = cursor.getIntOrNull(StudentSyncContract.Columns.QUESTS_COMPLETED_THIS_WEEK) ?: 0,
                returnsThisWeek = cursor.getIntOrNull(StudentSyncContract.Columns.RETURNS_THIS_WEEK) ?: 0,
                lastPhotoEpochMillis = cursor.getLongOrNull(StudentSyncContract.Columns.LAST_PHOTO_EPOCH_MILLIS),
                lastQuestEpochMillis = cursor.getLongOrNull(StudentSyncContract.Columns.LAST_QUEST_EPOCH_MILLIS),
                lastVisitEpochMillis = cursor.getLongOrNull(StudentSyncContract.Columns.LAST_VISIT_EPOCH_MILLIS),
                weekAnchorEpochMillis = cursor.getLongOrNull(StudentSyncContract.Columns.WEEK_ANCHOR_EPOCH_MILLIS)
            )
        }
    }

    suspend fun readStoryFragments(limit: Int = 12): List<StudentStoryFragmentSnapshot> = withContext(Dispatchers.IO) {
        if (!canAccessProvider()) return@withContext emptyList()
        queryList(
            uri = StudentSyncContract.STORY_FRAGMENTS_URI,
            columns = arrayOf(
                StudentSyncContract.Columns.ID,
                StudentSyncContract.Columns.NARRATIVE,
                StudentSyncContract.Columns.WORLD_EVENT_TYPE,
                StudentSyncContract.Columns.XP_AWARDED,
                StudentSyncContract.Columns.ROOM_UNLOCKED,
                StudentSyncContract.Columns.NPC_SPEAKER,
                StudentSyncContract.Columns.ENVIRONMENT_CHANGE,
                StudentSyncContract.Columns.EMOTIONAL_IMPACT,
                StudentSyncContract.Columns.TIMESTAMP_EPOCH_MILLIS
            )
        ) { cursor ->
            StudentStoryFragmentSnapshot(
                id = cursor.getStringOrNull(StudentSyncContract.Columns.ID).orEmpty(),
                narrative = cursor.getStringOrNull(StudentSyncContract.Columns.NARRATIVE),
                worldEventType = cursor.getStringOrNull(StudentSyncContract.Columns.WORLD_EVENT_TYPE).orEmpty(),
                xpAwarded = cursor.getIntOrNull(StudentSyncContract.Columns.XP_AWARDED) ?: 0,
                roomUnlocked = cursor.getStringOrNull(StudentSyncContract.Columns.ROOM_UNLOCKED),
                npcSpeaker = cursor.getStringOrNull(StudentSyncContract.Columns.NPC_SPEAKER),
                environmentChange = cursor.getStringOrNull(StudentSyncContract.Columns.ENVIRONMENT_CHANGE),
                emotionalImpact = cursor.getFloatOrNull(StudentSyncContract.Columns.EMOTIONAL_IMPACT) ?: 0f,
                timestampEpochMillis = cursor.getLongOrNull(StudentSyncContract.Columns.TIMESTAMP_EPOCH_MILLIS)
            )
        }.take(limit)
    }

    private inline fun <T> querySingle(
        uri: android.net.Uri,
        columns: Array<String>,
        crossinline map: (Cursor) -> T
    ): T? {
        return runCatching {
            context.contentResolver.query(
                uri,
                columns,
                null,
                null,
                null
            )?.use { cursor ->
                if (!cursor.moveToFirst()) return null
                map(cursor)
            }
        }.getOrNull()
    }

    private inline fun <T> queryList(
        uri: android.net.Uri,
        columns: Array<String>,
        crossinline map: (Cursor) -> T
    ): List<T> {
        return runCatching {
            context.contentResolver.query(
                uri,
                columns,
                null,
                null,
                null
            )?.use { cursor ->
                val out = mutableListOf<T>()
                while (cursor.moveToNext()) {
                    out += map(cursor)
                }
                out
            } ?: emptyList()
        }.getOrDefault(emptyList())
    }

    private fun decodeJsonStringArray(raw: String): List<String> {
        if (raw.isBlank()) return emptyList()
        return runCatching {
            val array = JSONArray(raw)
            (0 until array.length()).mapNotNull { idx -> array.optString(idx).takeIf { it.isNotBlank() } }
        }.getOrDefault(emptyList())
    }

    private fun Cursor.getStringOrNull(column: String): String? {
        val index = getColumnIndex(column)
        return if (index < 0 || isNull(index)) null else getString(index)
    }

    private fun Cursor.getIntOrNull(column: String): Int? {
        val index = getColumnIndex(column)
        return if (index < 0 || isNull(index)) null else getInt(index)
    }

    private fun Cursor.getLongOrNull(column: String): Long? {
        val index = getColumnIndex(column)
        return if (index < 0 || isNull(index)) null else getLong(index)
    }

    private fun Cursor.getFloatOrNull(column: String): Float? {
        val index = getColumnIndex(column)
        return if (index < 0 || isNull(index)) null else getFloat(index)
    }
}

data class StudentProfileSnapshot(
    val uid: String,
    val forgeName: String,
    val currentStage: String?,
    val currentTitle: String?,
    val avatarSummary: String?,
    val timelineSummary: String?,
    val importedAtEpochMillis: Long?
)

data class StudentProgressSnapshot(
    val xp: Int,
    val level: Int,
    val streakDays: Int,
    val lastVisitEpochMillis: Long?,
    val unlockFlagsJson: String,
    val achievementIdsJson: String,
    val achievements: List<String>
)

data class StudentBehaviorSignalsSnapshot(
    val photosThisWeek: Int,
    val questsCompletedThisWeek: Int,
    val returnsThisWeek: Int,
    val lastPhotoEpochMillis: Long?,
    val lastQuestEpochMillis: Long?,
    val lastVisitEpochMillis: Long?,
    val weekAnchorEpochMillis: Long?
)

data class StudentStoryFragmentSnapshot(
    val id: String,
    val narrative: String?,
    val worldEventType: String,
    val xpAwarded: Int,
    val roomUnlocked: String?,
    val npcSpeaker: String?,
    val environmentChange: String?,
    val emotionalImpact: Float,
    val timestampEpochMillis: Long?
)

