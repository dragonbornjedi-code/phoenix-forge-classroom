package com.phoenixforge.student.ui.dreams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.domain.model.DreamEntry
import com.phoenixforge.student.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed interface AddDreamStep {
    data object Idle : AddDreamStep
    data class AskSuggestion(val horizon: String) : AddDreamStep
    data class PickCategory(val horizon: String) : AddDreamStep
    data class PickExample(val horizon: String, val categoryId: String) : AddDreamStep
    data class CustomEntry(val horizon: String, val text: String = "") : AddDreamStep
}

data class DreamBoardUiState(
    val dreamsByType: Map<String, List<DreamEntry>> = emptyMap(),
    val addStep: AddDreamStep = AddDreamStep.Idle
)

@HiltViewModel
class DreamBoardViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {
    private val _state = MutableStateFlow(DreamBoardUiState())
    val state: StateFlow<DreamBoardUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeDreamEntries().collect { dreams ->
                _state.value = _state.value.copy(dreamsByType = dreams.groupBy { it.type })
            }
        }
    }

    fun startAddDream(horizon: String) {
        _state.value = _state.value.copy(addStep = AddDreamStep.AskSuggestion(horizon))
    }

    fun dismissAddFlow() {
        _state.value = _state.value.copy(addStep = AddDreamStep.Idle)
    }

    fun onWantSuggestion(wantsSuggestion: Boolean) {
        val horizon = (_state.value.addStep as? AddDreamStep.AskSuggestion)?.horizon ?: return
        _state.value = _state.value.copy(
            addStep = if (wantsSuggestion) AddDreamStep.PickCategory(horizon) else AddDreamStep.CustomEntry(horizon)
        )
    }

    fun selectCategory(categoryId: String) {
        val horizon = (_state.value.addStep as? AddDreamStep.PickCategory)?.horizon ?: return
        _state.value = _state.value.copy(addStep = AddDreamStep.PickExample(horizon, categoryId))
    }

    fun backToCategoryPicker() {
        val step = _state.value.addStep
        if (step is AddDreamStep.PickExample) {
            _state.value = _state.value.copy(addStep = AddDreamStep.PickCategory(step.horizon))
        }
    }

    fun updateCustomDreamText(text: String) {
        val step = _state.value.addStep
        if (step is AddDreamStep.CustomEntry) {
            _state.value = _state.value.copy(addStep = step.copy(text = text))
        }
    }

    fun saveDream(content: String, horizon: String) {
        val trimmed = content.trim()
        if (trimmed.isBlank()) return
        viewModelScope.launch {
            repository.saveDreamEntry(
                DreamEntry(
                    id = UUID.randomUUID().toString(),
                    type = horizon,
                    content = trimmed,
                    timestampEpochMillis = System.currentTimeMillis()
                )
            )
            dismissAddFlow()
        }
    }

    fun saveCustomDream() {
        val step = _state.value.addStep as? AddDreamStep.CustomEntry ?: return
        saveDream(step.text, step.horizon)
    }

    fun saveExample(example: String) {
        val step = _state.value.addStep as? AddDreamStep.PickExample ?: return
        saveDream(example, step.horizon)
    }
}
