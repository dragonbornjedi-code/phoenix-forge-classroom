package com.phoenixforge.profile.data.export

import android.content.Context
import android.content.Intent
import android.os.Environment
import com.phoenixforge.profile.domain.copy.AppBoundaryCopy
import com.phoenixforge.profile.domain.avatar.AvatarConfigV2
import com.phoenixforge.profile.domain.avatar.AvatarHeroCatalog
import com.phoenixforge.profile.domain.avatar.ForgeProfilePushBundle
import com.phoenixforge.profile.domain.avatar.ForgeProfilePushDto
import com.phoenixforge.profile.domain.model.Avatar
import com.phoenixforge.profile.domain.model.ForgeProfile
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
        writeToAppExternal(context, fileName, payload)?.let { written += it }
        writeToPublicPhoenixForge(fileName, payload)?.let { written += it }

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Forge Profile push — ${profile.forgeName}")
            putExtra(Intent.EXTRA_TEXT, buildShareText(profile, avatarConfig, written))
        }

        return PushResult(
            writtenPaths = written,
            shareIntent = shareIntent,
            message = if (written.isEmpty()) {
                "Could not write export file. Use Share to send profile details manually."
            } else {
                "Pushed to ${written.joinToString(" and ")}. On Ezra's tablet: Student Edition → Import Forge Profile → Pull snapshot."
            },
        )
    }

    private fun writeToAppExternal(context: Context, fileName: String, payload: String): String? =
        runCatching {
            val dir = File(context.getExternalFilesDir(null), "PhoenixForge/export").apply { mkdirs() }
            val file = File(dir, fileName)
            file.writeText(payload)
            file.absolutePath
        }.getOrNull()

    private fun writeToPublicPhoenixForge(fileName: String, payload: String): String? =
        runCatching {
            val root = Environment.getExternalStorageDirectory()
            val dir = File(root, "PhoenixForge/export").apply { mkdirs() }
            val file = File(dir, fileName)
            file.writeText(payload)
            file.absolutePath
        }.getOrNull()

    private fun buildShareText(
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
