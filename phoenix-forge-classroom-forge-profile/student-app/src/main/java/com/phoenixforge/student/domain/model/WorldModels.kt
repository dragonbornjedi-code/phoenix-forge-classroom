package com.phoenixforge.student.domain.model

import kotlinx.serialization.Serializable

enum class WorldEventType {
    PHOTO_UPLOADED,
    QUEST_COMPLETE,
    STREAK_MILESTONE,
    IMPORT_PROFILE,
    DAILY_RETURN,
    ABSENCE_RETURNED,
    ACHIEVEMENT_LOGGED,
    LEVEL_UP
}

data class WorldEvent(
    val type: WorldEventType,
    val payload: Map<String, String> = emptyMap(),
    val xpReward: Int = 0,
    val importance: Int = 1,
    val timestampEpochMillis: Long = System.currentTimeMillis()
)

data class WorldState(
    val level: Int,
    val xp: Int,
    val streakDays: Int,
    val unlockedRooms: List<String>,
    val activeNPCs: List<NpcState>,
    val lastActivity: String?,
    val questActive: Boolean,
    val environmentTheme: String = "forest_dawn",
    val drift: WorldDriftState? = null
)

@Serializable
data class NpcMemoryNode(
    val eventType: String,
    val summary: String,
    val emotionalWeight: Int,
    val strength: Float = 1f,
    val isAnchor: Boolean = false,
    val timestampEpochMillis: Long,
    val interpretedAs: String? = null
)

data class NpcReaction(
    val npcId: String,
    val npcName: String,
    val dialogue: String,
    val moodAfter: String
)

data class StoryFragment(
    val id: String,
    val narrative: String,
    val worldEventType: WorldEventType,
    val xpAwarded: Int,
    val roomUnlocked: String?,
    val npcSpeaker: String?,
    val environmentChange: String?,
    val timestampEpochMillis: Long,
    val causeFragmentId: String? = null,
    val callbackLine: String? = null,
    val emotionalImpact: Float = 0f,
    val continuityThread: String? = null
)

data class WorldEventResult(
    val world: WorldState,
    val story: StoryFragment,
    val npcReactions: List<NpcReaction>,
    val message: String,
    val emotionalImpact: EmotionalImpact? = null,
    val drift: WorldDriftState? = null
)

fun WorldEvent.payload(key: String): String? = payload[key]
