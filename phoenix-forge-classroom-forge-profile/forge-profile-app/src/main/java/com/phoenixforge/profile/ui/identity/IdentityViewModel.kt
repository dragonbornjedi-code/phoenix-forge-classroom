package com.phoenixforge.profile.ui.identity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class IdentityState(
    val profile: ForgeProfile? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class IdentityViewModel @Inject constructor(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _state = MutableStateFlow(IdentityState())
    val state: StateFlow<IdentityState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getProfile().collect { profile ->
                _state.value = _state.value.copy(profile = profile)
            }
        }
    }

    fun updateField(fieldName: String, value: String) {
        viewModelScope.launch {
            val current = repository.getProfile().firstOrNull() ?: return@launch
            val updated = when (fieldName) {
                "forgeName" -> current.copy(forgeName = value)
                "favoriteColor" -> current.copy(favoriteColor = value)
                "currentTitle" -> current.copy(currentTitle = value)
                else -> current
            }
            repository.updateProfile(updated)
        }
    }
}
