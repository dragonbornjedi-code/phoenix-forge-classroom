package com.phoenixforge.profile.ui.gate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.profile.domain.bootstrap.ProfileBootstrap
import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.ProfileRole
import com.phoenixforge.profile.domain.repository.ProfileRepository
import com.phoenixforge.profile.domain.session.ProfileSessionStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ProfileGatePhase {
    Loading,
    SignIn,
    Ready
}

data class ProfileGateState(
    val phase: ProfileGatePhase = ProfileGatePhase.Loading,
    val existingProfile: ForgeProfile? = null
)

@HiltViewModel
class ProfileGateViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val sessionStore: ProfileSessionStore,
    private val profileBootstrap: ProfileBootstrap
) : ViewModel() {
    private val _state = MutableStateFlow(ProfileGateState())
    val state: StateFlow<ProfileGateState> = _state.asStateFlow()

    private var sessionActive = false

    init {
        viewModelScope.launch {
            refreshGate()
        }
    }

    private suspend fun refreshGate() {
        val profile = repository.getProfile().firstOrNull()
        val remember = sessionStore.isRememberDeviceEnabled()
        val phase = when {
            profile == null -> ProfileGatePhase.SignIn
            remember || sessionActive -> ProfileGatePhase.Ready
            else -> ProfileGatePhase.SignIn
        }
        _state.value = ProfileGateState(phase = phase, existingProfile = profile)
    }

    fun continueWithExistingProfile(rememberDevice: Boolean) {
        viewModelScope.launch {
            sessionStore.setRememberDevice(rememberDevice)
            sessionActive = true
            _state.value = _state.value.copy(phase = ProfileGatePhase.Ready)
        }
    }

    fun createProfile(
        forgeName: String,
        ageYears: Int?,
        profileRole: ProfileRole,
        rememberDevice: Boolean
    ) {
        if (forgeName.isBlank()) return
        viewModelScope.launch {
            profileBootstrap.createBlankProfile(forgeName, ageYears, profileRole)
            sessionStore.setRememberDevice(rememberDevice)
            sessionActive = true
            _state.value = ProfileGateState(
                phase = ProfileGatePhase.Ready,
                existingProfile = repository.getProfile().firstOrNull()
            )
        }
    }

    fun useDifferentProfile() {
        viewModelScope.launch {
            profileBootstrap.resetAllProfileData()
            sessionStore.clearRememberDevice()
            sessionActive = false
            _state.value = ProfileGateState(phase = ProfileGatePhase.SignIn, existingProfile = null)
        }
    }

    fun signOut() {
        viewModelScope.launch {
            profileBootstrap.resetAllProfileData()
            sessionStore.clearRememberDevice()
            sessionActive = false
            _state.value = ProfileGateState(phase = ProfileGatePhase.SignIn, existingProfile = null)
        }
    }
}
