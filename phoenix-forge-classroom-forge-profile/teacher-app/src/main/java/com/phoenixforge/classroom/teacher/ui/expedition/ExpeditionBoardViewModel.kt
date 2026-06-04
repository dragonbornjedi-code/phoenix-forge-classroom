package com.phoenixforge.classroom.teacher.ui.expedition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.classroom.teacher.data.repository.TileRepository
import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpeditionBoardViewModel @Inject constructor(
    private val repo: TileRepository
) : ViewModel() {

    val tiles = repo.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _showSheet = MutableStateFlow(false)
    val showSheet = _showSheet.asStateFlow()

    init {
        viewModelScope.launch { repo.ensureSeedData() }
    }

    fun openSheet() { _showSheet.value = true }
    fun closeSheet() { _showSheet.value = false }

    fun createTile(title: String, description: String, domain: ForgeDomain) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repo.save(
                IntentTile(
                    title = title.trim(),
                    description = description.trim(),
                    domain = domain.name
                )
            )
            _showSheet.value = false
        }
    }

    fun deleteTile(id: String) {
        viewModelScope.launch { repo.delete(id) }
    }
}
