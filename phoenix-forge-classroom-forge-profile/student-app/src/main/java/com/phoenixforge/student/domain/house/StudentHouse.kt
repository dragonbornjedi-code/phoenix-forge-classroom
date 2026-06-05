package com.phoenixforge.student.domain.house

import com.phoenixforge.student.domain.model.HouseRoomType
import com.phoenixforge.student.domain.model.HouseState
import com.phoenixforge.student.domain.model.RoomNode
import com.phoenixforge.student.domain.model.StudentProgress
import com.phoenixforge.student.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentHouse @Inject constructor(
    private val repository: StudentRepository
) {
    fun observeHouse(): Flow<HouseState> = repository.observeHouse()

    suspend fun initializeDefaultHouse() {
        val unlocked = setOf(HouseRoomType.BEDROOM, HouseRoomType.QUEST_BOARD)
        repository.saveHouse(
            HouseState(
                rooms = HouseRoomType.entries.map { RoomNode(it, it in unlocked) },
                unlockedRoomTypes = unlocked,
                decorations = emptyList()
            )
        )
    }

    suspend fun syncUnlocksWithProgress(progress: StudentProgress) {
        val current = repository.observeHouse().first()
        val unlocked = HouseRoomType.entries
            .filter { progress.level >= it.unlockLevel }
            .toSet()
        val merged = current.unlockedRoomTypes + unlocked
        repository.saveHouse(
            current.copy(
                rooms = HouseRoomType.entries.map { type ->
                    RoomNode(
                        type = type,
                        isUnlocked = type in merged,
                        decorationIds = current.decorations.filter { it.startsWith(type.name) }
                    )
                },
                unlockedRoomTypes = merged
            )
        )
    }

    suspend fun addDecoration(decorationId: String) {
        val current = repository.observeHouse().first()
        if (decorationId !in current.decorations) {
            repository.saveHouse(current.copy(decorations = current.decorations + decorationId))
        }
    }

    fun observeUnlockedRoomCount(): Flow<Int> =
        observeHouse().map { state -> state.unlockedRoomTypes.size }
}
