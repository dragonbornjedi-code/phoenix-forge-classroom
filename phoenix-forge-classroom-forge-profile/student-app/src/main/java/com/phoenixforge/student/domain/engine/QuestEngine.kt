package com.phoenixforge.student.domain.engine

import com.phoenixforge.student.domain.model.Quest
import com.phoenixforge.student.domain.model.QuestStatus
import com.phoenixforge.student.domain.model.QuestType
import com.phoenixforge.student.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuestEngine @Inject constructor(
    private val repository: StudentRepository
) {
    fun observeQuests(): Flow<List<Quest>> = repository.observeQuests()

    fun observeActiveQuests(): Flow<List<Quest>> =
        observeQuests().map { quests ->
            quests.filter { it.status == QuestStatus.AVAILABLE || it.status == QuestStatus.IN_PROGRESS }
        }

    suspend fun seedStarterQuests() {
        val existing = repository.observeQuests().first()
        if (existing.isNotEmpty()) return
        listOf(
            Quest(
                id = UUID.randomUUID().toString(),
                type = QuestType.DAILY,
                title = "Morning Explorer",
                description = "Open Student Edition today.",
                status = QuestStatus.AVAILABLE,
                xpReward = 25,
                unlockRewardId = null,
                createdAtEpochMillis = System.currentTimeMillis()
            ),
            Quest(
                id = UUID.randomUUID().toString(),
                type = QuestType.CREATIVE,
                title = "Capture a Moment",
                description = "Import one photo into your Memory Vault.",
                status = QuestStatus.AVAILABLE,
                xpReward = 40,
                unlockRewardId = "decoration:GALLERY:frame_bronze",
                createdAtEpochMillis = System.currentTimeMillis()
            ),
            Quest(
                id = UUID.randomUUID().toString(),
                type = QuestType.SIDE,
                title = "Meet Spark",
                description = "Visit the NPC Room and greet your companion.",
                status = QuestStatus.AVAILABLE,
                xpReward = 15,
                unlockRewardId = null,
                createdAtEpochMillis = System.currentTimeMillis()
            )
        ).forEach { repository.saveQuest(it) }
    }

    suspend fun completeQuest(questId: String): Quest? {
        val quest = repository.observeQuests().first().find { it.id == questId } ?: return null
        val completed = quest.copy(status = QuestStatus.COMPLETED)
        repository.saveQuest(completed)
        return completed
    }

    suspend fun claimQuest(questId: String): Quest? {
        val quest = repository.observeQuests().first().find { it.id == questId } ?: return null
        if (quest.status != QuestStatus.COMPLETED) return null
        val claimed = quest.copy(status = QuestStatus.CLAIMED)
        repository.saveQuest(claimed)
        return claimed
    }
}
