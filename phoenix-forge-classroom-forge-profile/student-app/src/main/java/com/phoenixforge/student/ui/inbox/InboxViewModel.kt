package com.phoenixforge.student.ui.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.data.forgeimport.ProfileMessageReader
import com.phoenixforge.student.domain.model.StudentInboxMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class InboxUiState(
    val messages: List<StudentInboxMessage> = emptyList(),
    val isLoading: Boolean = true,
    val status: String? = null,
)

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val profileMessageReader: ProfileMessageReader,
) : ViewModel() {

    private val _state = MutableStateFlow(InboxUiState())
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, status = null)
            val messages = profileMessageReader.readStudentMessages()
                .filter { it.direction == "TO_STUDENT" }
            _state.value = InboxUiState(
                messages = messages,
                isLoading = false,
                status = if (messages.isEmpty()) {
                    "No messages yet. Ask a grown-up to send one from Teacher Edition."
                } else {
                    null
                },
            )
        }
    }
}
