package com.phoenixforge.classroom.teacher.domain.manifest

import android.content.Context
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.TileStatus
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

data class ManifestWriteResult(
    val manifest: LessonManifest,
    val writtenPaths: List<String>,
    val message: String,
)

/**
 * Builds and writes LESSON-scope daily manifests for Student Edition receive (step 1.02).
 */
object ManifestWriter {
    private val isoDate = DateTimeFormatter.ISO_LOCAL_DATE
    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    fun filterStackTiles(tiles: List<IntentTile>): List<IntentTile> =
        tiles
            .filter { tile ->
                when (TileStatus.fromName(tile.status)) {
                    TileStatus.PLANNED, TileStatus.ACTIVE, TileStatus.SENT -> true
                    TileStatus.COMPLETED, TileStatus.DEFERRED -> false
                }
            }
            .filter { tile ->
                val kind = tile.routineKind.trim()
                kind != "morning_routine" && kind != "night_routine"
            }
            .sortedBy { it.sortOrder }

    fun build(
        tiles: List<IntentTile>,
        studentUid: String,
        deviceId: String,
        today: LocalDate = LocalDate.now(),
    ): LessonManifest {
        val dateIso = isoDate.format(today)
        val stack = filterStackTiles(tiles)
        val createdEpochMs = System.currentTimeMillis()

        val days = if (stack.isEmpty()) {
            listOf(
                LessonManifestDay(
                    dayIndex = 0,
                    date = dateIso,
                    narrativeTitle = "Today's expedition",
                    narrativeText = "Your steward has not queued any tiles yet.",
                    quests = emptyList(),
                ),
            )
        } else {
            stack.mapIndexed { index, tile ->
                LessonManifestDay(
                    dayIndex = index,
                    date = dateIso,
                    narrativeTitle = tile.title,
                    narrativeText = tile.studentMission.ifBlank { tile.description },
                    routineKind = tile.routineKind.ifBlank { "daily_quest" },
                    quests = listOf(stubQuestId(tile)),
                )
            }
        }

        return LessonManifest(
            manifestId = "manifest_${studentUid}_${dateIso}",
            studentUid = studentUid.trim(),
            createdByDeviceId = deviceId,
            createdEpochMs = createdEpochMs,
            validFromDate = dateIso,
            validToDate = dateIso,
            days = days,
        )
    }

    fun encode(manifest: LessonManifest): String = json.encodeToString(manifest)

    fun writeToDisk(context: Context, manifest: LessonManifest): ManifestWriteResult {
        val payload = encode(manifest)
        val fileName = ManifestSyncPaths.manifestFileName(manifest.validFromDate)
        val relative = ManifestSyncPaths.manifestRelativePath(manifest.studentUid, manifest.validFromDate)

        val written = mutableListOf<String>()
        written += writePublicSync(relative, payload)
        writeAppExternalSync(context, relative, payload)?.let { written += it }

        val publicWritten = written.any { path ->
            !path.contains("/Android/data/")
        }
        val message = when {
            written.isEmpty() ->
                "Could not write manifest. Grant storage access or use adb push (see docs/HANDOFF_1_02.md)."
            !publicWritten ->
                "Wrote $fileName (app-private only). Student Edition cannot read this — " +
                    "grant Files access or push to /sdcard/PhoenixForge/sync/profiles/…"
            else ->
                "Wrote $fileName for ${manifest.studentUid.take(8)}… → ${written.joinToString(", ")}"
        }

        return ManifestWriteResult(
            manifest = manifest,
            writtenPaths = written,
            message = message,
        )
    }

    fun stubQuestId(tile: IntentTile): String =
        tile.starterLessonId?.trim()?.takeIf { it.isNotEmpty() }
            ?: "stub_tile_${tile.id.replace("-", "").take(12)}"

    private fun writePublicSync(relativePath: String, payload: String): List<String> =
        PublicSyncWriter.writeRelative(relativePath, payload)

    private fun writeAppExternalSync(context: Context, relativePath: String, payload: String): String? =
        runCatching {
            val file = File(context.getExternalFilesDir(null), "PhoenixForge/sync/profiles/$relativePath")
            file.parentFile?.mkdirs()
            file.writeText(payload)
            file.absolutePath
        }.getOrNull()
}

object TeacherDeviceIdStore {
    private const val PREFS = "teacher_device"
    private const val KEY_DEVICE_ID = "device_id"

    fun getOrCreate(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.getString(KEY_DEVICE_ID, null)?.takeIf { it.isNotBlank() }?.let { return it }
        val created = "teacher-${UUID.randomUUID()}"
        prefs.edit().putString(KEY_DEVICE_ID, created).apply()
        return created
    }
}
