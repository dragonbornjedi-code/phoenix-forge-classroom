package com.phoenixforge.profile.data.provider

import com.phoenixforge.profile.domain.avatar.AvatarHeroCatalog
import com.phoenixforge.profile.domain.model.Avatar

/**
 * Step 0.61 — full `/avatar` ContentProvider payload (AvatarConfigV2 + Godot hints).
 */
object AvatarExportPayload {

    fun build(profileUid: String, forgeName: String, avatar: Avatar): AvatarExportDto {
        val config = AvatarHeroCatalog.toConfig(profileUid, forgeName, avatar)
        return AvatarExportDto(
            hairType = avatar.hairType,
            eyeColor = avatar.eyeColor,
            skinTone = avatar.skinTone,
            clothingId = avatar.clothingId,
            version = avatar.version,
            shardLevel = avatar.shardLevel,
            heroStyle = config.heroStyle,
            heroColor = config.heroColor,
            godotModelPath = config.godotMeshHints.modelPath,
            avatarSummary = AvatarHeroCatalog.summaryLine(avatar),
            avatarConfigJson = AvatarHeroCatalog.encodeConfig(config),
        )
    }
}
