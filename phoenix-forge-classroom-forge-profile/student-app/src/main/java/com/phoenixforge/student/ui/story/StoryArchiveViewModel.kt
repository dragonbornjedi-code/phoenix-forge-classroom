package com.phoenixforge.student.ui.story

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.domain.model.StoryGraphNode
import com.phoenixforge.student.domain.simulation.StoryGraphEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoryArchiveViewModel @Inject constructor(
    private val storyGraphEngine: StoryGraphEngine
) : ViewModel() {

    private val _nodes = MutableStateFlow<List<StoryGraphNode>>(emptyList())
    val nodes: StateFlow<List<StoryGraphNode>> = _nodes.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _nodes.value = storyGraphEngine.buildGraph(50)
        }
    }
}
