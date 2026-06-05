package com.phoenixforge.student.sync

import android.content.ContentProvider
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.content.UriMatcher
import androidx.room.Room
import com.phoenixforge.student.data.local.StudentDatabase
import com.phoenixforge.student.data.local.dao.StudentDao
import com.phoenixforge.student.data.local.entity.BehaviorSignalsEntity
import com.phoenixforge.student.data.local.entity.ImportedProfileSnapshotEntity
import com.phoenixforge.student.data.local.entity.LifeEventEntity
import com.phoenixforge.student.data.local.entity.StoryFragmentEntity
import com.phoenixforge.student.data.local.entity.StudentProgressEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class StudentSyncContentProvider : ContentProvider() {

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(StudentSyncContract.AUTHORITY, StudentSyncContract.PATH_PROFILE_SNAPSHOT, MATCH_PROFILE_SNAPSHOT)
        addURI(StudentSyncContract.AUTHORITY, StudentSyncContract.PATH_PROGRESS, MATCH_PROGRESS)
        addURI(StudentSyncContract.AUTHORITY, StudentSyncContract.PATH_BEHAVIOR_SIGNALS, MATCH_BEHAVIOR_SIGNALS)
        addURI(StudentSyncContract.AUTHORITY, StudentSyncContract.PATH_STORY_FRAGMENTS, MATCH_STORY_FRAGMENTS)
        addURI(StudentSyncContract.AUTHORITY, StudentSyncContract.PATH_LIFE_EVENTS, MATCH_LIFE_EVENTS)
    }

    private var dao: StudentDao? = null

    override fun onCreate(): Boolean {
        val context = context ?: return false
        val db = Room.databaseBuilder(context, StudentDatabase::class.java, "student_house.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = db.studentDao()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        enforceReadPermission()

        val dao = dao ?: return null
        val match = uriMatcher.match(uri)

        return when (match) {
            MATCH_PROFILE_SNAPSHOT -> runBlocking { profileSnapshotCursor(dao, projection) }
            MATCH_PROGRESS -> runBlocking { progressCursor(dao, projection) }
            MATCH_BEHAVIOR_SIGNALS -> runBlocking { behaviorSignalsCursor(dao, projection) }
            MATCH_STORY_FRAGMENTS -> runBlocking { storyFragmentsCursor(dao, projection) }
            MATCH_LIFE_EVENTS -> runBlocking { lifeEventsCursor(dao, projection) }
            else -> null
        }
    }

    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: android.content.ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(
        uri: Uri,
        values: android.content.ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    private fun enforceReadPermission() {
        val ctx = context ?: return
        if (ctx.checkCallingOrSelfPermission(StudentSyncContract.READ_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            throw SecurityException("Unauthorized access to Student sync provider")
        }
    }

    private suspend fun profileSnapshotCursor(dao: StudentDao, projection: Array<out String>?): Cursor {
        val cols = resolveProjection(
            projection,
            defaultColumns = arrayOf(
                StudentSyncContract.Columns.UID,
                StudentSyncContract.Columns.FORGE_NAME,
                StudentSyncContract.Columns.CURRENT_STAGE,
                StudentSyncContract.Columns.CURRENT_TITLE,
                StudentSyncContract.Columns.AVATAR_SUMMARY,
                StudentSyncContract.Columns.TIMELINE_SUMMARY,
                StudentSyncContract.Columns.IMPORTED_AT_EPOCH_MILLIS
            )
        )
        val latest = dao.observeImportedProfiles().first().firstOrNull()
        return MatrixCursor(cols).apply {
            val row = latest?.toMap() ?: emptyMap()
            if (latest != null) addRow(cols.map { row[it] }.toTypedArray())
        }
    }

    private suspend fun progressCursor(dao: StudentDao, projection: Array<out String>?): Cursor {
        val cols = resolveProjection(
            projection,
            defaultColumns = arrayOf(
                StudentSyncContract.Columns.XP,
                StudentSyncContract.Columns.LEVEL,
                StudentSyncContract.Columns.STREAK_DAYS,
                StudentSyncContract.Columns.LAST_VISIT_EPOCH_MILLIS,
                StudentSyncContract.Columns.UNLOCK_FLAGS_JSON,
                StudentSyncContract.Columns.ACHIEVEMENT_IDS_JSON
            )
        )
        val progress = dao.getProgress()
        return MatrixCursor(cols).apply {
            val row = progress?.toMap() ?: emptyMap()
            if (progress != null) addRow(cols.map { row[it] }.toTypedArray())
        }
    }

    private suspend fun behaviorSignalsCursor(dao: StudentDao, projection: Array<out String>?): Cursor {
        val cols = resolveProjection(
            projection,
            defaultColumns = arrayOf(
                StudentSyncContract.Columns.PHOTOS_THIS_WEEK,
                StudentSyncContract.Columns.QUESTS_COMPLETED_THIS_WEEK,
                StudentSyncContract.Columns.RETURNS_THIS_WEEK,
                StudentSyncContract.Columns.LAST_PHOTO_EPOCH_MILLIS,
                StudentSyncContract.Columns.LAST_QUEST_EPOCH_MILLIS,
                StudentSyncContract.Columns.LAST_VISIT_EPOCH_MILLIS,
                StudentSyncContract.Columns.WEEK_ANCHOR_EPOCH_MILLIS
            )
        )
        val signals = dao.observeBehaviorSignals().first()
        return MatrixCursor(cols).apply {
            val row = signals?.toMap() ?: emptyMap()
            if (signals != null) addRow(cols.map { row[it] }.toTypedArray())
        }
    }

    private suspend fun storyFragmentsCursor(dao: StudentDao, projection: Array<out String>?): Cursor {
        val cols = resolveProjection(
            projection,
            defaultColumns = arrayOf(
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
        )

        val fragments = dao.observeStoryFragments(limit = STORY_FRAGMENT_LIMIT).first()
        return MatrixCursor(cols).apply {
            fragments.forEach { fragment ->
                val row = fragment.toMap()
                addRow(cols.map { row[it] }.toTypedArray())
            }
        }
    }

    private suspend fun lifeEventsCursor(dao: StudentDao, projection: Array<out String>?): Cursor {
        val cols = resolveProjection(
            projection,
            defaultColumns = arrayOf(
                StudentSyncContract.Columns.ID,
                StudentSyncContract.Columns.TYPE,
                StudentSyncContract.Columns.PAYLOAD,
                StudentSyncContract.Columns.TIMESTAMP_EPOCH_MILLIS
            )
        )

        val events = dao.observeLifeEvents(limit = LIFE_EVENT_LIMIT).first()
        return MatrixCursor(cols).apply {
            events.forEach { event ->
                val row = event.toMap()
                addRow(cols.map { row[it] }.toTypedArray())
            }
        }
    }

    private fun resolveProjection(
        requested: Array<out String>?,
        defaultColumns: Array<String>
    ): Array<String> {
        if (requested.isNullOrEmpty()) return defaultColumns
        val filtered = requested.filter { it.isNotBlank() }
        return if (filtered.isEmpty()) defaultColumns else filtered.toTypedArray()
    }

    private fun StudentProgressEntity.toMap(): Map<String, Any?> = mapOf(
        StudentSyncContract.Columns.XP to xp,
        StudentSyncContract.Columns.LEVEL to level,
        StudentSyncContract.Columns.STREAK_DAYS to streakDays,
        StudentSyncContract.Columns.LAST_VISIT_EPOCH_MILLIS to lastVisitEpochMillis,
        StudentSyncContract.Columns.UNLOCK_FLAGS_JSON to unlockFlagsJson,
        StudentSyncContract.Columns.ACHIEVEMENT_IDS_JSON to achievementIdsJson
    )

    private fun BehaviorSignalsEntity.toMap(): Map<String, Any?> = mapOf(
        StudentSyncContract.Columns.PHOTOS_THIS_WEEK to photosThisWeek,
        StudentSyncContract.Columns.QUESTS_COMPLETED_THIS_WEEK to questsCompletedThisWeek,
        StudentSyncContract.Columns.RETURNS_THIS_WEEK to returnsThisWeek,
        StudentSyncContract.Columns.LAST_PHOTO_EPOCH_MILLIS to lastPhotoEpochMillis,
        StudentSyncContract.Columns.LAST_QUEST_EPOCH_MILLIS to lastQuestEpochMillis,
        StudentSyncContract.Columns.LAST_VISIT_EPOCH_MILLIS to lastVisitEpochMillis,
        StudentSyncContract.Columns.WEEK_ANCHOR_EPOCH_MILLIS to weekAnchorEpochMillis
    )

    private fun ImportedProfileSnapshotEntity.toMap(): Map<String, Any?> = mapOf(
        StudentSyncContract.Columns.UID to uid,
        StudentSyncContract.Columns.FORGE_NAME to forgeName,
        StudentSyncContract.Columns.CURRENT_STAGE to currentStage,
        StudentSyncContract.Columns.CURRENT_TITLE to currentTitle,
        StudentSyncContract.Columns.AVATAR_SUMMARY to avatarSummary,
        StudentSyncContract.Columns.TIMELINE_SUMMARY to timelineSummary,
        StudentSyncContract.Columns.IMPORTED_AT_EPOCH_MILLIS to importedAtEpochMillis
    )

    private fun StoryFragmentEntity.toMap(): Map<String, Any?> = mapOf(
        StudentSyncContract.Columns.ID to id,
        StudentSyncContract.Columns.NARRATIVE to narrative,
        StudentSyncContract.Columns.WORLD_EVENT_TYPE to worldEventType,
        StudentSyncContract.Columns.XP_AWARDED to xpAwarded,
        StudentSyncContract.Columns.ROOM_UNLOCKED to roomUnlocked,
        StudentSyncContract.Columns.NPC_SPEAKER to npcSpeaker,
        StudentSyncContract.Columns.ENVIRONMENT_CHANGE to environmentChange,
        StudentSyncContract.Columns.EMOTIONAL_IMPACT to emotionalImpact,
        StudentSyncContract.Columns.TIMESTAMP_EPOCH_MILLIS to timestampEpochMillis
    )

    private fun LifeEventEntity.toMap(): Map<String, Any?> = mapOf(
        StudentSyncContract.Columns.ID to id,
        StudentSyncContract.Columns.TYPE to type,
        StudentSyncContract.Columns.PAYLOAD to payload,
        StudentSyncContract.Columns.TIMESTAMP_EPOCH_MILLIS to timestampEpochMillis
    )

    private companion object {
        const val MATCH_PROFILE_SNAPSHOT = 1
        const val MATCH_PROGRESS = 2
        const val MATCH_BEHAVIOR_SIGNALS = 3
        const val MATCH_STORY_FRAGMENTS = 4
        const val MATCH_LIFE_EVENTS = 5

        private const val STORY_FRAGMENT_LIMIT = 12
        private const val LIFE_EVENT_LIMIT = 12
    }
}

