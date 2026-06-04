package com.phoenixforge.student.ui.npc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.domain.engine.NPCEngine
import com.phoenixforge.student.domain.model.NpcState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NpcRoomViewModel @Inject constructor(
    npcEngine: NPCEngine
) : ViewModel() {

    val npcs: StateFlow<List<NpcState>> = npcEngine.observeNpcs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
