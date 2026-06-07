package com.phoenixforge.classroom.teacher.domain.model

/**
 * Level-3 steward reflection axes shown when marking an intent tile complete.
 * See EXPEDITION_BOARD_UX.md §3 — Deep Reflection.
 */
data class StewardReflection(
    val mental: Int? = null,
    val emotional: Int? = null,
    val physical: Int? = null,
    val educational: Int? = null,
    val behavioral: Int? = null,
) {
    fun toTileFields(): TileReflectionFields = TileReflectionFields(
        reflectionMental = mental,
        reflectionEmotional = emotional,
        reflectionPhysical = physical,
        reflectionEducational = educational,
        reflectionBehavioral = behavioral,
    )

    companion object {
        fun fromTile(tile: IntentTile): StewardReflection = StewardReflection(
            mental = tile.reflectionMental,
            emotional = tile.reflectionEmotional,
            physical = tile.reflectionPhysical,
            educational = tile.reflectionEducational,
            behavioral = tile.reflectionBehavioral,
        )
    }
}

data class TileReflectionFields(
    val reflectionMental: Int? = null,
    val reflectionEmotional: Int? = null,
    val reflectionPhysical: Int? = null,
    val reflectionEducational: Int? = null,
    val reflectionBehavioral: Int? = null,
)

data class StewardReflectionAxis(
    val id: AxisId,
    val label: String,
    val lowLabel: String,
    val highLabel: String,
) {
    enum class AxisId {
        MENTAL,
        EMOTIONAL,
        PHYSICAL,
        EDUCATIONAL,
        BEHAVIORAL,
    }
}

object StewardReflectionCatalog {
    const val NEUTRAL = 50

    val axes = listOf(
        StewardReflectionAxis(
            id = StewardReflectionAxis.AxisId.MENTAL,
            label = "Mental",
            lowLabel = "Distracted",
            highLabel = "Focused",
        ),
        StewardReflectionAxis(
            id = StewardReflectionAxis.AxisId.EMOTIONAL,
            label = "Emotional",
            lowLabel = "Frustrated",
            highLabel = "Happy",
        ),
        StewardReflectionAxis(
            id = StewardReflectionAxis.AxisId.PHYSICAL,
            label = "Physical",
            lowLabel = "Tired",
            highLabel = "Energetic",
        ),
        StewardReflectionAxis(
            id = StewardReflectionAxis.AxisId.EDUCATIONAL,
            label = "Educational",
            lowLabel = "Easy",
            highLabel = "Challenging",
        ),
        StewardReflectionAxis(
            id = StewardReflectionAxis.AxisId.BEHAVIORAL,
            label = "Behavioral",
            lowLabel = "Supported",
            highLabel = "Independent",
        ),
    )

    fun valueFor(reflection: StewardReflection, axis: StewardReflectionAxis): Int =
        when (axis.id) {
            StewardReflectionAxis.AxisId.MENTAL -> reflection.mental
            StewardReflectionAxis.AxisId.EMOTIONAL -> reflection.emotional
            StewardReflectionAxis.AxisId.PHYSICAL -> reflection.physical
            StewardReflectionAxis.AxisId.EDUCATIONAL -> reflection.educational
            StewardReflectionAxis.AxisId.BEHAVIORAL -> reflection.behavioral
        } ?: NEUTRAL

    fun withValue(reflection: StewardReflection, axis: StewardReflectionAxis, value: Int): StewardReflection =
        when (axis.id) {
            StewardReflectionAxis.AxisId.MENTAL -> reflection.copy(mental = value)
            StewardReflectionAxis.AxisId.EMOTIONAL -> reflection.copy(emotional = value)
            StewardReflectionAxis.AxisId.PHYSICAL -> reflection.copy(physical = value)
            StewardReflectionAxis.AxisId.EDUCATIONAL -> reflection.copy(educational = value)
            StewardReflectionAxis.AxisId.BEHAVIORAL -> reflection.copy(behavioral = value)
        }

    fun poleLabel(axis: StewardReflectionAxis, value: Int): String =
        when {
            value <= 33 -> axis.lowLabel
            value >= 67 -> axis.highLabel
            else -> "Balanced"
        }
}
