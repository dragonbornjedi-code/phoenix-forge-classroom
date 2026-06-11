package com.phoenixforge.student.sync

import android.content.Context
import android.util.Log
import com.phoenixforge.student.domain.session.StudentSessionStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Reads LESSON manifests from the Syncthing sync tree (step 1.03).
 * Student never reads PROTECTED scope — manifest is LESSON-only pass-through.
 */
@Singleton
class ManifestReader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sessionStore: StudentSessionStore,
) {
    private val isoDate = DateTimeFormatter.ISO_LOCAL_DATE
    private val json = Json { ignoreUnknownKeys = true }

    fun readActiveManifest(today: LocalDate = LocalDate.now()): ManifestReadResult {
        val studentUid = sessionStore.getActiveImportUid()?.trim().orEmpty()
        if (studentUid.isEmpty()) return ManifestReadResult.NotSignedIn

        val dateIso = isoDate.format(today)
        val relative = ManifestSyncPaths.manifestRelativePath(studentUid, dateIso)
        val searched = candidateFiles(relative)

        for (file in searched) {
            if (!file.exists()) continue
            return parseManifestFile(file, studentUid, dateIso)
        }

        return ManifestReadResult.NotFound(
            studentUid = studentUid,
            searchedPaths = searched.map { it.absolutePath },
        )
    }

    private fun parseManifestFile(
        file: File,
        studentUid: String,
        dateIso: String,
    ): ManifestReadResult {
        return runCatching {
            val manifest = json.decodeFromString(LessonManifest.serializer(), file.readText())
            if (manifest.studentUid.isNotBlank() && manifest.studentUid != studentUid) {
                return ManifestReadResult.Invalid(
                    reason = "Manifest is for a different profile.",
                    sourcePath = file.absolutePath,
                )
            }
            val today = LocalDate.parse(dateIso)
            if (!manifest.isValidForDate(today)) {
                return ManifestReadResult.Invalid(
                    reason = "Manifest is not valid for today ($dateIso).",
                    sourcePath = file.absolutePath,
                )
            }
            Log.i(TAG, "loaded manifest for $studentUid from ${file.absolutePath}")
            ManifestReadResult.Ready(
                manifest = manifest,
                items = manifest.todayItems(today),
                sourcePath = file.absolutePath,
            )
        }.getOrElse { error ->
            ManifestReadResult.Invalid(
                reason = error.message ?: "Could not parse manifest.",
                sourcePath = file.absolutePath,
            )
        }
    }

    private fun candidateFiles(relativePath: String): List<File> {
        val out = mutableListOf<File>()
        PublicSyncWriter.candidateRoots().forEach { root ->
            out += File(root, relativePath)
        }
        context.getExternalFilesDir(null)?.let { base ->
            out += File(base, relativePath)
        }
        return out
    }

    private companion object {
        const val TAG = "ManifestReader"
    }
}
