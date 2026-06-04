package com.phoenixforge.student.domain.vault

import com.phoenixforge.student.domain.model.LifeChapter
import com.phoenixforge.student.domain.model.MemoryArtifact
import com.phoenixforge.student.domain.model.StudentProgress
import com.phoenixforge.student.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

data class VaultChapter(
    val chapter: LifeChapter,
    val memories: List<MemoryArtifact>,
    val sealedCount: Int
)

@Singleton
class MemoryVault @Inject constructor(
    private val repository: StudentRepository
) {
    fun observeAllMemories(): Flow<List<MemoryArtifact>> = repository.observeMemories()

    fun observeChapters(): Flow<List<VaultChapter>> =
        observeAllMemories().map { memories ->
            LifeChapter.entries.map { chapter ->
                val chapterMemories = memories.filter { it.chapter == chapter }
                VaultChapter(
                    chapter = chapter,
                    memories = chapterMemories.sortedByDescending { it.capturedAtEpochMillis },
                    sealedCount = chapterMemories.count { it.isSealed && !isUnsealed(it) }
                )
            }.filter { it.memories.isNotEmpty() || it.chapter == LifeChapter.EARLY_EXPLORER }
        }

    fun chapterForLevel(level: Int): LifeChapter = when {
        level >= LifeChapter.MASTERY_ARC.minLevel -> LifeChapter.MASTERY_ARC
        level >= LifeChapter.SKILL_GROWTH.minLevel -> LifeChapter.SKILL_GROWTH
        level >= LifeChapter.DISCOVERY_PHASE.minLevel -> LifeChapter.DISCOVERY_PHASE
        else -> LifeChapter.EARLY_EXPLORER
    }

    suspend fun sealMemory(memory: MemoryArtifact, unlockAtEpochMillis: Long) {
        repository.saveMemory(
            memory.copy(isSealed = true, sealedUntilEpochMillis = unlockAtEpochMillis)
        )
    }

    fun isUnsealed(memory: MemoryArtifact): Boolean {
        if (!memory.isSealed) return true
        val unlockAt = memory.sealedUntilEpochMillis ?: return true
        return System.currentTimeMillis() >= unlockAt
    }

    fun visibleMemories(memories: List<MemoryArtifact>): List<MemoryArtifact> =
        memories.filter { isUnsealed(it) }

    fun chapterForProgress(progress: StudentProgress): LifeChapter = chapterForLevel(progress.level)
}
