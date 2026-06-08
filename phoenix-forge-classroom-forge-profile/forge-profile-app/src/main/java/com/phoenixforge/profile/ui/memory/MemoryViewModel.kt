package com.phoenixforge.profile.ui.memory

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.profile.data.memory.AudioCaptureHelper
import com.phoenixforge.profile.data.memory.MemoryCapturePipeline
import com.phoenixforge.profile.domain.model.ArtifactSource
import com.phoenixforge.profile.domain.model.ArtifactType
import com.phoenixforge.profile.domain.model.MemoryArtifact
import com.phoenixforge.profile.domain.model.MemoryCategory
import com.phoenixforge.profile.domain.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MemoryState(
    val artifacts: List<MemoryArtifact> = emptyList(),
    val selectedCategory: MemoryCategory = MemoryCategory.FAMILY,
    val filterCategory: MemoryCategory? = null,
    val isLoading: Boolean = false,
    val isRecording: Boolean = false,
    val canImportCloud: Boolean = false,
    val message: String? = null,
    val pendingCameraId: String? = null,
    val pendingCameraUri: Uri? = null,
)

@HiltViewModel
class MemoryViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val pipeline: MemoryCapturePipeline,
    private val audioCapture: AudioCaptureHelper,
) : ViewModel() {

    private val _state = MutableStateFlow(MemoryState(canImportCloud = pipeline.canImportFromCloud()))
    val state: StateFlow<MemoryState> = _state.asStateFlow()

    private var pendingAudioId: String? = null

    init {
        viewModelScope.launch {
            repository.getMemoryArtifacts().collect { list ->
                _state.value = _state.value.copy(artifacts = list)
            }
        }
    }

    fun refreshCloudGate() {
        _state.value = _state.value.copy(canImportCloud = pipeline.canImportFromCloud())
    }

    fun selectCategory(category: MemoryCategory) {
        _state.value = _state.value.copy(selectedCategory = category)
    }

    fun setFilter(category: MemoryCategory?) {
        _state.value = _state.value.copy(filterCategory = category)
    }

    fun prepareCamera(): Uri? {
        val (id, uri) = pipeline.prepareCameraCapture()
        _state.value = _state.value.copy(pendingCameraId = id, pendingCameraUri = uri)
        return uri
    }

    fun onCameraResult(success: Boolean) {
        val id = _state.value.pendingCameraId ?: return
        if (!success) {
            _state.value = _state.value.copy(message = "Camera capture cancelled.")
            return
        }
        viewModelScope.launch {
            runCatching {
                pipeline.saveCameraPhoto(id, _state.value.selectedCategory)
            }.onSuccess {
                _state.value = _state.value.copy(
                    message = "Photo saved to ${it.category.displayName}.",
                    pendingCameraId = null,
                    pendingCameraUri = null,
                )
            }.onFailure {
                _state.value = _state.value.copy(message = it.message ?: "Could not save photo.")
            }
        }
    }

    fun importFromGallery(uri: Uri) {
        viewModelScope.launch {
            runCatching {
                pipeline.importPhotoFromUri(
                    uri,
                    _state.value.selectedCategory,
                    ArtifactSource.DEVICE_GALLERY,
                )
            }.onSuccess {
                _state.value = _state.value.copy(message = "Imported to ${it.category.displayName}.")
            }.onFailure {
                _state.value = _state.value.copy(message = it.message ?: "Import failed.")
            }
        }
    }

    fun importFromCloud(uri: Uri) {
        viewModelScope.launch {
            runCatching {
                pipeline.importPhotoFromUri(
                    uri,
                    _state.value.selectedCategory,
                    ArtifactSource.GOOGLE_DRIVE,
                )
            }.onSuccess {
                _state.value = _state.value.copy(message = "Imported from cloud to ${it.category.displayName}.")
            }.onFailure {
                _state.value = _state.value.copy(message = it.message ?: "Cloud import failed.")
            }
        }
    }

    fun startRecording() {
        val id = pipeline.prepareAudioCapture()
        pendingAudioId = id
        audioCapture.start(pipeline.audioPath(id))
        _state.value = _state.value.copy(isRecording = true, message = "Recording…")
    }

    fun stopRecording() {
        val id = pendingAudioId ?: return
        val stopped = audioCapture.stop()
        pendingAudioId = null
        _state.value = _state.value.copy(isRecording = false)
        if (!stopped) {
            _state.value = _state.value.copy(message = "Recording failed.")
            return
        }
        viewModelScope.launch {
            runCatching {
                pipeline.saveAudioRecording(id, _state.value.selectedCategory)
            }.onSuccess {
                _state.value = _state.value.copy(message = "Voice memo saved.")
            }.onFailure {
                _state.value = _state.value.copy(message = it.message ?: "Could not save audio.")
            }
        }
    }

    fun visibleArtifacts(): List<MemoryArtifact> {
        val filter = _state.value.filterCategory
        return _state.value.artifacts.filter { artifact ->
            filter == null || artifact.category == filter
        }
    }
}
