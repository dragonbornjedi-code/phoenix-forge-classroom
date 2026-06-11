package com.phoenixforge.profile.data.export

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.core.content.FileProvider
import com.phoenixforge.profile.domain.avatar.AvatarConfigV2
import com.phoenixforge.profile.domain.avatar.AvatarHeroCatalog
import com.phoenixforge.profile.domain.avatar.ForgeProfilePushBundle
import com.phoenixforge.profile.domain.avatar.ForgeProfilePushDto
import com.phoenixforge.profile.domain.copy.AppBoundaryCopy
import com.phoenixforge.profile.domain.model.Avatar
import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.ProfileRole
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Parent-triggered export only — no background sync.
 * Writes JSON to PhoenixForge/export and returns a share intent for manual handoff.
 */
@Singleton
class ProfileManualPushExporter @Inject constructor() {

    data class PushResult(
        val writtenPaths: List<String>,
        val shareIntent: Intent,
        val message: String,
    )

    fun export(
        context: Context,
        profile: ForgeProfile,
        avatar: Avatar?,
    ): PushResult {
        val avatarConfig = avatar?.let { AvatarHeroCatalog.toConfig(profile.uid, profile.forgeName, it) }
        val bundle = ForgeProfilePushBundle(
            pushedAtEpochMillis = System.currentTimeMillis(),
            profile = ForgeProfilePushDto(
                uid = profile.uid,
                forgeName = profile.forgeName,
                currentStage = profile.currentStage,
                currentTitle = profile.currentTitle,
                ageYears = profile.ageYears,
                profileRole = profile.profileRole,
            ),
            avatar = avatarConfig,
        )
        val payload = AvatarHeroCatalog.encodePushBundle(bundle)
        val fileName = "forge_profile_push.json"

        val written = mutableListOf<String>()
        val primaryFile = writeToAppExternal(context, fileName, payload)
        primaryFile?.absolutePath?.let { written += it }
        writeToPublicPhoenixForge(fileName, payload)?.let { path ->
            if (path !in written) written += path
        }

        val shareIntent = buildShareIntent(context, primaryFile, profile, avatarConfig, written)

        return PushResult(
            writtenPaths = written,
            shareIntent = shareIntent,
            message = if (written.isEmpty()) {
                "Could not write export file. Use Share to send profile details manually."
            } else {
                "Pushed JSON to ${written.joinToString(" and ")}. " +
                    "Student Edition → Import Forge Profile → Pull snapshot (or open the shared .json file)."
            },
        )
    }

    private fun buildShareIntent(
        context: Context,
        file: File?,
        profile: ForgeProfile,
        avatar: AvatarConfigV2?,
        writtenPaths: List<String>,
    ): Intent {
        val subject = "Forge Profile push — ${profile.forgeName}"
        return if (file != null && file.exists()) {
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file,
            )
            Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, buildShareHint(profile, avatar, writtenPaths))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        } else {
            Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, buildShareHint(profile, avatar, writtenPaths))
            }
        }
    }

    private fun writeToAppExternal(context: Context, fileName: String, payload: String): File? =
        runCatching {
            val dir = File(context.getExternalFilesDir(null), "PhoenixForge/export").apply { mkdirs() }
            val file = File(dir, fileName)
            file.writeText(payload)
            file
        }.getOrNull()

    private fun writeToPublicPhoenixForge(fileName: String, payload: String): String? =
        runCatching {
            val root = Environment.getExternalStorageDirectory()
            val dir = File(root, "PhoenixForge/export").apply { mkdirs() }
            val file = File(dir, fileName)
            file.writeText(payload)
            file.absolutePath
        }.getOrNull()

    private fun buildShareHint(
        profile: ForgeProfile,
        avatar: AvatarConfigV2?,
        writtenPaths: List<String>,
    ): String = buildString {
        appendLine(parentPushInstructions(profile, avatar))
        if (writtenPaths.isNotEmpty()) {
            appendLine("Export files:")
            writtenPaths.forEach { appendLine("• $it") }
        }
    }

    private fun parentPushInstructions(profile: ForgeProfile, avatar: AvatarConfigV2?): String =
        buildString {
            val isChild = ProfileRole.fromStorageKey(profile.profileRole) == ProfileRole.STUDENT_SELF
            if (isChild) {
                appendLine("Forge snapshot for ${profile.forgeName}")
                avatar?.let {
                    appendLine("Hero: ${AvatarHeroCatalog.displayStyle(it.heroStyle)} · ${AvatarHeroCatalog.displayColor(it.heroColor)}")
                }
                appendLine()
                appendLine("Grown-up: Student Edition → Import Forge Profile → Pull snapshot.")
            } else {
                appendLine(AppBoundaryCopy.MANUAL_SYNC)
                appendLine("Forge name: ${profile.forgeName}")
                appendLine("Profile ID: ${profile.uid}")
                avatar?.let {
                    appendLine("Hero: ${AvatarHeroCatalog.displayStyle(it.heroStyle)} · ${AvatarHeroCatalog.displayColor(it.heroColor)}")
                    appendLine("Godot model: ${it.godotMeshHints.modelPath}")
                }
                appendLine()
                appendLine(AppBoundaryCopy.pushAvatarHint())
            }
        }
}
