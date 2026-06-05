package com.phoenixforge.student.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.domain.engine.NPCEngine
import com.phoenixforge.student.domain.engine.QuestEngine
import com.phoenixforge.student.domain.house.StudentHouse
import com.phoenixforge.student.domain.model.HouseState
import com.phoenixforge.student.domain.model.StudentProgress
import com.phoenixforge.student.domain.model.StudentWorldState
import com.phoenixforge.student.domain.model.WorldState
import com.phoenixforge.student.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    studentHouse: StudentHouse,
    questEngine: QuestEngine,
    npcEngine: NPCEngine,
    repository: StudentRepository
) : ViewModel() {

    val worldState: StateFlow<StudentWorldState> = combine(
        combine(
            studentHouse.observeHouse(),
            repository.observeProgress(),
            repository.observeLatestStory()
        ) { house, progress, story -> Triple(house, progress, story) },
        combine(
            npcEngine.observeCompanion(),
            questEngine.observeActiveQuests(),
            repository.observeRecentMemories(5),
            repository.observeWorldMeta()
        ) { companion, quests, memories, meta ->
            Quad(companion, quests, memories, meta)
        }
    ) { core, activity ->
        val (house, progress, story) = core
        val (companion, quests, memories, meta) = activity
        StudentWorldState(
            house = house,
            progress = progress,
            world = WorldState(
                level = progress.level,
                xp = progress.xp,
                streakDays = progress.streakDays,
                unlockedRooms = house.unlockedRoomTypes.map { it.name },
                activeNPCs = emptyList(),
                lastActivity = meta.first,
                questActive = quests.isNotEmpty(),
                environmentTheme = meta.second,
                drift = meta.third
            ),
            activeCompanion = companion,
            activeQuests = quests.take(3),
            recentMemories = memories,
            latestStory = story
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        StudentWorldState(
            house = HouseState(emptyList(), emptySet(), emptyList()),
            progress = StudentProgress(0, 1, 0, 0, emptySet(), emptySet()),
            world = WorldState(1, 0, 0, emptyList(), emptyList(), null, false),
            activeCompanion = null,
            activeQuests = emptyList(),
            recentMemories = emptyList(),
            latestStory = null
        )
    )

    private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
