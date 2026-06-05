package com.phoenixforge.classroom.teacher.ui.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.classroom.teacher.data.students.StudentBehaviorSignalsSnapshot
import com.phoenixforge.classroom.teacher.data.students.StudentProgressSnapshot
import com.phoenixforge.classroom.teacher.data.students.StudentProfileSnapshot
import com.phoenixforge.classroom.teacher.data.students.StudentStoryFragmentSnapshot
import com.phoenixforge.classroom.teacher.data.students.StudentSyncReader
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class StudentSnapshotUiState(
    val profile: StudentProfileSnapshot? = null,
    val progress: StudentProgressSnapshot? = null,
    val behaviorSignals: StudentBehaviorSignalsSnapshot? = null,
    val emotionalCheckIns: List<StudentStoryFragmentSnapshot> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class StudentSnapshotViewModel @Inject constructor(
    private val reader: StudentSyncReader
) : ViewModel() {

    private val _state = MutableStateFlow(StudentSnapshotUiState())
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val error = runCatching {
                val profile = reader.readProfileSnapshot()
                val progress = reader.readProgress()
                val behaviorSignals = reader.readBehaviorSignals()
                val checkIns = reader.readStoryFragments()

                _state.value = StudentSnapshotUiState(
                    profile = profile,
                    progress = progress,
                    behaviorSignals = behaviorSignals,
                    emotionalCheckIns = checkIns,
                    isLoading = false,
                    errorMessage = null
                )
            }.exceptionOrNull()

            if (error != null) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = error.message ?: "Student snapshot read failed."
                )
            }
        }
    }
}

