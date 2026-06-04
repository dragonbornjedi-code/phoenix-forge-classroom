package com.phoenixforge.profile.ui.studio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.profile.domain.model.Avatar
import com.phoenixforge.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

data class AvatarStudioState(
    val currentAvatar: Avatar? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class AvatarViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AvatarStudioState())
    val state: StateFlow<AvatarStudioState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAvatarHistory().collect { history ->
                _state.value = _state.value.copy(currentAvatar = history.firstOrNull())
            }
        }
    }

    fun updateAvatar(hair: String, eyes: String, skin: String, clothing: String) {
        val newAvatar = Avatar(
            id = UUID.randomUUID().toString(),
            hairType = hair,
            eyeColor = eyes,
            skinTone = skin,
            clothingId = clothing,
            version = (_state.value.currentAvatar?.version ?: 0) + 1,
            timestamp = Instant.now()
        )
        viewModelScope.launch {
            repository.saveAvatar(newAvatar)
        }
    }
}
