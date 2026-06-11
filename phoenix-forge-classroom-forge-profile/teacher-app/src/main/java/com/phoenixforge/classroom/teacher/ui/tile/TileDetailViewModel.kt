package com.phoenixforge.classroom.teacher.ui.tile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.classroom.teacher.data.repository.TileRepository
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.StewardReflection
import com.phoenixforge.classroom.teacher.domain.model.StewardReflectionAxis
import com.phoenixforge.classroom.teacher.domain.model.StewardReflectionCatalog
import com.phoenixforge.classroom.teacher.domain.model.TileStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TileDetailUiState(
    val tile: IntentTile? = null,
    val evidenceNotes: String = "",
    val coachingCues: String = "",
    val materials: String = "",
    val fieldGuideExamples: String = "",
    val fieldGuideSupports: String = "",
    val fieldGuideRecovery: String = "",
    val reflection: StewardReflection = StewardReflection(),
)

@HiltViewModel
class TileDetailViewModel @Inject constructor(
    private val repo: TileRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tileId: String = savedStateHandle.get<String>("tileId").orEmpty()

    private val _state = MutableStateFlow(TileDetailUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val tile = repo.findById(tileId) ?: return@launch
            _state.value = TileDetailUiState(
                tile = tile,
                evidenceNotes = tile.evidenceNotes,
                coachingCues = tile.coachingCues,
                materials = tile.materials,
                fieldGuideExamples = tile.fieldGuideExamples,
                fieldGuideSupports = tile.fieldGuideSupports,
                fieldGuideRecovery = tile.fieldGuideRecovery,
                reflection = StewardReflection.fromTile(tile),
            )
        }
    }

    fun updateEvidence(notes: String) = _state.update { it.copy(evidenceNotes = notes) }
    fun updateCoaching(cues: String) = _state.update { it.copy(coachingCues = cues) }
    fun updateMaterials(materials: String) = _state.update { it.copy(materials = materials) }
    fun updateExamples(examples: String) = _state.update { it.copy(fieldGuideExamples = examples) }
    fun updateSupports(supports: String) = _state.update { it.copy(fieldGuideSupports = supports) }
    fun updateRecovery(recovery: String) = _state.update { it.copy(fieldGuideRecovery = recovery) }

    fun updateReflectionAxis(axis: StewardReflectionAxis, value: Int) {
        _state.update {
            it.copy(
                reflection = StewardReflectionCatalog.withValue(it.reflection, axis, value.coerceIn(0, 100))
            )
        }
    }

    fun saveDetails() {
        val current = _state.value.tile ?: return
        viewModelScope.launch {
            val fields = _state.value.reflection.toTileFields()
            repo.update(
                current.copy(
                    evidenceNotes = _state.value.evidenceNotes,
                    coachingCues = _state.value.coachingCues,
                    materials = _state.value.materials,
                    fieldGuideExamples = _state.value.fieldGuideExamples,
                    fieldGuideSupports = _state.value.fieldGuideSupports,
                    fieldGuideRecovery = _state.value.fieldGuideRecovery,
                    reflectionMental = fields.reflectionMental,
                    reflectionEmotional = fields.reflectionEmotional,
                    reflectionPhysical = fields.reflectionPhysical,
                    reflectionEducational = fields.reflectionEducational,
                    reflectionBehavioral = fields.reflectionBehavioral,
                )
            )
            _state.update { it.copy(tile = repo.findById(tileId)) }
        }
    }

    fun markComplete() {
        viewModelScope.launch {
            val reflection = _state.value.reflection.copy(
                mental = _state.value.reflection.mental ?: StewardReflectionCatalog.NEUTRAL,
                emotional = _state.value.reflection.emotional ?: StewardReflectionCatalog.NEUTRAL,
                physical = _state.value.reflection.physical ?: StewardReflectionCatalog.NEUTRAL,
                educational = _state.value.reflection.educational ?: StewardReflectionCatalog.NEUTRAL,
                behavioral = _state.value.reflection.behavioral ?: StewardReflectionCatalog.NEUTRAL,
            )
            repo.markComplete(tileId, _state.value.evidenceNotes, reflection)
            val updated = repo.findById(tileId)
            _state.update {
                it.copy(
                    tile = updated,
                    reflection = updated?.let(StewardReflection::fromTile) ?: it.reflection,
                )
            }
        }
    }

    fun setStatus(status: TileStatus) {
        val current = _state.value.tile ?: return
        viewModelScope.launch {
            repo.update(current.copy(status = status.name))
            _state.update { it.copy(tile = repo.findById(tileId)) }
        }
    }

    fun setCarFriendly(enabled: Boolean) {
        val current = _state.value.tile ?: return
        viewModelScope.launch {
            repo.update(current.copy(carFriendly = enabled))
            _state.update { it.copy(tile = repo.findById(tileId)) }
        }
    }
}
