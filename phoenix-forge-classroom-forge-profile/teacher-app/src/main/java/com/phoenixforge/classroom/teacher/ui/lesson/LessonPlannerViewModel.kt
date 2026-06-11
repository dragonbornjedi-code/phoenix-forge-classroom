package com.phoenixforge.classroom.teacher.ui.lesson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.classroom.teacher.data.repository.TileRepository
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainCatalog
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainId
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumSubdomain
import com.phoenixforge.classroom.teacher.domain.lesson.LessonPlanDraft
import com.phoenixforge.classroom.teacher.domain.lesson.LessonPlannerEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LessonPlannerUiState(
    val domains: List<com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomain> =
        CurriculumDomainCatalog.domains,
    val selectedDomainId: CurriculumDomainId = CurriculumDomainId.COGNITIVE_ACADEMIC,
    val subdomains: List<CurriculumSubdomain> = emptyList(),
    val selectedSubdomainId: String? = null,
    val draft: LessonPlanDraft? = null,
    val tileCreatedMessage: String? = null,
    val createdTileId: String? = null,
    val isSaving: Boolean = false
)

@HiltViewModel
class LessonPlannerViewModel @Inject constructor(
    private val plannerEngine: LessonPlannerEngine,
    private val tileRepository: TileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LessonPlannerUiState())
    val state: StateFlow<LessonPlannerUiState> = _state.asStateFlow()

    init {
        selectDomain(CurriculumDomainId.COGNITIVE_ACADEMIC)
    }

    fun selectDomain(domainId: CurriculumDomainId) {
        val subs = plannerEngine.subdomainOptions(domainId)
        _state.update {
            it.copy(
                selectedDomainId = domainId,
                subdomains = subs,
                selectedSubdomainId = subs.firstOrNull()?.id,
                draft = subs.firstOrNull()?.let { sub -> plannerEngine.generatePlan(domainId, sub.id) }
            )
        }
    }

    fun selectSubdomain(subdomainId: String) {
        val domainId = _state.value.selectedDomainId
        _state.update {
            it.copy(
                selectedSubdomainId = subdomainId,
                draft = plannerEngine.generatePlan(domainId, subdomainId)
            )
        }
    }

    fun addToExpeditionBoard() {
        val plan = _state.value.draft ?: return
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val tile = tileRepository.createFromLessonPlan(plan)
            _state.update {
                it.copy(
                    isSaving = false,
                    tileCreatedMessage = "\"${plan.title}\" added to Expedition Board.",
                    createdTileId = tile.id,
                )
            }
        }
    }

    fun dismissMessage() {
        _state.update { it.copy(tileCreatedMessage = null, createdTileId = null) }
    }
}
