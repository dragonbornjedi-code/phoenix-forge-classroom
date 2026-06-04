package com.phoenixforge.student.ui.vault

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.domain.vault.MemoryVault
import com.phoenixforge.student.domain.vault.VaultChapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MemoryVaultViewModel @Inject constructor(
    memoryVault: MemoryVault
) : ViewModel() {

    val chapters: StateFlow<List<VaultChapter>> = memoryVault.observeChapters()
        .map { chapters -> chapters.map { chapter -> chapter.copy(memories = memoryVault.visibleMemories(chapter.memories)) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
