package com.phoenixforge.student.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.data.forgeimport.ForgeProfileMemoryImporter
import com.phoenixforge.student.domain.engine.LifeEventCollector
import com.phoenixforge.student.domain.gallery.StudentGallery
import com.phoenixforge.student.domain.model.GalleryPhoto
import com.phoenixforge.student.domain.model.PhotoTag
import com.phoenixforge.student.domain.model.WorldEventResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GalleryUiState(
    val photos: List<GalleryPhoto> = emptyList(),
    val selectedTag: PhotoTag = PhotoTag.ADVENTURE,
    val isLoading: Boolean = false,
    val lastResult: WorldEventResult? = null,
    val message: String? = null,
    val canImportCloud: Boolean = false,
)

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val studentGallery: StudentGallery,
    private val lifeEventCollector: LifeEventCollector,
    private val forgeProfileMemoryImporter: ForgeProfileMemoryImporter,
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryUiState())
    val state: StateFlow<GalleryUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                canImportCloud = studentGallery.canImportFromCloud(),
            )
            val photos = studentGallery.loadDevicePhotos()
            _state.value = _state.value.copy(photos = photos, isLoading = false)
        }
    }

    fun selectTag(tag: PhotoTag) {
        _state.value = _state.value.copy(selectedTag = tag)
    }

    fun importPhoto(photo: GalleryPhoto) {
        viewModelScope.launch {
            val result = lifeEventCollector.onPhotoImported(photo, _state.value.selectedTag)
            _state.value = _state.value.copy(
                lastResult = result,
                message = result.story.narrative
            )
        }
    }

    fun importFromCloud(uri: android.net.Uri) {
        viewModelScope.launch {
            runCatching {
                val chapter = studentGallery.suggestChapter()
                studentGallery.importCloudPhoto(uri, _state.value.selectedTag, chapter)
            }.onSuccess {
                _state.value = _state.value.copy(message = "Imported from cloud into Memory Vault.")
            }.onFailure {
                _state.value = _state.value.copy(message = it.message ?: "Cloud import failed.")
            }
        }
    }

    fun pullSchoolFromForgeProfile() {
        viewModelScope.launch {
            val result = forgeProfileMemoryImporter.importSchoolMemories()
            _state.value = _state.value.copy(message = result.message)
        }
    }
}
