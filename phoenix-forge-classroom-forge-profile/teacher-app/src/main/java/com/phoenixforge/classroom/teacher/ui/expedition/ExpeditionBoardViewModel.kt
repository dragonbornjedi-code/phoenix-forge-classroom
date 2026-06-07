package com.phoenixforge.classroom.teacher.ui.expedition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.classroom.teacher.data.repository.TileRepository
import com.phoenixforge.classroom.teacher.domain.chariot.ChariotExport
import com.phoenixforge.classroom.teacher.domain.daystart.StartDayExport
import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpeditionBoardViewModel @Inject constructor(
    private val repo: TileRepository
) : ViewModel() {

    val tiles = repo.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _filter = MutableStateFlow(ExpeditionBoardFilter.ALL)
    val filter = _filter.asStateFlow()

    val visibleTiles =
        combine(tiles, _filter) { list, boardFilter ->
            filterExpeditionTiles(list, boardFilter)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _showSheet = MutableStateFlow(false)
    val showSheet = _showSheet.asStateFlow()

    private val _showStartDayExport = MutableStateFlow(false)
    val showStartDayExport = _showStartDayExport.asStateFlow()

    private val _showChariotExport = MutableStateFlow(false)
    val showChariotExport = _showChariotExport.asStateFlow()

    val startDayExportText =
        tiles
            .map { StartDayExport.build(it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val chariotExportText =
        tiles
            .map { ChariotExport.buildExportText(ChariotExport.build(it)) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    val chariotExportJson =
        tiles
            .map { ChariotExport.build(it).toJson() }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), "")

    fun setFilter(filter: ExpeditionBoardFilter) {
        _filter.value = filter
    }

    init {
        viewModelScope.launch { repo.ensureSeedData() }
    }

    fun openSheet() { _showSheet.value = true }
    fun closeSheet() { _showSheet.value = false }

    fun openStartDayExport() { _showStartDayExport.value = true }
    fun closeStartDayExport() { _showStartDayExport.value = false }

    fun openChariotExport() { _showChariotExport.value = true }
    fun closeChariotExport() { _showChariotExport.value = false }

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

    fun moveTile(fromIndex: Int, toIndex: Int) {
        if (_filter.value != ExpeditionBoardFilter.ALL) return
        viewModelScope.launch {
            val current = repo.observeAll().first()
            val reordered = applyTileReorder(current, fromIndex, toIndex)
            repo.persistSortOrder(reordered)
        }
    }
}
