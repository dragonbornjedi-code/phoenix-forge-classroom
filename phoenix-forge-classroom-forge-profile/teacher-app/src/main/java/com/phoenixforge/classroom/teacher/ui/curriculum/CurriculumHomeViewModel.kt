package com.phoenixforge.classroom.teacher.ui.curriculum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.classroom.teacher.data.repository.TileRepository
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomain
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumRepository
import com.phoenixforge.classroom.teacher.domain.curriculum.StarterLesson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CurriculumHomeState(
    val domains: List<CurriculumDomain> = emptyList(),
    val designRules: List<String> = emptyList(),
    val pack01Lessons: List<StarterLesson> = emptyList(),
    val pack01Imported: Boolean = false,
    val importMessage: String? = null
)

@HiltViewModel
class CurriculumHomeViewModel @Inject constructor(
    private val curriculumRepository: CurriculumRepository,
    private val tileRepository: TileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CurriculumHomeState())
    val state: StateFlow<CurriculumHomeState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val imported = tileRepository.hasPack01Tiles()
            _state.value = CurriculumHomeState(
                domains = curriculumRepository.allDomains(),
                designRules = curriculumRepository.designRules(),
                pack01Lessons = curriculumRepository.pack01Lessons(),
                pack01Imported = imported
            )
        }
    }

    fun importPack01() {
        viewModelScope.launch {
            val count = tileRepository.importPack01IfEmpty()
            _state.value = _state.value.copy(
                pack01Imported = count > 0 || _state.value.pack01Imported,
                importMessage = when {
                    count > 0 -> "Added $count starter lessons to Expedition Board."
                    _state.value.pack01Imported -> "Pack 01 is already on your board."
                    else -> "Pack 01 tiles already exist."
                }
            )
        }
    }

    fun dismissImportMessage() {
        _state.value = _state.value.copy(importMessage = null)
    }
}
