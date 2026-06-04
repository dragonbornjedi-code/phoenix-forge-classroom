package com.phoenixforge.student.domain.simulation

import com.phoenixforge.student.domain.model.BehaviorSignals
import com.phoenixforge.student.domain.model.WorldDriftState
import com.phoenixforge.student.domain.model.WorldState
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

@Singleton
class WorldDriftEngine @Inject constructor() {

    fun compute(signals: BehaviorSignals, world: WorldState, daysSinceLastVisit: Long): WorldDriftState {
        val photoPressure = min(signals.photosThisWeek / 8f, 1f)
        val questNeglect = if (signals.questsCompletedThisWeek == 0 && signals.photosThisWeek > 2) 0.7f else 0.2f
        val streakFlow = min(world.streakDays / 14f, 1f)
        val absenceHush = min(daysSinceLastVisit / 5f, 1f)

        val galleryVitality = (photoPressure * 0.8f + streakFlow * 0.2f - absenceHush * 0.3f).coerceIn(0f, 1f)
        val worldQuietness = (questNeglect + absenceHush * 0.5f - photoPressure * 0.3f).coerceIn(0f, 1f)
        val temporalDistortion = (streakFlow * 0.6f + absenceHush * 0.4f).coerceIn(0f, 1f)
        val themeIntensity = (galleryVitality + (1f - worldQuietness) * 0.5f).coerceIn(0.2f, 1f)

        val (label, cue) = when {
            galleryVitality > 0.65f -> "Gallery alive" to "Light pools shift across walls"
            worldQuietness > 0.6f -> "House in hush" to "Rooms hold still air"
            temporalDistortion > 0.55f -> "Time bending" to "Moments stretch and snap"
            else -> "Balanced drift" to "The house breathes evenly"
        }

        return WorldDriftState(
            galleryVitality = galleryVitality,
            worldQuietness = worldQuietness,
            temporalDistortion = temporalDistortion,
            themeIntensity = themeIntensity,
            driftLabel = label,
            visualCue = cue
        )
    }

    fun blendedTheme(baseTheme: String, drift: WorldDriftState): String = when {
        drift.galleryVitality > 0.6f -> "${baseTheme}_alive"
        drift.worldQuietness > 0.55f -> "${baseTheme}_hush"
        drift.temporalDistortion > 0.5f -> "${baseTheme}_bend"
        else -> baseTheme
    }
}
