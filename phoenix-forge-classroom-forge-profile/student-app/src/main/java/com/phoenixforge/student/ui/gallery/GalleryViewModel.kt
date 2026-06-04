package com.phoenixforge.student.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val message: String? = null
)

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val studentGallery: StudentGallery,
    private val lifeEventCollector: LifeEventCollector
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryUiState())
    val state: StateFlow<GalleryUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
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
}
