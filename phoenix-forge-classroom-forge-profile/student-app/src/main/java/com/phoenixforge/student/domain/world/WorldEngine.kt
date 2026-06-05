package com.phoenixforge.student.domain.world

import com.phoenixforge.student.domain.model.HouseState
import com.phoenixforge.student.domain.model.HouseRoomType
import com.phoenixforge.student.domain.model.QuestStatus
import com.phoenixforge.student.domain.model.RoomNode
import com.phoenixforge.student.domain.model.StoryFragment
import com.phoenixforge.student.domain.model.WorldDriftState
import com.phoenixforge.student.domain.model.WorldEvent
import com.phoenixforge.student.domain.model.WorldEventType
import com.phoenixforge.student.domain.model.WorldState
import com.phoenixforge.student.domain.repository.StudentRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow

@Singleton
class WorldEngine @Inject constructor(
    private val repository: StudentRepository
) {
    suspend fun loadWorld(): WorldState {
        val progress = repository.observeProgress().first()
        val house = repository.observeHouse().first()
        val npcs = repository.observeNpcs().first()
        val quests = repository.observeQuests().first()
        val meta = repository.observeWorldMeta().first()

        return WorldState(
            level = progress.level,
            xp = progress.xp,
            streakDays = progress.streakDays,
            unlockedRooms = house.unlockedRoomTypes.map { it.name },
            activeNPCs = npcs.filter { it.isUnlocked },
            lastActivity = meta.first,
            questActive = quests.any { it.status == QuestStatus.AVAILABLE || it.status == QuestStatus.IN_PROGRESS },
            environmentTheme = meta.second,
            drift = meta.third
        )
    }

    suspend fun applyEvent(
        world: WorldState,
        event: WorldEvent,
        story: StoryFragment,
        drift: WorldDriftState,
        environmentTheme: String
    ): WorldState {
        val newXp = world.xp + event.xpReward
        val newLevel = levelForXp(newXp)
        val leveledUp = newLevel > world.level

        val roomFromStory = story.roomUnlocked?.let { listOf(it) }.orEmpty()
        val roomFromLevel = if (leveledUp) roomsUnlockedAtLevel(newLevel) else emptyList()
        val roomFromQuest = if (event.type == WorldEventType.QUEST_COMPLETE) {
            listOfNotNull("ROOM_QUEST_${world.unlockedRooms.size + 1}".takeIf { world.unlockedRooms.size < HouseRoomType.entries.size })
        } else emptyList()

        val updatedRooms = (world.unlockedRooms + roomFromStory + roomFromLevel + roomFromQuest).distinct()

        val updated = world.copy(
            xp = newXp,
            level = newLevel,
            unlockedRooms = updatedRooms,
            lastActivity = event.type.name,
            environmentTheme = environmentTheme,
            drift = drift
        )

        persistWorld(updated, event, leveledUp, drift)
        return updated
    }

    private suspend fun persistWorld(
        world: WorldState,
        event: WorldEvent,
        leveledUp: Boolean,
        drift: WorldDriftState
    ) {
        val progress = repository.observeProgress().first()
        repository.saveProgress(
            progress.copy(
                xp = world.xp,
                level = world.level,
                streakDays = world.streakDays
            )
        )

        val unlockedTypes = world.unlockedRooms.mapNotNull { name ->
            runCatching { HouseRoomType.valueOf(name) }.getOrNull()
                ?: name.removePrefix("ROOM_QUEST_").let { HouseRoomType.entries.getOrNull(it.toIntOrNull()?.minus(1) ?: -1) }
        }.toSet()

        val mergedUnlocked = repository.observeHouse().first().unlockedRoomTypes + unlockedTypes +
            HouseRoomType.entries.filter { world.level >= it.unlockLevel }

        repository.saveHouse(
            HouseState(
                rooms = HouseRoomType.entries.map { type ->
                    RoomNode(type = type, isUnlocked = type in mergedUnlocked)
                },
                unlockedRoomTypes = mergedUnlocked,
                decorations = repository.observeHouse().first().decorations
            )
        )

        repository.saveWorldMeta(world.lastActivity, world.environmentTheme, drift)

        if (leveledUp) {
            repository.recordLifeEvent(
                com.phoenixforge.student.domain.model.LifeEvent(
                    type = com.phoenixforge.student.domain.model.LifeEventType.LEVEL_UP,
                    payload = "level=${world.level}"
                )
            )
        }

        repository.recordLifeEvent(
            com.phoenixforge.student.domain.model.LifeEvent(
                type = mapToLifeEventType(event.type),
                payload = event.payload.entries.joinToString(";") { "${it.key}=${it.value}" }
            )
        )
    }

    suspend fun updateStreak(streakDays: Int) {
        val progress = repository.observeProgress().first()
        repository.saveProgress(progress.copy(streakDays = streakDays))
    }

    fun levelForXp(xp: Int): Int {
        var level = 1
        var threshold = 0
        while (xp >= threshold + xpForLevel(level)) {
            threshold += xpForLevel(level)
            level++
        }
        return level
    }

    fun xpForLevel(level: Int): Int = (50 * level.toDouble().pow(1.2)).toInt()

    private fun roomsUnlockedAtLevel(level: Int): List<String> =
        HouseRoomType.entries.filter { it.unlockLevel == level }.map { it.name }

    private fun mapToLifeEventType(type: WorldEventType): com.phoenixforge.student.domain.model.LifeEventType =
        when (type) {
            WorldEventType.PHOTO_UPLOADED -> com.phoenixforge.student.domain.model.LifeEventType.PHOTO_IMPORTED
            WorldEventType.QUEST_COMPLETE -> com.phoenixforge.student.domain.model.LifeEventType.QUEST_COMPLETED
            WorldEventType.STREAK_MILESTONE, WorldEventType.DAILY_RETURN, WorldEventType.ABSENCE_RETURNED ->
                com.phoenixforge.student.domain.model.LifeEventType.DAILY_RETURN
            WorldEventType.IMPORT_PROFILE -> com.phoenixforge.student.domain.model.LifeEventType.FORGE_PROFILE_IMPORTED
            WorldEventType.ACHIEVEMENT_LOGGED -> com.phoenixforge.student.domain.model.LifeEventType.ACHIEVEMENT_LOGGED
            WorldEventType.LEVEL_UP -> com.phoenixforge.student.domain.model.LifeEventType.LEVEL_UP
        }
}
