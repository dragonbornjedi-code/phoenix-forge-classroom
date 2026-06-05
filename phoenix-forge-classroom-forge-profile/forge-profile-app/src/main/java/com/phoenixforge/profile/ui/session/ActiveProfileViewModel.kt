package com.phoenixforge.profile.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.profile.domain.access.ProfileAccessPolicy
import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.ProfileRole
import com.phoenixforge.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ActiveProfileViewModel @Inject constructor(
    repository: ProfileRepository
) : ViewModel() {

    val profile: StateFlow<ForgeProfile?> = repository.getProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val profileRole: StateFlow<ProfileRole?> = profile
        .map { active -> ProfileRole.fromStorageKey(active?.profileRole) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val visibleSurfaces: StateFlow<List<ProfileAccessPolicy.Surface>> = profileRole
        .map { ProfileAccessPolicy.visibleSurfaces(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
