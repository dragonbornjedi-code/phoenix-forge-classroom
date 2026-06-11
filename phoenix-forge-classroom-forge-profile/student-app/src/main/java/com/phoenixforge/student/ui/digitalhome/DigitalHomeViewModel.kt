package com.phoenixforge.student.ui.digitalhome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.domain.model.HouseRoomType
import com.phoenixforge.student.domain.model.NpcType
import com.phoenixforge.student.domain.repository.StudentRepository
import com.phoenixforge.student.sync.DigitalHomeWire
import com.phoenixforge.student.ui.theme.StudentKidCopy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class DigitalZoneCard(
    val room: HouseRoomType,
    val isUnlocked: Boolean,
    val unlockHint: String,
)

data class DigitalHomeUiState(
    val level: Int = 1,
    val xp: Int = 0,
    val tokens: Int = 0,
    val zones: List<DigitalZoneCard> = emptyList(),
    val companionName: String? = null,
    val companionUnlocked: Boolean = false,
    val whispsUnlocked: Int = 0,
    val whispsTotal: Int = 0,
    val petSpaceUnlocked: Boolean = false,
)

@HiltViewModel
class DigitalHomeViewModel @Inject constructor(
    repository: StudentRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DigitalHomeUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.observeProgress(),
                repository.observeHouse(),
                repository.observeNpcs(),
            ) { progress, house, npcs ->
                val tokens = progress.currency[DigitalHomeWire.CURRENCY_FORGE_TOKENS] ?: 0
                val zones = HouseRoomType.entries.map { room ->
                    val unlocked = room in house.unlockedRoomTypes
                    DigitalZoneCard(
                        room = room,
                        isUnlocked = unlocked,
                        unlockHint = if (unlocked) {
                            StudentKidCopy.roomOpen()
                        } else {
                            StudentKidCopy.roomLockedAtLevel(room.unlockLevel)
                        },
                    )
                }
                val companion = npcs.firstOrNull { it.type == NpcType.COMPANION }
                val whisps = npcs.filter { it.type == NpcType.WHISP }
                DigitalHomeUiState(
                    level = progress.level,
                    xp = progress.xp,
                    tokens = tokens,
                    zones = zones,
                    companionName = companion?.name,
                    companionUnlocked = companion?.isUnlocked == true,
                    whispsUnlocked = whisps.count { it.isUnlocked },
                    whispsTotal = whisps.size,
                    petSpaceUnlocked = HouseRoomType.PET_SPACE in house.unlockedRoomTypes,
                )
            }.collect { _state.value = it }
        }
    }
}
