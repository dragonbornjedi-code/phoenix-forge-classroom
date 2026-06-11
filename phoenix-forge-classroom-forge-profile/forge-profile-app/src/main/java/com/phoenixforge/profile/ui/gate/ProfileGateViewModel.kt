package com.phoenixforge.profile.ui.gate

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.profile.domain.access.DeviceProfilePolicy
import com.phoenixforge.profile.domain.bootstrap.ProfileBootstrap
import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.ProfileRole
import com.phoenixforge.profile.domain.repository.ProfileRepository
import com.phoenixforge.profile.domain.session.ProfileSessionStore
import com.phoenixforge.profile.ui.interop.ExternalApps
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
    val existingProfile: ForgeProfile? = null,
    val savedProfiles: List<ForgeProfile> = emptyList(),
    val showCreateForm: Boolean = false,
    val creatableRoles: List<ProfileRole> = listOf(
        ProfileRole.STEWARD_FOR_STUDENT,
        ProfileRole.STUDENT_SELF,
    ),
)

@HiltViewModel
class ProfileGateViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
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
        repository.ensureActiveProfile()
        val savedProfiles = repository.listProfiles().firstOrNull().orEmpty()
        val profile = repository.getProfile().firstOrNull()
        val remember = sessionStore.isRememberDeviceEnabled()
        val creatableRoles = DeviceProfilePolicy.creatableProfileRoles(
            savedProfiles = savedProfiles,
            teacherEditionInstalled = ExternalApps.isTeacherEditionInstalled(appContext),
        )
        val phase = when {
            savedProfiles.isEmpty() -> ProfileGatePhase.SignIn
            profile == null -> ProfileGatePhase.SignIn
            remember || sessionActive -> ProfileGatePhase.Ready
            else -> ProfileGatePhase.SignIn
        }
        _state.value = ProfileGateState(
            phase = phase,
            existingProfile = profile,
            savedProfiles = savedProfiles,
            showCreateForm = savedProfiles.isEmpty(),
            creatableRoles = creatableRoles,
        )
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
                existingProfile = repository.getProfile().firstOrNull(),
                savedProfiles = repository.listProfiles().firstOrNull().orEmpty()
            )
        }
    }

    fun switchToProfile(uid: String, rememberDevice: Boolean) {
        viewModelScope.launch {
            repository.switchActiveProfile(uid)
            sessionStore.setRememberDevice(rememberDevice)
            sessionActive = true
            _state.value = ProfileGateState(
                phase = ProfileGatePhase.Ready,
                existingProfile = repository.getProfile().firstOrNull(),
                savedProfiles = repository.listProfiles().firstOrNull().orEmpty()
            )
        }
    }

    fun useDifferentProfile() {
        viewModelScope.launch {
            sessionStore.clearRememberDevice()
            sessionStore.clearActiveProfileUid()
            sessionActive = false
            val savedProfiles = repository.listProfiles().firstOrNull().orEmpty()
            _state.value = ProfileGateState(
                phase = ProfileGatePhase.SignIn,
                existingProfile = null,
                savedProfiles = savedProfiles,
                showCreateForm = savedProfiles.isEmpty()
            )
        }
    }

    fun showCreateProfileForm() {
        _state.value = _state.value.copy(showCreateForm = true)
    }

    fun signOut() {
        viewModelScope.launch {
            sessionStore.clearRememberDevice()
            sessionActive = false
            val savedProfiles = repository.listProfiles().firstOrNull().orEmpty()
            val profile = repository.getProfile().firstOrNull()
            _state.value = ProfileGateState(
                phase = ProfileGatePhase.SignIn,
                existingProfile = profile,
                savedProfiles = savedProfiles,
                showCreateForm = savedProfiles.isEmpty()
            )
        }
    }

    fun eraseAllLocalData() {
        viewModelScope.launch {
            profileBootstrap.resetAllProfileData()
            sessionStore.clearRememberDevice()
            sessionStore.clearActiveProfileUid()
            sessionActive = false
            _state.value = ProfileGateState(
                phase = ProfileGatePhase.SignIn,
                existingProfile = null,
                savedProfiles = emptyList(),
                showCreateForm = true
            )
        }
    }
}
