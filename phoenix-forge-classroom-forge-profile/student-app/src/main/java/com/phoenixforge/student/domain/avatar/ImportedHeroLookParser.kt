package com.phoenixforge.student.domain.avatar

/**
 * Mirrors forge-profile-core [AvatarHeroCatalog] for Student Edition display.
 * Keeps student-app independent of forge-profile-core AAR.
 */
object ImportedHeroLookParser {
    fun parse(summary: String?): ParsedHeroLook? {
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

    fun normalizeStyle(raw: String): String =
        when (raw.trim().lowercase()) {
            "explorer", "builder", "artist", "guardian" -> raw.trim().lowercase()
            "knight" -> "explorer"
            "barbarian" -> "builder"
            "mage" -> "artist"
            "rogue", "rogue_hooded" -> "guardian"
            else -> "explorer"
        }

    fun normalizeColor(raw: String): String =
        when (raw.trim().lowercase()) {
            "blue", "green", "gold", "pink", "purple" -> raw.trim().lowercase()
            else -> "blue"
        }

    fun displayStyle(style: String): String =
        normalizeStyle(style).replaceFirstChar { it.titlecase() }

    fun displayColor(color: String): String =
        normalizeColor(color).replaceFirstChar { it.titlecase() }

    fun styleGlyph(style: String): String =
        when (normalizeStyle(style)) {
            "builder" -> "⚒"
            "artist" -> "✦"
            "guardian" -> "☾"
            else -> "🛡"
        }

    fun sparkWelcome(forgeName: String, style: String): String =
        when (normalizeStyle(style)) {
            "builder" -> "Spark says: $forgeName, let's build something awesome today!"
            "artist" -> "Spark says: $forgeName, your colors are glowing — time to make magic!"
            "guardian" -> "Spark says: $forgeName, your hearth is cozy and safe. Ready to explore?"
            else -> "Spark says: Welcome home, $forgeName! Your big adventure starts right here!"
        }
}

data class ParsedHeroLook(
    val style: String,
    val color: String,
    val skinTone: String,
    val clothingId: String,
    val version: Int,
)
