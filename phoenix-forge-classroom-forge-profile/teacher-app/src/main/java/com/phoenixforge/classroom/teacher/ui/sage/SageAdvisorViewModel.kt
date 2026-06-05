package com.phoenixforge.classroom.teacher.ui.sage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.classroom.teacher.data.network.NetworkGate
import com.phoenixforge.classroom.teacher.data.security.SecureCredentialStore
import com.phoenixforge.classroom.teacher.domain.sage.SageChatMessage
import com.phoenixforge.classroom.teacher.domain.sage.SageChatResult
import com.phoenixforge.classroom.teacher.domain.sage.SageChatService
import com.phoenixforge.classroom.teacher.domain.sage.SagePersona
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SageUiMessage(val role: String, val content: String)

data class SageAdvisorUiState(
    val messages: List<SageUiMessage> = emptyList(),
    val input: String = "",
    val isSending: Boolean = false,
    val error: String? = null,
    val isOnline: Boolean = false,
    val hasCredentials: Boolean = false,
    val showSettings: Boolean = false,
    val apiKeyInput: String = "",
    val providerUrlInput: String = SecureCredentialStore.DEFAULT_PROVIDER_URL,
    val modelIdInput: String = SecureCredentialStore.DEFAULT_MODEL
)

@HiltViewModel
class SageAdvisorViewModel @Inject constructor(
    private val chatService: SageChatService,
    private val credentials: SecureCredentialStore,
    private val networkGate: NetworkGate
) : ViewModel() {

    private val _state = MutableStateFlow(SageAdvisorUiState())
    val state: StateFlow<SageAdvisorUiState> = _state.asStateFlow()

    init {
        refreshConnectivity()
        _state.update {
            it.copy(
                hasCredentials = credentials.hasApiKey(),
                providerUrlInput = credentials.getProviderUrl(),
                modelIdInput = credentials.getModelId(),
                messages = listOf(
                    SageUiMessage(
                        "assistant",
                        "I'm ${SagePersona.DISPLAY_NAME}. When you're online with an API key configured, " +
                            "I can help with monthly eval and lesson planning using your full Curriculum Of Life catalog."
                    )
                )
            )
        }
    }

    fun refreshConnectivity() {
        _state.update { it.copy(isOnline = networkGate.isOnline()) }
    }

    fun updateInput(value: String) {
        _state.update { it.copy(input = value, error = null) }
    }

    fun toggleSettings() {
        _state.update { it.copy(showSettings = !it.showSettings) }
    }

    fun updateApiKeyInput(value: String) {
        _state.update { it.copy(apiKeyInput = value) }
    }

    fun updateProviderUrl(value: String) {
        _state.update { it.copy(providerUrlInput = value) }
    }

    fun updateModelId(value: String) {
        _state.update { it.copy(modelIdInput = value) }
    }

    fun saveCredentials() {
        val key = _state.value.apiKeyInput.trim()
        if (key.isNotBlank()) credentials.setApiKey(key)
        credentials.setProviderUrl(_state.value.providerUrlInput.trim())
        credentials.setModelId(_state.value.modelIdInput.trim())
        _state.update {
            it.copy(
                hasCredentials = credentials.hasApiKey(),
                apiKeyInput = "",
                showSettings = false,
                error = null
            )
        }
    }

    fun clearCredentials() {
        credentials.clearApiKey()
        _state.update { it.copy(hasCredentials = false, apiKeyInput = "") }
    }

    fun send() {
        val text = _state.value.input.trim()
        if (text.isBlank() || _state.value.isSending) return
        refreshConnectivity()

        val userMsg = SageUiMessage("user", text)
        _state.update {
            it.copy(
                messages = it.messages + userMsg,
                input = "",
                isSending = true,
                error = null
            )
        }

        viewModelScope.launch {
            val history = _state.value.messages.dropLast(1).map { SageChatMessage(it.role, it.content) }
            when (val result = chatService.send(history, text)) {
                is SageChatResult.Success -> _state.update {
                    it.copy(
                        messages = it.messages + SageUiMessage("assistant", result.reply),
                        isSending = false
                    )
                }
                is SageChatResult.Error -> _state.update {
                    it.copy(isSending = false, error = result.message)
                }
            }
        }
    }
}
