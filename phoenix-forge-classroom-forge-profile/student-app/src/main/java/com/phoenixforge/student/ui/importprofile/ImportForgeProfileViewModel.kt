package com.phoenixforge.student.ui.importprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.data.forgeimport.ForgeProfileImporter
import com.phoenixforge.student.data.forgeimport.ForgeProfilePreview
import com.phoenixforge.student.domain.engine.LifeEventCollector
import com.phoenixforge.student.domain.model.ImportedProfileSnapshot
import com.phoenixforge.student.domain.model.WorldEventResult
import com.phoenixforge.student.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ImportUiState(
    val preview: ForgeProfilePreview? = null,
    val imports: List<ImportedProfileSnapshot> = emptyList(),
    val lastReward: WorldEventResult? = null,
    val isImporting: Boolean = false
)

@HiltViewModel
class ImportForgeProfileViewModel @Inject constructor(
    private val importer: ForgeProfileImporter,
    private val lifeEventCollector: LifeEventCollector,
    repository: StudentRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ImportUiState())
    val state: StateFlow<ImportUiState> = _state.asStateFlow()

    val pastImports: StateFlow<List<ImportedProfileSnapshot>> = repository.observeImportedProfiles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        refreshPreview()
    }

    fun refreshPreview() {
        _state.value = _state.value.copy(preview = importer.probeProfile())
    }

    fun importSelectedProfile() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isImporting = true)
            val snapshot = importer.importSnapshot()
            if (snapshot != null) {
                val grant = lifeEventCollector.onForgeProfileImported(snapshot)
                _state.value = _state.value.copy(lastReward = grant, isImporting = false)
            } else {
                refreshPreview()
                _state.value = _state.value.copy(isImporting = false)
            }
        }
    }
}
