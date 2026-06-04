package com.phoenixforge.student.domain.engine

import com.phoenixforge.student.domain.model.StudentProgress
import com.phoenixforge.student.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow

@Singleton
class ProgressionEngine @Inject constructor(
    private val repository: StudentRepository
) {
    fun observeProgress(): Flow<StudentProgress> = repository.observeProgress()

    suspend fun addXp(amount: Int): StudentProgress {
        val current = repository.observeProgress().first()
        val newXp = current.xp + amount
        val newLevel = levelForXp(newXp)
        val leveledUp = newLevel > current.level
        val updated = current.copy(xp = newXp, level = newLevel)
        repository.saveProgress(updated)
        if (leveledUp) {
            repository.recordLifeEvent(
                com.phoenixforge.student.domain.model.LifeEvent(
                    type = com.phoenixforge.student.domain.model.LifeEventType.LEVEL_UP,
                    payload = "level=$newLevel"
                )
            )
        }
        return updated
    }

    suspend fun recordDailyReturn(): StudentProgress {
        val now = System.currentTimeMillis()
        val current = repository.observeProgress().first()
        val dayMillis = 86_400_000L
        val lastDay = if (current.lastVisitEpochMillis == 0L) 0L else current.lastVisitEpochMillis / dayMillis
        val today = now / dayMillis
        val newStreak = when {
            lastDay == 0L -> 1
            today == lastDay -> current.streakDays
            today == lastDay + 1 -> current.streakDays + 1
            else -> 1
        }
        val updated = current.copy(streakDays = newStreak, lastVisitEpochMillis = now)
        repository.saveProgress(updated)
        return updated
    }

    suspend fun addUnlockFlag(flag: String): StudentProgress {
        val current = repository.observeProgress().first()
        val updated = current.copy(unlockFlags = current.unlockFlags + flag)
        repository.saveProgress(updated)
        return updated
    }

    suspend fun addAchievement(id: String): StudentProgress {
        val current = repository.observeProgress().first()
        if (id in current.achievementIds) return current
        val updated = current.copy(achievementIds = current.achievementIds + id)
        repository.saveProgress(updated)
        return updated
    }

    fun xpForNextLevel(level: Int): Int = (50 * level.toDouble().pow(1.2)).toInt()

    private fun levelForXp(xp: Int): Int {
        var level = 1
        var threshold = 0
        while (xp >= threshold + xpForNextLevel(level)) {
            threshold += xpForNextLevel(level)
            level++
        }
        return level
    }
}
