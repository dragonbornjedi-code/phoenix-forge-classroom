package com.phoenixforge.classroom.teacher.ui.sage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.classroom.teacher.data.network.NetworkGate
import com.phoenixforge.classroom.teacher.data.security.SecureCredentialStore
import com.phoenixforge.classroom.teacher.domain.sage.AiProviderCatalog
import com.phoenixforge.classroom.teacher.domain.sage.AiProviderPreset
import com.phoenixforge.classroom.teacher.domain.sage.SageChatMessage
import com.phoenixforge.classroom.teacher.domain.sage.SageChatResult
import com.phoenixforge.classroom.teacher.domain.sage.SageChatService
import com.phoenixforge.classroom.teacher.domain.sage.SageExpeditionApplier
import com.phoenixforge.classroom.teacher.domain.sage.SagePersona
import com.phoenixforge.classroom.teacher.domain.sage.SageTileAction
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SageUiMessage(
    val id: String = UUID.randomUUID().toString(),
    val role: String,
    val content: String,
    val pendingActions: List<SageTileAction> = emptyList(),
    val applyMessage: String? = null,
    val actionsApplied: Boolean = false,
    val createdTileIds: List<String> = emptyList(),
)

data class SageAdvisorUiState(
    val messages: List<SageUiMessage> = emptyList(),
    val input: String = "",
    val isSending: Boolean = false,
    val isApplying: Boolean = false,
    val error: String? = null,
    val isOnline: Boolean = false,
    val hasCredentials: Boolean = false,
    val showSettings: Boolean = false,
    val apiKeyInput: String = "",
    val selectedProviderId: String = AiProviderCatalog.defaultProvider().id,
    val selectedModelId: String = AiProviderCatalog.defaultProvider().freeTierModels.first(),
    val providerExpanded: Boolean = false,
    val modelExpanded: Boolean = false,
)

@HiltViewModel
class SageAdvisorViewModel @Inject constructor(
    private val chatService: SageChatService,
    private val credentials: SecureCredentialStore,
    private val networkGate: NetworkGate,
    private val expeditionApplier: SageExpeditionApplier,
) : ViewModel() {

    private val _state = MutableStateFlow(SageAdvisorUiState())
    val state: StateFlow<SageAdvisorUiState> = _state.asStateFlow()

    init {
        refreshConnectivity()
        val savedUrl = credentials.getProviderUrl()
        val provider = AiProviderCatalog.findByUrl(savedUrl) ?: AiProviderCatalog.defaultProvider()
        val savedModel = credentials.getModelId()
        val model = provider.freeTierModels.firstOrNull { it == savedModel }
            ?: provider.freeTierModels.first()
        _state.update {
            it.copy(
                hasCredentials = credentials.hasApiKey(),
                selectedProviderId = provider.id,
                selectedModelId = model,
                messages = listOf(
                    SageUiMessage(
                        role = "assistant",
                        content = "I'm ${SagePersona.DISPLAY_NAME}. I draft quests for your Expedition Board. " +
                            "Long-press any of my replies to copy, read aloud, or create an expedition tile. " +
                            "Ask me to brainstorm a quest, then add it when you're ready.",
                    ),
                ),
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

    fun toggleProviderExpanded() {
        _state.update { it.copy(providerExpanded = !it.providerExpanded, modelExpanded = false) }
    }

    fun toggleModelExpanded() {
        _state.update { it.copy(modelExpanded = !it.modelExpanded, providerExpanded = false) }
    }

    fun selectProvider(provider: AiProviderPreset) {
        _state.update {
            it.copy(
                selectedProviderId = provider.id,
                selectedModelId = provider.freeTierModels.first(),
                providerExpanded = false,
                modelExpanded = false,
            )
        }
    }

    fun selectModel(modelId: String) {
        _state.update { it.copy(selectedModelId = modelId, modelExpanded = false) }
    }

    fun selectedProvider(): AiProviderPreset =
        AiProviderCatalog.providers.firstOrNull { it.id == _state.value.selectedProviderId }
            ?: AiProviderCatalog.defaultProvider()

    fun saveCredentials() {
        val key = _state.value.apiKeyInput.trim()
        val provider = selectedProvider()
        if (key.isNotBlank()) credentials.setApiKey(key)
        credentials.setProviderUrl(provider.baseUrl)
        credentials.setModelId(_state.value.selectedModelId)
        _state.update {
            it.copy(
                hasCredentials = credentials.hasApiKey(),
                apiKeyInput = "",
                showSettings = false,
                error = null,
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

        val userMsg = SageUiMessage(role = "user", content = text)
        _state.update {
            it.copy(
                messages = it.messages + userMsg,
                input = "",
                isSending = true,
                error = null,
            )
        }

        viewModelScope.launch {
            val history = _state.value.messages.dropLast(1).map { SageChatMessage(it.role, it.content) }
            when (val result = chatService.send(history, text)) {
                is SageChatResult.Success -> _state.update {
                    it.copy(
                        messages = it.messages + SageUiMessage(
                            role = "assistant",
                            content = result.displayText,
                            pendingActions = result.actions,
                        ),
                        isSending = false,
                    )
                }
                is SageChatResult.Error -> _state.update {
                    it.copy(isSending = false, error = result.message)
                }
            }
        }
    }

    fun applyActions(messageId: String) {
        val message = _state.value.messages.firstOrNull { it.id == messageId } ?: return
        if (message.pendingActions.isEmpty() || message.actionsApplied || _state.value.isApplying) return

        viewModelScope.launch {
            _state.update { it.copy(isApplying = true, error = null) }
            val result = expeditionApplier.apply(message.pendingActions)
            _state.update { state ->
                state.copy(
                    isApplying = false,
                    messages = state.messages.map { msg ->
                        if (msg.id == messageId) {
                            msg.copy(
                                actionsApplied = true,
                                applyMessage = result.message,
                                createdTileIds = result.createdTileIds,
                            )
                        } else {
                            msg
                        }
                    },
                )
            }
        }
    }
}
