package com.phoenixforge.student.ui.npc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.domain.engine.NPCEngine
import com.phoenixforge.student.domain.model.HouseRoomType
import com.phoenixforge.student.domain.model.NpcState
import com.phoenixforge.student.domain.model.NpcType
import com.phoenixforge.student.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class CompanionsHubState(
    val companion: NpcState? = null,
    val whisps: List<NpcState> = emptyList(),
    val pets: List<NpcState> = emptyList(),
    val petSpaceUnlocked: Boolean = false,
    val level: Int = 1,
    val sparkPhaseLabel: String = "Playful Helper (ages 5–7)"
)

@HiltViewModel
class NpcRoomViewModel @Inject constructor(
    npcEngine: NPCEngine,
    repository: StudentRepository
) : ViewModel() {

    val hubState: StateFlow<CompanionsHubState> = combine(
        npcEngine.observeNpcs(),
        repository.observeProgress(),
        repository.observeHouse()
    ) { npcs, progress, house ->
        CompanionsHubState(
            companion = npcs.firstOrNull { it.type == NpcType.COMPANION && it.isUnlocked },
            whisps = npcs.filter { it.type == NpcType.WHISP },
            pets = npcs.filter { it.type == NpcType.PET && it.isUnlocked },
            petSpaceUnlocked = progress.level >= HouseRoomType.PET_SPACE.unlockLevel ||
                house.unlockedRoomTypes.contains(HouseRoomType.PET_SPACE),
            level = progress.level,
            sparkPhaseLabel = sparkPhaseForLevel(progress.level)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CompanionsHubState())
}

private fun sparkPhaseForLevel(level: Int): String = when {
    level < 3 -> "Playful Helper (ages 5–7)"
    level < 6 -> "Curious Partner (ages 8–10)"
    level < 10 -> "Reflective Friend (ages 11–13)"
    else -> "Intellectual Peer (ages 14–18)"
}
