package com.phoenixforge.student.domain.story

import com.phoenixforge.student.domain.model.EmotionalImpact
import com.phoenixforge.student.domain.model.HouseRoomType
import com.phoenixforge.student.domain.model.NpcReaction
import com.phoenixforge.student.domain.model.StoryFragment
import com.phoenixforge.student.domain.model.WorldEvent
import com.phoenixforge.student.domain.model.WorldEventType
import com.phoenixforge.student.domain.model.WorldState
import com.phoenixforge.student.domain.model.payload
import com.phoenixforge.student.domain.repository.StudentRepository
import com.phoenixforge.student.domain.simulation.StoryGraphEngine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryEngine @Inject constructor(
    private val repository: StudentRepository,
    private val storyGraphEngine: StoryGraphEngine
) {
    suspend fun generate(
        event: WorldEvent,
        world: WorldState,
        npcReactions: List<NpcReaction>,
        impact: EmotionalImpact
    ): StoryFragment {
        val speaker = npcReactions.firstOrNull()?.npcName
            ?: world.activeNPCs.firstOrNull { it.type == com.phoenixforge.student.domain.model.NpcType.COMPANION }?.name

        val cause = storyGraphEngine.findCauseFragment(event, world)
        val fragment = storyGraphEngine.composeFragment(event, world, impact, npcReactions, cause, speaker)
            .copy(
                roomUnlocked = resolveRoomUnlock(event, world),
                environmentChange = resolveEnvironmentChange(event, world)
            )

        repository.saveStoryFragment(fragment)
        return fragment
    }

    private fun resolveRoomUnlock(event: WorldEvent, world: WorldState): String? {
        if (event.type != WorldEventType.QUEST_COMPLETE && event.type != WorldEventType.LEVEL_UP) return null
        val nextRoom = HouseRoomType.entries.firstOrNull { room ->
            room.name !in world.unlockedRooms && world.level + 1 >= room.unlockLevel
        }
        return nextRoom?.name
    }

    private fun resolveEnvironmentChange(event: WorldEvent, world: WorldState): String? = when (event.type) {
        WorldEventType.PHOTO_UPLOADED -> "warm_gallery"
        WorldEventType.IMPORT_PROFILE -> "origin_aurora"
        WorldEventType.STREAK_MILESTONE -> "streak_fire"
        WorldEventType.QUEST_COMPLETE -> "quest_glow"
        WorldEventType.ABSENCE_RETURNED -> "return_glow"
        else -> null
    } ?: world.environmentTheme
}
