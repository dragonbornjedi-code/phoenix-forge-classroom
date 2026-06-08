package com.phoenixforge.profile.data.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.pm.PackageManager
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.runBlocking
import java.io.File

/**
 * Exposes DTO-safe projections only. All reads go through [ProfileExportReader], not Room.
 */
class ProfileContentProvider : ContentProvider() {

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(ProfileContract.AUTHORITY, ProfileContract.PATH_PROFILE, MATCH_PROFILE)
        addURI(ProfileContract.AUTHORITY, ProfileContract.PATH_AVATAR, MATCH_AVATAR)
        addURI(ProfileContract.AUTHORITY, ProfileContract.PATH_TIMELINE, MATCH_TIMELINE)
        addURI(ProfileContract.AUTHORITY, ProfileContract.PATH_MEMORIES, MATCH_MEMORIES)
        addURI(ProfileContract.AUTHORITY, "${ProfileContract.PATH_MEMORY_FILE}/*", MATCH_MEMORY_FILE)
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        enforceReadPermission()

        val context = context ?: return null
        val reader = EntryPointAccessors.fromApplication(
            context.applicationContext,
            ProfileProviderEntryPoint::class.java
        ).profileExportReader()

        return when (uriMatcher.match(uri)) {
            MATCH_PROFILE -> runBlocking {
                val dto = reader.readProfile() ?: return@runBlocking null
                val columns = resolveProjection(
                    projection,
                    ProfileContract.ProfileProjection.COLUMNS
                )
                matrixFromColumns(columns) {
                    addMappedRow(columns, profileRow(dto))
                }
            }
            MATCH_AVATAR -> runBlocking {
                val dto = reader.readLatestAvatar() ?: return@runBlocking null
                val columns = resolveProjection(
                    projection,
                    ProfileContract.AvatarProjection.COLUMNS
                )
                matrixFromColumns(columns) {
                    addMappedRow(columns, avatarRow(dto))
                }
            }
            MATCH_TIMELINE -> runBlocking {
                val events = reader.readTimeline()
                val columns = resolveProjection(
                    projection,
                    ProfileContract.TimelineProjection.COLUMNS
                )
                matrixFromColumns(columns) {
                    events.forEach { dto -> addMappedRow(columns, timelineRow(dto)) }
                }
            }
            MATCH_MEMORIES -> runBlocking {
                val memories = reader.readMemories()
                val columns = resolveProjection(
                    projection,
                    ProfileContract.MemoriesProjection.COLUMNS
                )
                matrixFromColumns(columns) {
                    memories.forEach { dto -> addMappedRow(columns, memoryRow(dto)) }
                }
            }
            else -> null
        }
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        enforceReadPermission()
        if (uriMatcher.match(uri) != MATCH_MEMORY_FILE) return null
        val artifactId = uri.lastPathSegment ?: return null
        val ctx = context ?: return null
        val memoriesDir = File(ctx.filesDir, "memories")
        val photo = File(memoriesDir, "$artifactId.jpg")
        val audio = File(memoriesDir, "$artifactId.m4a")
        val file = when {
            photo.exists() -> photo
            audio.exists() -> audio
            else -> return null
        }
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    }

    override fun getType(uri: Uri): String? = when (uriMatcher.match(uri)) {
        MATCH_PROFILE -> ProfileContract.MIME_PROFILE
        MATCH_AVATAR -> ProfileContract.MIME_AVATAR
        MATCH_TIMELINE -> ProfileContract.MIME_TIMELINE
        MATCH_MEMORIES -> ProfileContract.MIME_MEMORIES
        MATCH_MEMORY_FILE -> ProfileContract.MIME_MEMORY_FILE
        else -> null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0

    private fun enforceReadPermission() {
        if (context?.checkCallingOrSelfPermission(ProfileContract.READ_PERMISSION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            throw SecurityException("Unauthorized access to Forge Profile")
        }
    }

    private fun resolveProjection(requested: Array<out String>?, defaults: Array<String>): Array<String> =
        if (requested.isNullOrEmpty()) defaults else requested.filter { it in defaults }.toTypedArray().ifEmpty { defaults }

    private fun matrixFromColumns(columns: Array<String>, block: MatrixCursor.() -> Unit): MatrixCursor =
        MatrixCursor(columns).apply(block)

    private fun MatrixCursor.addMappedRow(columns: Array<String>, values: Map<String, Any?>) {
        addRow(columns.map { col -> values[col] }.toTypedArray())
    }

    private fun profileRow(dto: ProfileExportDto): Map<String, Any?> = mapOf(
        ProfileContract.Columns.UID to dto.uid,
        ProfileContract.Columns.FORGE_NAME to dto.forgeName,
        ProfileContract.Columns.CURRENT_STAGE to dto.currentStage,
        ProfileContract.Columns.CURRENT_TITLE to dto.currentTitle,
        ProfileContract.Columns.AGE_YEARS to dto.ageYears,
        ProfileContract.Columns.PROFILE_ROLE to dto.profileRole
    )

    private fun avatarRow(dto: AvatarExportDto): Map<String, Any?> = mapOf(
        ProfileContract.Columns.AVATAR_HAIR to dto.hairType,
        ProfileContract.Columns.AVATAR_EYES to dto.eyeColor,
        ProfileContract.Columns.AVATAR_SKIN to dto.skinTone,
        ProfileContract.Columns.AVATAR_CLOTHING to dto.clothingId,
        ProfileContract.Columns.AVATAR_VERSION to dto.version,
        ProfileContract.Columns.AVATAR_SHARD_LEVEL to dto.shardLevel,
        ProfileContract.Columns.HERO_STYLE to dto.heroStyle,
        ProfileContract.Columns.HERO_COLOR to dto.heroColor,
        ProfileContract.Columns.GODOT_MODEL_PATH to dto.godotModelPath,
        ProfileContract.Columns.AVATAR_SUMMARY to dto.avatarSummary,
        ProfileContract.Columns.AVATAR_CONFIG_JSON to dto.avatarConfigJson,
    )

    private fun timelineRow(dto: TimelineEventExportDto): Map<String, Any?> = mapOf(
        ProfileContract.Columns.EVENT_TITLE to dto.title,
        ProfileContract.Columns.EVENT_TYPE to dto.type,
        ProfileContract.Columns.EVENT_TIMESTAMP to dto.timestampEpochMillis
    )

    private fun memoryRow(dto: MemoryExportDto): Map<String, Any?> = mapOf(
        ProfileContract.Columns.MEMORY_ID to dto.id,
        ProfileContract.Columns.MEMORY_TYPE to dto.type,
        ProfileContract.Columns.MEMORY_CATEGORY to dto.category,
        ProfileContract.Columns.MEMORY_SOURCE to dto.source,
        ProfileContract.Columns.MEMORY_CAPTURED_AT to dto.capturedAtEpochMillis,
        ProfileContract.Columns.MEMORY_NOTE to dto.note,
        ProfileContract.Columns.MEMORY_CHECKSUM to dto.checksum,
        ProfileContract.Columns.MEMORY_SYNCED_TO_STUDENT to if (dto.syncedToStudent) 1 else 0,
        ProfileContract.Columns.MEMORY_CONTENT_URI to dto.contentUri,
    )

    private companion object {
        const val MATCH_PROFILE = 1
        const val MATCH_AVATAR = 2
        const val MATCH_TIMELINE = 3
        const val MATCH_MEMORIES = 4
        const val MATCH_MEMORY_FILE = 5
    }
}
