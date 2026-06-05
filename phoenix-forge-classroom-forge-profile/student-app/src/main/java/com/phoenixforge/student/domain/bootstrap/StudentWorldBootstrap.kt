package com.phoenixforge.student.domain.bootstrap

import com.phoenixforge.student.domain.engine.QuestEngine
import com.phoenixforge.student.domain.house.StudentHouse
import com.phoenixforge.student.domain.model.NpcState
import com.phoenixforge.student.domain.model.NpcType
import com.phoenixforge.student.domain.model.StudentProgress
import com.phoenixforge.student.domain.repository.StudentRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentWorldBootstrap @Inject constructor(
    private val repository: StudentRepository,
    private val studentHouse: StudentHouse,
    private val questEngine: QuestEngine
) {
    suspend fun ensureWorldInitialized() {
        val progress = repository.observeProgress().first()
        if (progress.lastVisitEpochMillis == 0L) {
            repository.saveProgress(
                StudentProgress(
                    xp = 0,
                    level = 1,
                    streakDays = 0,
                    lastVisitEpochMillis = System.currentTimeMillis(),
                    unlockFlags = emptySet(),
                    achievementIds = emptySet()
                )
            )
            studentHouse.initializeDefaultHouse()
            repository.saveNpc(
                NpcState(
                    id = "companion_spark",
                    type = NpcType.COMPANION,
                    name = "Spark",
                    evolutionStage = 1,
                    isUnlocked = true,
                    lastReaction = "Welcome to Phoenix Forge Classroom Student Edition!",
                    mood = "excited",
                    personalityTraits = listOf("empathetic", "curious", "loyal")
                )
            )
            repository.saveNpc(
                NpcState(
                    id = "whisp_lumen",
                    type = NpcType.WHISP,
                    name = "Lumen",
                    evolutionStage = 0,
                    isUnlocked = false,
                    lastReaction = null,
                    mood = "curious",
                    personalityTraits = listOf("elusive", "playful")
                )
            )
            repository.saveWorldMeta(lastActivity = null, environmentTheme = "forest_dawn", drift = null)
            questEngine.seedStarterQuests()
        }
    }
}
