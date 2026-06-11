package com.phoenixforge.student.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.domain.model.ImportedProfileSnapshot
import com.phoenixforge.student.domain.repository.StudentRepository
import com.phoenixforge.student.domain.session.StudentSessionStore
import com.phoenixforge.student.sync.MemoryEventImporter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val draftCount: Int = 0,
    val importMessage: String? = null,
    val isImporting: Boolean = false,
    val isSignedIn: Boolean = false,
    val showDeveloperSection: Boolean = false,
    val signOutMessage: String? = null,
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val memoryEventImporter: MemoryEventImporter,
    private val sessionStore: StudentSessionStore,
    repository: StudentRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState(isSignedIn = sessionStore.isSignedIn()))
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    val pastImports: StateFlow<List<ImportedProfileSnapshot>> = repository.observeImportedProfiles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            repository.observeMemoryEventDraftCount().collect { count ->
                _state.value = _state.value.copy(draftCount = count)
            }
        }
    }

    fun importForgeWorldMemories() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isImporting = true, importMessage = null)
            val result = memoryEventImporter.importNewEvents()
            _state.value = _state.value.copy(
                isImporting = false,
                importMessage = result.message,
            )
        }
    }

    fun signOut() {
        sessionStore.signOut()
        _state.value = _state.value.copy(
            isSignedIn = false,
            signOutMessage = "Signed out. Your hearth is waiting — import again when ready.",
        )
    }

    fun toggleDeveloperSection() {
        _state.value = _state.value.copy(showDeveloperSection = !_state.value.showDeveloperSection)
    }
}
