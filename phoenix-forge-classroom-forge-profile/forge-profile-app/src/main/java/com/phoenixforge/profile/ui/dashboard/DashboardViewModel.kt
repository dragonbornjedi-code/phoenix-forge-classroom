package com.phoenixforge.profile.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardState(
    val profile: ForgeProfile? = null,
    val timelineEventCount: Int = 0,
    val memoryCount: Int = 0,
    val avatarCount: Int = 0
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.getProfile(),
                repository.getTimelineEvents(),
                repository.getMemoryArtifacts(),
                repository.getAvatarHistory()
            ) { profile, timeline, memories, avatars ->
                DashboardState(
                    profile = profile,
                    timelineEventCount = timeline.size,
                    memoryCount = memories.size,
                    avatarCount = avatars.size
                )
            }.collect { dashboardState ->
                _state.value = dashboardState
            }
        }
    }
}
