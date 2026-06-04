package com.phoenixforge.student.domain.simulation

import com.phoenixforge.student.domain.model.BehaviorSignals
import com.phoenixforge.student.domain.model.EmotionalImpact
import com.phoenixforge.student.domain.model.EmotionalValence
import com.phoenixforge.student.domain.model.NarrativeTone
import com.phoenixforge.student.domain.model.WorldEvent
import com.phoenixforge.student.domain.model.WorldEventType
import com.phoenixforge.student.domain.model.WorldState
import com.phoenixforge.student.domain.model.payload
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

@Singleton
class EmotionalImpactSpine @Inject constructor() {

    fun score(
        event: WorldEvent,
        world: WorldState,
        signals: BehaviorSignals,
        daysSinceLastVisit: Long
    ): EmotionalImpact {
        val base = event.importance * 0.15f + event.xpReward * 0.01f
        val absenceBoost = if (daysSinceLastVisit >= 3) 0.35f else 0f
        val streakBoost = min(world.streakDays * 0.04f, 0.4f)
        val photoHabit = min(signals.photosThisWeek * 0.05f, 0.25f)

        val score = (base + absenceBoost + streakBoost + photoHabit).coerceIn(0.1f, 1f)

        val (valence, tone) = when (event.type) {
            WorldEventType.PHOTO_UPLOADED -> EmotionalValence.JOY to NarrativeTone.WARM
            WorldEventType.QUEST_COMPLETE -> EmotionalValence.PRIDE to NarrativeTone.BRIGHT
            WorldEventType.STREAK_MILESTONE -> EmotionalValence.WONDER to NarrativeTone.BRIGHT
            WorldEventType.IMPORT_PROFILE -> EmotionalValence.WONDER to NarrativeTone.MYSTERIOUS
            WorldEventType.ABSENCE_RETURNED -> EmotionalValence.LONGING to NarrativeTone.REFLECTIVE
            WorldEventType.DAILY_RETURN -> if (daysSinceLastVisit >= 3) {
                EmotionalValence.LONGING to NarrativeTone.REFLECTIVE
            } else {
                EmotionalValence.JOY to NarrativeTone.WARM
            }
            WorldEventType.ACHIEVEMENT_LOGGED -> EmotionalValence.PRIDE to NarrativeTone.BRIGHT
            WorldEventType.LEVEL_UP -> EmotionalValence.WONDER to NarrativeTone.BRIGHT
        }

        val intensity = when {
            event.type == WorldEventType.ABSENCE_RETURNED -> 0.9f
            daysSinceLastVisit >= 5 -> 0.85f
            signals.questsCompletedThisWeek == 0 && signals.photosThisWeek > 3 -> 0.7f
            else -> score
        }.coerceIn(0.2f, 1f)

        return EmotionalImpact(score = score, valence = valence, intensity = intensity, tone = tone)
    }

    fun memoryStrength(impact: EmotionalImpact, event: WorldEvent): Float =
        (impact.score * impact.intensity * event.importance / 5f).coerceIn(0.15f, 1f)
}
