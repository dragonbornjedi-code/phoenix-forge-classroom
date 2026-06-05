package com.phoenixforge.classroom.teacher.ui.curriculum

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomain
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainId
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumRepository
import com.phoenixforge.classroom.teacher.domain.curriculum.StarterLesson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class CurriculumDomainUiState(
    val domain: CurriculumDomain? = null,
    val lessons: List<StarterLesson> = emptyList()
)

@HiltViewModel
class CurriculumDomainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    curriculumRepository: CurriculumRepository
) : ViewModel() {

    private val domainId: CurriculumDomainId = CurriculumDomainId.fromId(
        savedStateHandle["domainId"]
    ) ?: CurriculumDomainId.COGNITIVE_ACADEMIC

    private val _state = MutableStateFlow(
        CurriculumDomainUiState(
            domain = curriculumRepository.domain(domainId),
            lessons = curriculumRepository.lessonsForDomain(domainId)
        )
    )
    val state: StateFlow<CurriculumDomainUiState> = _state.asStateFlow()
}
