package com.phoenixforge.profile.ui.studio

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.profile.data.export.ProfileManualPushExporter
import com.phoenixforge.profile.domain.avatar.AvatarHeroCatalog
import com.phoenixforge.profile.domain.avatar.AvatarShardCatalog
import com.phoenixforge.profile.domain.model.Avatar
import com.phoenixforge.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AvatarStudioState(
    val currentAvatar: Avatar? = null,
    val draftStyle: String = "explorer",
    val draftColor: String = "blue",
    val draftSkinTone: String = "medium",
    val draftShardLevel: Int = 0,
    val suggestedShardLevel: Int = 2,
    val isLoading: Boolean = false,
    val lastSavedVersion: Int = 0,
)

@HiltViewModel
class AvatarViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val pushExporter: ProfileManualPushExporter,
) : ViewModel() {

    private val _state = MutableStateFlow(AvatarStudioState())
    val state: StateFlow<AvatarStudioState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getProfile().collect { profile ->
                val suggested = AvatarShardCatalog.suggestFromAge(profile?.ageYears)
                _state.value = _state.value.copy(suggestedShardLevel = suggested)
            }
        }
        viewModelScope.launch {
            repository.getAvatarHistory().collect { history ->
                val latest = history.firstOrNull()
                if (latest == null) {
                    val suggested = _state.value.suggestedShardLevel
                    val seeded = AvatarHeroCatalog.buildAvatar(
                        style = "explorer",
                        color = "blue",
                        skinTone = "medium",
                        version = 1,
                        shardLevel = suggested,
                    )
                    repository.saveAvatar(seeded)
                    _state.value = _state.value.copy(
                        currentAvatar = seeded,
                        draftStyle = seeded.hairType,
                        draftColor = seeded.eyeColor,
                        draftSkinTone = seeded.skinTone,
                        draftShardLevel = seeded.shardLevel,
                        lastSavedVersion = seeded.version,
                    )
                } else {
                    _state.value = _state.value.copy(
                        currentAvatar = latest,
                        draftStyle = AvatarHeroCatalog.normalizeStyle(latest.hairType),
                        draftColor = AvatarHeroCatalog.normalizeColor(latest.eyeColor),
                        draftSkinTone = AvatarHeroCatalog.normalizeSkinTone(latest.skinTone),
                        draftShardLevel = latest.shardLevel,
                        lastSavedVersion = latest.version,
                    )
                }
            }
        }
    }

    fun selectStyle(style: String) {
        _state.value = _state.value.copy(draftStyle = AvatarHeroCatalog.normalizeStyle(style))
        persistDraft()
    }

    fun selectColor(color: String) {
        _state.value = _state.value.copy(draftColor = AvatarHeroCatalog.normalizeColor(color))
        persistDraft()
    }

    fun selectSkinTone(skinTone: String) {
        _state.value = _state.value.copy(draftSkinTone = AvatarHeroCatalog.normalizeSkinTone(skinTone))
        persistDraft()
    }

    fun selectShardLevel(level: Int) {
        _state.value = _state.value.copy(draftShardLevel = AvatarShardCatalog.clamp(level))
        persistDraft()
    }

    fun applySuggestedShard() {
        selectShardLevel(_state.value.suggestedShardLevel)
    }

    fun randomize() {
        val randomized = AvatarHeroCatalog.randomAvatar(_state.value.currentAvatar)
        _state.value = _state.value.copy(
            draftStyle = randomized.hairType,
            draftColor = randomized.eyeColor,
        )
        viewModelScope.launch {
            repository.saveAvatar(randomized)
        }
    }

    fun previewAvatar(): Avatar {
        val current = _state.value
        return AvatarHeroCatalog.buildAvatar(
            style = current.draftStyle,
            color = current.draftColor,
            skinTone = current.draftSkinTone,
            version = current.currentAvatar?.version ?: 1,
            shardLevel = current.draftShardLevel,
            timestamp = current.currentAvatar?.timestamp ?: java.time.Instant.now(),
            id = current.currentAvatar?.id ?: java.util.UUID.randomUUID().toString(),
        )
    }

    private fun persistDraft() {
        val current = _state.value
        val base = current.currentAvatar
        val nextVersion = (base?.version ?: 0) + 1
        val avatar = AvatarHeroCatalog.buildAvatar(
            style = current.draftStyle,
            color = current.draftColor,
            skinTone = current.draftSkinTone,
            version = nextVersion,
            shardLevel = current.draftShardLevel,
            id = base?.id ?: java.util.UUID.randomUUID().toString(),
        )
        viewModelScope.launch {
            repository.saveAvatar(avatar)
            _state.value = _state.value.copy(lastSavedVersion = nextVersion)
        }
    }

    fun pushToTablet(context: Context, onResult: (String) -> Unit) {
        viewModelScope.launch {
            val profile = repository.getProfile().firstOrNull()
            val avatar = previewAvatar()
            if (profile == null) {
                onResult("Create a Forge Profile first.")
                return@launch
            }
            val result = pushExporter.export(context, profile, avatar)
            onResult(result.message)
            context.startActivity(result.shareIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }
}
