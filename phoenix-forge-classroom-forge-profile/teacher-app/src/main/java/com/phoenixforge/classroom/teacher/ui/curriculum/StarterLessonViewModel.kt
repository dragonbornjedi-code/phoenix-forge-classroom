package com.phoenixforge.classroom.teacher.ui.curriculum

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.classroom.teacher.data.repository.TileRepository
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumRepository
import com.phoenixforge.classroom.teacher.domain.curriculum.StarterLesson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StarterLessonUiState(
    val lesson: StarterLesson? = null,
    val tileCreated: Boolean = false,
    val tileCreatedMessage: String? = null
)

@HiltViewModel
class StarterLessonViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val curriculumRepository: CurriculumRepository,
    private val tileRepository: TileRepository
) : ViewModel() {

    private val lessonId: String = checkNotNull(savedStateHandle["lessonId"])

    private val _state = MutableStateFlow(StarterLessonUiState())
    val state: StateFlow<StarterLessonUiState> = _state.asStateFlow()

    init {
        _state.value = StarterLessonUiState(lesson = curriculumRepository.starterLesson(lessonId))
    }

    fun addToExpeditionBoard() {
        val lesson = _state.value.lesson ?: return
        viewModelScope.launch {
            tileRepository.createFromStarterLesson(lesson)
            _state.value = _state.value.copy(
                tileCreated = true,
                tileCreatedMessage = "\"${lesson.title}\" added to Expedition Board."
            )
        }
    }

    fun dismissMessage() {
        _state.value = _state.value.copy(tileCreatedMessage = null)
    }
}
