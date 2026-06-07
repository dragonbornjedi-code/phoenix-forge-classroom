package com.phoenixforge.profile.domain.avatar

import com.phoenixforge.profile.domain.model.Avatar
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import java.util.UUID

/**
 * Sovereign avatar contract aligned with embral/Godot KayKit adventurers.
 * Persisted in Room via [Avatar] field mapping until full AvatarConfig migration (0.59).
 */
object AvatarHeroCatalog {
    val heroStyles: List<String> = listOf("explorer", "builder", "artist", "guardian", "scout")
    val heroColors: List<String> = listOf("blue", "green", "gold", "pink", "purple")
    val skinTones: List<String> = listOf("light", "medium", "tan", "deep")

    private val styleToModelFile = mapOf(
        "explorer" to "Knight.glb",
        "builder" to "Barbarian.glb",
        "artist" to "Mage.glb",
        "guardian" to "Rogue_Hooded.glb",
        "scout" to "Rogue.glb",
    )

    private val styleToClothingId = mapOf(
        "explorer" to "knight",
        "builder" to "barbarian",
        "artist" to "mage",
        "guardian" to "rogue_hooded",
        "scout" to "rogue",
    )

    fun godotModelPath(style: String): String {
        val file = styleToModelFile[normalizeStyle(style)] ?: styleToModelFile.getValue("explorer")
        return "res://assets/models/kaykit_adventurers/$file"
    }

    fun clothingIdForStyle(style: String): String =
        styleToClothingId[normalizeStyle(style)] ?: "knight"

    fun normalizeStyle(raw: String): String =
        raw.trim().lowercase().let { value ->
            when (value) {
                in heroStyles -> value
                "knight" -> "explorer"
                "barbarian" -> "builder"
                "mage" -> "artist"
                "rogue", "rogue_hooded" -> "guardian"
                "scout" -> "scout"
                else -> "explorer"
            }
        }

    fun normalizeSkinTone(raw: String): String =
        raw.trim().lowercase().let { value ->
            if (value in skinTones) value else "medium"
        }

    fun normalizeColor(raw: String): String =
        raw.trim().lowercase().let { value ->
            if (value in heroColors) value else "blue"
        }

    fun defaultAvatar(timestamp: Instant = Instant.now()): Avatar =
        buildAvatar(
            style = "explorer",
            color = "blue",
            skinTone = "medium",
            version = 1,
            timestamp = timestamp,
        )

    fun buildAvatar(
        style: String,
        color: String,
        skinTone: String = "medium",
        version: Int,
        timestamp: Instant = Instant.now(),
        id: String = UUID.randomUUID().toString(),
    ): Avatar {
        val normalizedStyle = normalizeStyle(style)
        return Avatar(
            id = id,
            hairType = normalizedStyle,
            eyeColor = normalizeColor(color),
            skinTone = normalizeSkinTone(skinTone),
            clothingId = clothingIdForStyle(normalizedStyle),
            version = version,
            timestamp = timestamp,
        )
    }

    fun randomAvatar(previous: Avatar?): Avatar {
        val style = heroStyles.random()
        val color = heroColors.random()
        return buildAvatar(
            style = style,
            color = color,
            skinTone = previous?.skinTone ?: "medium",
            version = (previous?.version ?: 0) + 1,
        )
    }

    fun toConfig(profileUid: String, forgeName: String, avatar: Avatar): AvatarConfigV2 =
        AvatarConfigV2(
            childId = profileUid,
            forgeName = forgeName,
            schemaVersion = 2,
            heroStyle = normalizeStyle(avatar.hairType),
            heroColor = normalizeColor(avatar.eyeColor),
            skinTone = avatar.skinTone,
            clothingId = avatar.clothingId,
            avatarVersion = avatar.version,
            godotMeshHints = GodotMeshHints(
                modelPath = godotModelPath(avatar.hairType),
                attachmentSlots = mapOf(
                    "hat" to null,
                    "cape" to null,
                ),
            ),
        )

    fun encodeConfig(config: AvatarConfigV2): String =
        json.encodeToString(config)

    fun encodePushBundle(bundle: ForgeProfilePushBundle): String =
        json.encodeToString(bundle)

    private val json = Json { prettyPrint = true }

    fun summaryLine(avatar: Avatar): String =
        listOf(
            normalizeStyle(avatar.hairType),
            normalizeColor(avatar.eyeColor),
            avatar.skinTone,
            avatar.clothingId,
            "v${avatar.version}",
        ).joinToString("|")

    fun parseSummary(summary: String?): ParsedHeroLook? {
        if (summary.isNullOrBlank()) return null
        val parts = summary.split("|")
        if (parts.size < 4) return null
        return ParsedHeroLook(
            style = normalizeStyle(parts[0]),
            color = normalizeColor(parts[1]),
            skinTone = parts[2],
            clothingId = parts[3],
            version = parts.getOrNull(4)?.removePrefix("v")?.toIntOrNull() ?: 1,
        )
    }

    fun displayStyle(style: String): String =
        normalizeStyle(style).replaceFirstChar { it.titlecase() }

    fun displayColor(color: String): String =
        normalizeColor(color).replaceFirstChar { it.titlecase() }

    fun displaySkinTone(skinTone: String): String =
        normalizeSkinTone(skinTone).replaceFirstChar { it.titlecase() }
}

data class ParsedHeroLook(
    val style: String,
    val color: String,
    val skinTone: String,
    val clothingId: String,
    val version: Int,
)

@Serializable
data class AvatarConfigV2(
    val childId: String,
    val forgeName: String,
    val schemaVersion: Int,
    val heroStyle: String,
    val heroColor: String,
    val skinTone: String,
    val clothingId: String,
    val avatarVersion: Int,
    val godotMeshHints: GodotMeshHints,
)

@Serializable
data class GodotMeshHints(
    val modelPath: String,
    val attachmentSlots: Map<String, String?>,
)

@Serializable
data class ForgeProfilePushBundle(
    val pushedAtEpochMillis: Long,
    val pushKind: String = "manual_steward_push",
    val profile: ForgeProfilePushDto,
    val avatar: AvatarConfigV2?,
)

@Serializable
data class ForgeProfilePushDto(
    val uid: String,
    val forgeName: String,
    val currentStage: String,
    val currentTitle: String?,
    val ageYears: Int?,
    val profileRole: String?,
)
