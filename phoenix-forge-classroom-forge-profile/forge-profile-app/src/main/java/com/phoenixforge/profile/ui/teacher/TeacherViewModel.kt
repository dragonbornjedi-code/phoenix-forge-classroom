package com.phoenixforge.profile.ui.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.profile.domain.auth.AuthProvider
import com.phoenixforge.profile.domain.auth.AuthResult
import com.phoenixforge.profile.domain.copy.AppBoundaryCopy
import com.phoenixforge.profile.domain.model.TeacherMetadata
import com.phoenixforge.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TeacherState(
    val metadata: List<TeacherMetadata> = emptyList(),
    val isAuthorized: Boolean = false,
    val gateMessage: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val authProvider: AuthProvider
) : ViewModel() {

    private val _state = MutableStateFlow(TeacherState(isAuthorized = authProvider.isAuthorized()))
    val state: StateFlow<TeacherState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getProfile()
                .filterNotNull()
                .flatMapLatest { profile -> repository.getTeacherMetadata(profile.uid) }
                .collectLatest { metadata ->
                    _state.value = _state.value.copy(metadata = metadata)
                }
        }
    }

    fun requestStewardAccess() {
        viewModelScope.launch {
            when (authProvider.requestStewardAccess()) {
                AuthResult.Granted -> _state.value = _state.value.copy(
                    isAuthorized = true,
                    gateMessage = null
                )
                AuthResult.Denied -> _state.value = _state.value.copy(
                    isAuthorized = false,
                    gateMessage = AppBoundaryCopy.GATE_DENIED
                )
                AuthResult.NotConfigured -> _state.value = _state.value.copy(
                    isAuthorized = false,
                    gateMessage = AppBoundaryCopy.GATE_NOT_CONFIGURED
                )
            }
        }
    }

    fun enableStewardOnDevice() {
        viewModelScope.launch {
            when (authProvider.enableStewardOnDevice()) {
                AuthResult.Granted -> _state.value = _state.value.copy(
                    isAuthorized = true,
                    gateMessage = null
                )
                AuthResult.Denied -> _state.value = _state.value.copy(
                    isAuthorized = false,
                    gateMessage = AppBoundaryCopy.GATE_ENABLE_FAILED
                )
                AuthResult.NotConfigured -> _state.value = _state.value.copy(
                    isAuthorized = false,
                    gateMessage = AppBoundaryCopy.GATE_SETUP_UNAVAILABLE
                )
            }
        }
    }

    fun signOut() {
        authProvider.clearAuthorization()
        _state.value = _state.value.copy(isAuthorized = false, gateMessage = null)
    }
}
