package com.phoenixforge.profile.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.profile.data.export.ProfileManualPushExporter
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
    val latestAvatar: com.phoenixforge.profile.domain.model.Avatar? = null,
    val timelineEventCount: Int = 0,
    val memoryCount: Int = 0,
    val avatarCount: Int = 0
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val pushExporter: ProfileManualPushExporter,
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
                    latestAvatar = avatars.firstOrNull(),
                    timelineEventCount = timeline.size,
                    memoryCount = memories.size,
                    avatarCount = avatars.size
                )
            }.collect { dashboardState ->
                _state.value = dashboardState
            }
        }
    }

    fun pushToTablet(context: android.content.Context, onResult: (String) -> Unit) {
        val profile = _state.value.profile ?: run {
            onResult("No profile loaded.")
            return
        }
        val avatar = _state.value.latestAvatar
        val result = pushExporter.export(context, profile, avatar)
        onResult(result.message)
        context.startActivity(result.shareIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
