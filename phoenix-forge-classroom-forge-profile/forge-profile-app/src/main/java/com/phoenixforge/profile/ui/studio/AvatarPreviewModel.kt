package com.phoenixforge.profile.ui.studio

import androidx.compose.ui.graphics.Color
import com.phoenixforge.profile.domain.avatar.AvatarHeroCatalog
import com.phoenixforge.profile.domain.model.Avatar

/** Pure mapping from persisted [Avatar] fields to layered preview tokens. */
data class AvatarPreviewLook(
    val skinColor: Color,
    val hairColor: Color,
    val hairStyle: HairStyle,
    val heroStyle: HeroStyle,
    val heroAccent: Color,
    val eyeColor: Color,
    val clothingColor: Color,
    val clothingStyle: ClothingStyle,
)

enum class HairStyle { SHORT, LONG, CURLY, BALD }

enum class HeroStyle { EXPLORER, BUILDER, ARTIST, GUARDIAN, SCOUT }

enum class ClothingStyle { TUNIC, ARMOR, ROBE, SHIRT, HOODED }

object AvatarPreviewMapper {
    private val defaultSkin = Color(0xFFE8B4A0)
    private val defaultHair = Color(0xFF3B2314)
    private val defaultEyes = Color(0xFF4A6741)
    private val defaultClothing = Color(0xFF2D5A27)

    fun fromAvatar(avatar: Avatar?): AvatarPreviewLook {
        if (avatar == null) return defaults()
        if (isHeroEncoding(avatar)) {
            val heroStyle = heroStyleFor(avatar.hairType)
            val heroAccent = heroAccentFor(avatar.eyeColor)
            return AvatarPreviewLook(
                skinColor = skinToneColor(avatar.skinTone),
                hairColor = defaultHair,
                hairStyle = HairStyle.SHORT,
                heroStyle = heroStyle,
                heroAccent = heroAccent,
                eyeColor = defaultEyes,
                clothingColor = heroAccent.copy(alpha = 0.85f),
                clothingStyle = clothingStyleForHero(heroStyle),
            )
        }
        return AvatarPreviewLook(
            skinColor = skinToneColor(avatar.skinTone),
            hairColor = hairColorForLegacy(avatar.hairType),
            hairStyle = hairStyleForLegacy(avatar.hairType),
            heroStyle = HeroStyle.EXPLORER,
            heroAccent = heroAccentFor("blue"),
            eyeColor = eyeColorForLegacy(avatar.eyeColor),
            clothingColor = clothingColorForLegacy(avatar.clothingId),
            clothingStyle = clothingStyleForLegacy(avatar.clothingId),
        )
    }

    private fun isHeroEncoding(avatar: Avatar): Boolean {
        val hair = avatar.hairType.trim().lowercase()
        val clothing = avatar.clothingId.trim().lowercase()
        return hair in AvatarHeroCatalog.heroStyles ||
            clothing in setOf("knight", "barbarian", "mage", "rogue_hooded", "rogue")
    }

    private fun clothingStyleForLegacy(clothingId: String): ClothingStyle =
        when (clothingId.trim().lowercase()) {
            "armor" -> ClothingStyle.ARMOR
            "robe" -> ClothingStyle.ROBE
            "shirt" -> ClothingStyle.SHIRT
            else -> ClothingStyle.TUNIC
        }

    private fun clothingColorForLegacy(clothingId: String): Color =
        when (clothingId.trim().lowercase()) {
            "armor" -> Color(0xFF6B7280)
            "robe" -> Color(0xFF4C1D95)
            "shirt" -> Color(0xFF2563EB)
            else -> defaultClothing
        }

    fun fromParsedHero(style: String, color: String, skinTone: String): AvatarPreviewLook {
        val heroStyle = heroStyleFor(style)
        val heroAccent = heroAccentFor(color)
        return AvatarPreviewLook(
            skinColor = skinToneColor(skinTone),
            hairColor = defaultHair,
            hairStyle = HairStyle.SHORT,
            heroStyle = heroStyle,
            heroAccent = heroAccent,
            eyeColor = defaultEyes,
            clothingColor = heroAccent.copy(alpha = 0.85f),
            clothingStyle = clothingStyleForHero(heroStyle),
        )
    }

    fun defaults(): AvatarPreviewLook = fromParsedHero("explorer", "blue", "medium")

    fun heroStyleFor(raw: String): HeroStyle =
        when (AvatarHeroCatalog.normalizeStyle(raw)) {
            "builder" -> HeroStyle.BUILDER
            "artist" -> HeroStyle.ARTIST
            "guardian" -> HeroStyle.GUARDIAN
            "scout" -> HeroStyle.SCOUT
            else -> HeroStyle.EXPLORER
        }

    fun heroAccentFor(raw: String): Color =
        when (AvatarHeroCatalog.normalizeColor(raw)) {
            "green" -> Color(0xFF33CC59)
            "gold" -> Color(0xFFCCB326)
            "pink" -> Color(0xFFFF73CC)
            "purple" -> Color(0xFFA673FF)
            else -> Color(0xFF3399FF)
        }

    fun clothingStyleForHero(heroStyle: HeroStyle): ClothingStyle =
        when (heroStyle) {
            HeroStyle.EXPLORER -> ClothingStyle.ARMOR
            HeroStyle.BUILDER -> ClothingStyle.SHIRT
            HeroStyle.ARTIST -> ClothingStyle.ROBE
            HeroStyle.GUARDIAN -> ClothingStyle.HOODED
            HeroStyle.SCOUT -> ClothingStyle.SHIRT
        }

    private fun hairStyleForLegacy(hairType: String): HairStyle =
        when (hairType.trim().lowercase()) {
            "long" -> HairStyle.LONG
            "curly" -> HairStyle.CURLY
            "bald" -> HairStyle.BALD
            in AvatarHeroCatalog.heroStyles -> HairStyle.SHORT
            else -> HairStyle.SHORT
        }

    private fun hairColorForLegacy(hairType: String): Color =
        when (hairType.trim().lowercase()) {
            "bald" -> Color.Transparent
            in AvatarHeroCatalog.heroStyles -> defaultHair
            "long" -> Color(0xFF2A1810)
            "curly" -> Color(0xFF5C3D2E)
            else -> defaultHair
        }

    private fun eyeColorForLegacy(eyeColor: String): Color =
        if (eyeColor.trim().lowercase() in AvatarHeroCatalog.heroColors) {
            defaultEyes
        } else {
            when (eyeColor.trim().lowercase()) {
                "blue" -> Color(0xFF4A90D9)
                "green" -> Color(0xFF4A6741)
                "brown" -> Color(0xFF6B4423)
                "grey", "gray" -> Color(0xFF7A8B99)
                else -> defaultEyes
            }
        }

    fun skinToneColor(skinTone: String): Color =
        when (skinTone.trim().lowercase()) {
            "light" -> Color(0xFFF5D0C5)
            "medium" -> defaultSkin
            "tan" -> Color(0xFFD4A574)
            "deep" -> Color(0xFF8D5524)
            else -> defaultSkin
        }
}
