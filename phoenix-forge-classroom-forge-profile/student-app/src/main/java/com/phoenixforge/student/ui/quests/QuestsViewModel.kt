package com.phoenixforge.student.ui.quests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.domain.engine.LifeEventCollector
import com.phoenixforge.student.domain.engine.QuestEngine
import com.phoenixforge.student.domain.model.Quest
import com.phoenixforge.student.domain.model.WorldEventResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestsViewModel @Inject constructor(
    questEngine: QuestEngine,
    private val lifeEventCollector: LifeEventCollector
) : ViewModel() {

    val quests: StateFlow<List<Quest>> = questEngine.observeQuests()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _lastResult = MutableStateFlow<WorldEventResult?>(null)
    val lastResult: StateFlow<WorldEventResult?> = _lastResult.asStateFlow()

    fun completeQuest(quest: Quest) {
        viewModelScope.launch {
            _lastResult.value = lifeEventCollector.onQuestCompleted(quest)
        }
    }
}
