package com.phoenixforge.classroom.teacher.ui.identity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.classroom.teacher.data.forgeprofile.ForgeProfileTeacherReader
import com.phoenixforge.classroom.teacher.data.forgeprofile.TeacherForgeProfileSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeacherIdentityViewModel @Inject constructor(
    private val reader: ForgeProfileTeacherReader
) : ViewModel() {

    private val _snapshot = MutableStateFlow<TeacherForgeProfileSnapshot?>(null)
    val snapshot: StateFlow<TeacherForgeProfileSnapshot?> = _snapshot.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _snapshot.value = reader.readLinkedTeacherProfile()
        }
    }
}
