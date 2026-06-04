package com.phoenixforge.profile.ui.memory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.profile.domain.model.MemoryArtifact
import com.phoenixforge.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MemoryState(
    val artifacts: List<MemoryArtifact> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class MemoryViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MemoryState())
    val state: StateFlow<MemoryState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getMemoryArtifacts().collect { list ->
                _state.value = _state.value.copy(artifacts = list)
            }
        }
    }
}
