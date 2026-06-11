package com.phoenixforge.student.ui.garden

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.domain.progression.GardenMerchantCatalog
import com.phoenixforge.student.domain.progression.ProgressionEngine
import com.phoenixforge.student.domain.repository.StudentRepository
import com.phoenixforge.student.sync.DigitalHomeWire
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

data class GardenMerchantUiState(
    val tokens: Int = 0,
    val ownedItemIds: Set<String> = emptySet(),
    val statusMessage: String? = null,
    val purchasing: Boolean = false,
)

@HiltViewModel
class GardenMerchantViewModel @Inject constructor(
    repository: StudentRepository,
    private val progressionEngine: ProgressionEngine,
) : ViewModel() {

    private val _state = MutableStateFlow(GardenMerchantUiState())
    val state = _state.asStateFlow()

    val catalog = GardenMerchantCatalog.items

    init {
        viewModelScope.launch {
            combine(
                repository.observeProgress(),
                repository.observeHouse(),
            ) { progress, house ->
                progress to house
            }.collect { (progress, house) ->
                _state.value = _state.value.copy(
                    tokens = progress.currency[DigitalHomeWire.CURRENCY_FORGE_TOKENS] ?: 0,
                    ownedItemIds = house.inventory.map { it.itemId }.toSet(),
                )
            }
        }
    }

    fun purchase(itemId: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(purchasing = true, statusMessage = null)
            val result = progressionEngine.purchaseGardenItem(itemId)
            _state.value = _state.value.copy(
                purchasing = false,
                statusMessage = result.fold(
                    onSuccess = { it.message },
                    onFailure = { it.message ?: "Purchase failed." },
                ),
            )
        }
    }
}
