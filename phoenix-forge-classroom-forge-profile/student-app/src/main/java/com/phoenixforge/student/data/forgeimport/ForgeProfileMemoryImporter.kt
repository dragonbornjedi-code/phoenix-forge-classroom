package com.phoenixforge.student.data.forgeimport

import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import com.phoenixforge.student.domain.model.LifeChapter
import com.phoenixforge.student.domain.model.LifeEvent
import com.phoenixforge.student.domain.model.LifeEventType
import com.phoenixforge.student.domain.model.MemoryArtifact
import com.phoenixforge.student.domain.model.MemorySource
import com.phoenixforge.student.domain.model.PhotoTag
import com.phoenixforge.student.domain.repository.StudentRepository
import com.phoenixforge.student.domain.vault.MemoryVault
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import java.io.File
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class ForgeProfileMemoryRow(
    val id: String,
    val type: String,
    val category: String,
    val capturedAtEpochMillis: Long,
    val note: String?,
    val syncedToStudent: Boolean,
    val contentUri: String,
)

data class ForgeProfileMemoryImportResult(
    val imported: Int,
    val skipped: Int,
    val message: String,
)

/**
 * Pulls school-category photos from Forge Profile into Student Memory Vault.
 * Offline on-device — same phone can host both APKs (master step 0.65 cross-app).
 */
@Singleton
class ForgeProfileMemoryImporter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: StudentRepository,
    private val memoryVault: MemoryVault,
) {
    fun canAccessProvider(): Boolean =
        context.checkSelfPermission(ForgeProfileContract.READ_PERMISSION) == PackageManager.PERMISSION_GRANTED

    suspend fun importSchoolMemories(): ForgeProfileMemoryImportResult {
        if (!canAccessProvider()) {
            return ForgeProfileMemoryImportResult(
                imported = 0,
                skipped = 0,
                message = "Install Forge Profile and grant read permission first.",
            )
        }
        val rows = readSchoolMemories()
        if (rows.isEmpty()) {
            return ForgeProfileMemoryImportResult(
                imported = 0,
                skipped = 0,
                message = "No school photos in Forge Profile yet.",
            )
        }
        val existing = repository.observeMemories().first()
        val existingNotes = existing.mapNotNull { it.note }.toSet()
        var imported = 0
        var skipped = 0
        val chapter = memoryVault.chapterForProgress(repository.observeProgress().first())

        rows.forEach { row ->
            val marker = profileNoteMarker(row.id)
            if (existingNotes.any { it.contains(marker) }) {
                skipped++
                return@forEach
            }
            val localUri = copyToVault(row) ?: run {
                skipped++
                return@forEach
            }
            repository.saveMemory(
                MemoryArtifact(
                    id = UUID.randomUUID().toString(),
                    mediaUri = localUri,
                    tag = PhotoTag.SCHOOL,
                    chapter = chapter,
                    capturedAtEpochMillis = row.capturedAtEpochMillis,
                    note = marker + (row.note?.let { " · $it" } ?: ""),
                    isSealed = false,
                    sealedUntilEpochMillis = null,
                    source = MemorySource.FORGE_PROFILE_IMPORT,
                )
            )
            imported++
        }

        if (imported > 0) {
            repository.recordLifeEvent(
                LifeEvent(
                    type = LifeEventType.PHOTO_IMPORTED,
                    payload = "forge_profile_school=$imported",
                )
            )
        }

        return ForgeProfileMemoryImportResult(
            imported = imported,
            skipped = skipped,
            message = when {
                imported > 0 -> "Imported $imported school photo(s) into Memory Vault."
                skipped > 0 -> "All school photos were already in the vault."
                else -> "Nothing to import."
            },
        )
    }

    private fun readSchoolMemories(): List<ForgeProfileMemoryRow> {
        val uri = Uri.parse("content://${ForgeProfileContract.AUTHORITY}/memories")
        val projection = arrayOf(
            ForgeProfileContract.Columns.MEMORY_ID,
            ForgeProfileContract.Columns.MEMORY_TYPE,
            ForgeProfileContract.Columns.MEMORY_CATEGORY,
            ForgeProfileContract.Columns.MEMORY_CAPTURED_AT,
            ForgeProfileContract.Columns.MEMORY_NOTE,
            ForgeProfileContract.Columns.MEMORY_SYNCED_TO_STUDENT,
            ForgeProfileContract.Columns.MEMORY_CONTENT_URI,
        )
        val rows = mutableListOf<ForgeProfileMemoryRow>()
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            while (cursor.moveToNext()) {
                val category = cursor.getStringOrNull(ForgeProfileContract.Columns.MEMORY_CATEGORY)
                val type = cursor.getStringOrNull(ForgeProfileContract.Columns.MEMORY_TYPE)
                if (category != "SCHOOL" || type != "PHOTO") continue
                rows += ForgeProfileMemoryRow(
                    id = cursor.getStringOrNull(ForgeProfileContract.Columns.MEMORY_ID).orEmpty(),
                    type = type,
                    category = category,
                    capturedAtEpochMillis = cursor.getLongOrNull(ForgeProfileContract.Columns.MEMORY_CAPTURED_AT)
                        ?: System.currentTimeMillis(),
                    note = cursor.getStringOrNull(ForgeProfileContract.Columns.MEMORY_NOTE),
                    syncedToStudent = (cursor.getIntOrNull(ForgeProfileContract.Columns.MEMORY_SYNCED_TO_STUDENT) ?: 0) == 1,
                    contentUri = cursor.getStringOrNull(ForgeProfileContract.Columns.MEMORY_CONTENT_URI).orEmpty(),
                )
            }
        }
        return rows.filter { it.id.isNotBlank() && it.contentUri.isNotBlank() }
    }

    private fun copyToVault(row: ForgeProfileMemoryRow): String? = runCatching {
        val dir = File(context.filesDir, "vault_imports").apply { mkdirs() }
        val dest = File(dir, "${row.id}.jpg")
        context.contentResolver.openInputStream(Uri.parse(row.contentUri))?.use { input ->
            dest.outputStream().use { output -> input.copyTo(output) }
        } ?: return null
        Uri.fromFile(dest).toString()
    }.getOrNull()

    private fun profileNoteMarker(profileArtifactId: String): String = "from_profile:$profileArtifactId"

    private fun Cursor.getStringOrNull(column: String): String? {
        val idx = getColumnIndex(column)
        return if (idx >= 0 && !isNull(idx)) getString(idx) else null
    }

    private fun Cursor.getIntOrNull(column: String): Int? {
        val idx = getColumnIndex(column)
        return if (idx >= 0 && !isNull(idx)) getInt(idx) else null
    }

    private fun Cursor.getLongOrNull(column: String): Long? {
        val idx = getColumnIndex(column)
        return if (idx >= 0 && !isNull(idx)) getLong(idx) else null
    }
}
