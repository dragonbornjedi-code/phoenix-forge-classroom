package com.phoenixforge.student.domain.world

import com.phoenixforge.student.domain.engine.NPCEngine
import com.phoenixforge.student.domain.engine.ProgressionEngine
import com.phoenixforge.student.domain.engine.QuestEngine
import com.phoenixforge.student.domain.engine.RewardEngine
import com.phoenixforge.student.domain.model.Quest
import com.phoenixforge.student.domain.model.WorldEvent
import com.phoenixforge.student.domain.model.WorldEventResult
import com.phoenixforge.student.domain.model.WorldEventType
import com.phoenixforge.student.domain.simulation.BehaviorSignalTracker
import com.phoenixforge.student.domain.simulation.EmotionalImpactSpine
import com.phoenixforge.student.domain.simulation.WorldDriftEngine
import com.phoenixforge.student.domain.story.StoryEngine
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorldOrchestrator @Inject constructor(
    private val worldEngine: WorldEngine,
    private val storyEngine: StoryEngine,
    private val npcEngine: NPCEngine,
    private val progressionEngine: ProgressionEngine,
    private val rewardEngine: RewardEngine,
    private val questEngine: QuestEngine,
    private val emotionalImpactSpine: EmotionalImpactSpine,
    private val behaviorSignalTracker: BehaviorSignalTracker,
    private val worldDriftEngine: WorldDriftEngine
) {
    suspend fun dispatch(event: WorldEvent, daysSinceLastVisit: Long? = null): WorldEventResult {
        val worldBefore = worldEngine.loadWorld()
        val preSignals = behaviorSignalTracker.current()
        val daysSince = daysSinceLastVisit
            ?: behaviorSignalTracker.daysSinceLastVisit(preSignals)
        val signals = behaviorSignalTracker.recordEvent(event)

        val impact = emotionalImpactSpine.score(event, worldBefore, signals, daysSince)
        val npcReactions = npcEngine.processEvent(event, worldBefore, impact, signals.weekAnchorEpochMillis)
        val story = storyEngine.generate(event, worldBefore, npcReactions, impact)
        val drift = worldDriftEngine.compute(signals, worldBefore, daysSince)
        val blendedTheme = worldDriftEngine.blendedTheme(
            story.environmentChange ?: worldBefore.environmentTheme,
            drift
        )
        val worldAfter = worldEngine.applyEvent(
            world = worldBefore,
            event = event,
            story = story,
            drift = drift,
            environmentTheme = blendedTheme
        )

        val unlockId = event.payload["unlockRewardId"]
        if (!unlockId.isNullOrBlank()) {
            rewardEngine.grantUnlock(unlockId, event.payload["reason"] ?: event.type.name)
        }

        val dialogue = npcReactions.firstOrNull()?.dialogue
        val message = buildString {
            append("+${event.xpReward} XP")
            story.roomUnlocked?.let { append(" · Unlocked $it") }
            dialogue?.let { append(" · $it") }
            if (drift.driftLabel.isNotBlank()) append(" · ${drift.driftLabel}")
        }

        return WorldEventResult(
            world = worldAfter.copy(drift = drift),
            story = story,
            npcReactions = npcReactions,
            message = message.ifBlank { story.narrative },
            emotionalImpact = impact,
            drift = drift
        )
    }

    suspend fun onDailyReturn(): WorldEventResult {
        val preSignals = behaviorSignalTracker.current()
        val daysSince = behaviorSignalTracker.daysSinceLastVisit(preSignals)
        progressionEngine.recordDailyReturn()
        worldEngine.updateStreak(progressionEngine.observeProgress().first().streakDays)

        val progress = progressionEngine.observeProgress().first()
        val (eventType, xp, importance) = when {
            daysSince >= 3 -> Triple(
                WorldEventType.ABSENCE_RETURNED,
                25,
                4
            )
            progress.streakDays > 0 && progress.streakDays % 7 == 0 -> Triple(
                WorldEventType.STREAK_MILESTONE,
                35,
                3
            )
            else -> Triple(WorldEventType.DAILY_RETURN, 10, 1)
        }

        return dispatch(
            WorldEvent(
                type = eventType,
                payload = buildMap {
                    put("streakDays", progress.streakDays.toString())
                    if (daysSince >= 3) put("daysAway", daysSince.toString())
                },
                xpReward = xp,
                importance = importance
            ),
            daysSinceLastVisit = daysSince
        )
    }

    suspend fun onPhotoUploaded(tag: String): WorldEventResult =
        dispatch(
            WorldEvent(
                type = WorldEventType.PHOTO_UPLOADED,
                payload = mapOf("tag" to tag),
                xpReward = 30,
                importance = 2
            )
        )

    suspend fun onQuestComplete(quest: Quest): WorldEventResult {
        questEngine.completeQuest(quest.id)
        return dispatch(
            WorldEvent(
                type = WorldEventType.QUEST_COMPLETE,
                payload = mapOf(
                    "questTitle" to quest.title,
                    "questId" to quest.id,
                    "unlockRewardId" to (quest.unlockRewardId ?: ""),
                    "reason" to quest.title
                ),
                xpReward = quest.xpReward,
                importance = 3
            )
        )
    }

    suspend fun onForgeProfileImported(forgeName: String, currentStage: String?, uid: String): WorldEventResult =
        dispatch(
            WorldEvent(
                type = WorldEventType.IMPORT_PROFILE,
                payload = mapOf(
                    "forgeName" to forgeName,
                    "currentStage" to (currentStage ?: "EARLY_DISCOVERY"),
                    "uid" to uid
                ),
                xpReward = 50,
                importance = 5
            )
        )

    suspend fun onAchievementLogged(title: String): WorldEventResult {
        progressionEngine.addAchievement("achievement:$title")
        return dispatch(
            WorldEvent(
                type = WorldEventType.ACHIEVEMENT_LOGGED,
                payload = mapOf("title" to title),
                xpReward = 20,
                importance = 2
            )
        )
    }

    suspend fun completeMatchingQuest(titleFragment: String) {
        val quest = questEngine.observeQuests().first()
            .find { titleFragment in it.title && it.status == com.phoenixforge.student.domain.model.QuestStatus.AVAILABLE }
        quest?.let { onQuestComplete(it) }
    }
}
