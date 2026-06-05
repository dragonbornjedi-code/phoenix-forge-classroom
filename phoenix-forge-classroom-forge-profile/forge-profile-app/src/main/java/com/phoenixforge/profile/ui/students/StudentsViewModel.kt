package com.phoenixforge.profile.ui.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.profile.domain.model.LinkedStudentProfile
import com.phoenixforge.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StudentsUiState(
    val linkedStudents: List<LinkedStudentProfile> = emptyList(),
    val showLinkDialog: Boolean = false,
    val linkName: String = "",
    val linkUid: String = "",
    val linkNotes: String = "",
    val errorMessage: String? = null
)

@HiltViewModel
class StudentsViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(StudentsUiState())
    val state: StateFlow<StudentsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getLinkedStudents().collect { students ->
                _state.value = _state.value.copy(linkedStudents = students)
            }
        }
    }

    fun openLinkDialog() {
        _state.value = _state.value.copy(
            showLinkDialog = true,
            linkName = "",
            linkUid = "",
            linkNotes = "",
            errorMessage = null
        )
    }

    fun dismissLinkDialog() {
        _state.value = _state.value.copy(showLinkDialog = false, errorMessage = null)
    }

    fun updateLinkName(value: String) {
        _state.value = _state.value.copy(linkName = value)
    }

    fun updateLinkUid(value: String) {
        _state.value = _state.value.copy(linkUid = value)
    }

    fun updateLinkNotes(value: String) {
        _state.value = _state.value.copy(linkNotes = value)
    }

    fun saveLink() {
        val name = _state.value.linkName.trim()
        val uid = _state.value.linkUid.trim()
        if (name.isBlank() || uid.isBlank()) {
            _state.value = _state.value.copy(errorMessage = "Name and profile ID are required.")
            return
        }
        viewModelScope.launch {
            repository.linkStudentProfile(name, uid, _state.value.linkNotes.takeIf { it.isNotBlank() })
            dismissLinkDialog()
        }
    }

    fun unlink(profileUid: String) {
        viewModelScope.launch {
            repository.unlinkStudentProfile(profileUid)
        }
    }
}
