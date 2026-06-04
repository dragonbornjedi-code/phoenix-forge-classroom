package com.phoenixforge.profile.data.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.pm.PackageManager
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.runBlocking

/**
 * Exposes DTO-safe projections only. All reads go through [ProfileExportReader], not Room.
 */
class ProfileContentProvider : ContentProvider() {

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(ProfileContract.AUTHORITY, ProfileContract.PATH_PROFILE, MATCH_PROFILE)
        addURI(ProfileContract.AUTHORITY, ProfileContract.PATH_AVATAR, MATCH_AVATAR)
        addURI(ProfileContract.AUTHORITY, ProfileContract.PATH_TIMELINE, MATCH_TIMELINE)
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
            else -> null
        }
    }

    override fun getType(uri: Uri): String? = when (uriMatcher.match(uri)) {
        MATCH_PROFILE -> ProfileContract.MIME_PROFILE
        MATCH_AVATAR -> ProfileContract.MIME_AVATAR
        MATCH_TIMELINE -> ProfileContract.MIME_TIMELINE
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
        ProfileContract.Columns.CURRENT_TITLE to dto.currentTitle
    )

    private fun avatarRow(dto: AvatarExportDto): Map<String, Any?> = mapOf(
        ProfileContract.Columns.AVATAR_HAIR to dto.hairType,
        ProfileContract.Columns.AVATAR_EYES to dto.eyeColor,
        ProfileContract.Columns.AVATAR_SKIN to dto.skinTone,
        ProfileContract.Columns.AVATAR_CLOTHING to dto.clothingId,
        ProfileContract.Columns.AVATAR_VERSION to dto.version
    )

    private fun timelineRow(dto: TimelineEventExportDto): Map<String, Any?> = mapOf(
        ProfileContract.Columns.EVENT_TITLE to dto.title,
        ProfileContract.Columns.EVENT_TYPE to dto.type,
        ProfileContract.Columns.EVENT_TIMESTAMP to dto.timestampEpochMillis
    )

    private companion object {
        const val MATCH_PROFILE = 1
        const val MATCH_AVATAR = 2
        const val MATCH_TIMELINE = 3
    }
}
