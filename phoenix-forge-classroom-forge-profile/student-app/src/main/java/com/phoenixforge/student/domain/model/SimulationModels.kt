package com.phoenixforge.student.domain.model

import kotlinx.serialization.Serializable

/** Shared spine: every event produces emotional weight that propagates to story, NPC, and world. */
data class EmotionalImpact(
    val score: Float,
    val valence: EmotionalValence,
    val intensity: Float,
    val tone: NarrativeTone
)

enum class EmotionalValence { JOY, WONDER, PRIDE, LONGING, STILLNESS, URGENCY }

enum class NarrativeTone { WARM, BRIGHT, QUIET, MYSTERIOUS, URGENT, REFLECTIVE }

data class BehaviorSignals(
    val photosThisWeek: Int = 0,
    val questsCompletedThisWeek: Int = 0,
    val returnsThisWeek: Int = 0,
    val lastPhotoEpochMillis: Long = 0L,
    val lastQuestEpochMillis: Long = 0L,
    val lastVisitEpochMillis: Long = 0L,
    val weekAnchorEpochMillis: Long = 0L
)

/** Persistent environmental drift derived from behavior — not a one-off theme swap. */
@Serializable
data class WorldDriftState(
    val galleryVitality: Float,
    val worldQuietness: Float,
    val temporalDistortion: Float,
    val themeIntensity: Float,
    val driftLabel: String,
    val visualCue: String
)

@Serializable
data class NpcMemoryAnchor(
    val summary: String,
    val eventType: String,
    val strength: Float,
    val weekAnchorEpochMillis: Long
)

data class StoryGraphNode(
    val fragment: StoryFragment,
    val causeFragment: StoryFragment?,
    val linkedNpcMood: String?
)
