package com.phoenixforge.student.ui.importprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.data.forgeimport.ForgeProfileImporter
import com.phoenixforge.student.data.forgeimport.ForgeProfilePreview
import com.phoenixforge.student.domain.engine.LifeEventCollector
import com.phoenixforge.student.domain.model.WorldEventResult
import com.phoenixforge.student.domain.session.StudentSessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ImportUiState(
    val preview: ForgeProfilePreview? = null,
    val lastReward: WorldEventResult? = null,
    val isImporting: Boolean = false
)

@HiltViewModel
class ImportForgeProfileViewModel @Inject constructor(
    private val importer: ForgeProfileImporter,
    private val lifeEventCollector: LifeEventCollector,
    private val sessionStore: StudentSessionStore,
) : ViewModel() {

    private val _state = MutableStateFlow(ImportUiState())
    val state: StateFlow<ImportUiState> = _state.asStateFlow()

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
                sessionStore.setActiveImportUid(snapshot.uid)
                val grant = lifeEventCollector.onForgeProfileImported(snapshot)
                _state.value = _state.value.copy(lastReward = grant, isImporting = false)
            } else {
                refreshPreview()
                _state.value = _state.value.copy(isImporting = false)
            }
        }
    }
}
