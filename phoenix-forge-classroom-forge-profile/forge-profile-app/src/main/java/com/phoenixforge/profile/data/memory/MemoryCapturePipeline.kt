package com.phoenixforge.profile.data.memory

import android.net.Uri
import com.phoenixforge.profile.data.network.WifiGate
import com.phoenixforge.profile.domain.model.ArtifactSource
import com.phoenixforge.profile.domain.model.ArtifactType
import com.phoenixforge.profile.domain.model.MemoryArtifact
import com.phoenixforge.profile.domain.model.MemoryCategory
import com.phoenixforge.profile.domain.repository.ProfileRepository
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryCapturePipeline @Inject constructor(
    private val repository: ProfileRepository,
    private val storage: MemoryArtifactStorage,
    private val wifiGate: WifiGate,
) {
    fun canImportFromCloud(): Boolean = wifiGate.isOnWifi()

    fun prepareCameraCapture(): Pair<String, Uri> {
        val id = UUID.randomUUID().toString()
        return id to storage.cameraCaptureUri(id)
    }

    suspend fun saveCameraPhoto(
        artifactId: String,
        category: MemoryCategory,
        note: String? = null,
    ): MemoryArtifact {
        val path = storage.photoFile(artifactId).absolutePath
        return persistPhoto(artifactId, path, category, ArtifactSource.CAMERA, note)
    }

    suspend fun importPhotoFromUri(
        sourceUri: Uri,
        category: MemoryCategory,
        source: ArtifactSource,
        note: String? = null,
    ): MemoryArtifact {
        if (source == ArtifactSource.GOOGLE_DRIVE && !wifiGate.isOnWifi()) {
            error("Google Drive import requires Wi‑Fi")
        }
        val id = UUID.randomUUID().toString()
        val path = storage.copyFromUri(sourceUri, id, "jpg")
        return persistPhoto(id, path, category, source, note)
    }

    suspend fun saveAudioRecording(
        artifactId: String,
        category: MemoryCategory,
        note: String? = null,
    ): MemoryArtifact {
        val path = storage.audioFile(artifactId).absolutePath
        val artifact = MemoryArtifact(
            id = artifactId,
            type = ArtifactType.AUDIO,
            localPath = path,
            checksum = storage.sha256(path),
            capturedAt = Instant.now(),
            note = note,
            category = category,
            source = ArtifactSource.AUDIO_MIC,
        )
        repository.saveMemoryArtifact(artifact)
        return artifact
    }

    fun prepareAudioCapture(): String = UUID.randomUUID().toString()

    fun audioPath(artifactId: String): String = storage.audioFile(artifactId).absolutePath

    private suspend fun persistPhoto(
        id: String,
        path: String,
        category: MemoryCategory,
        source: ArtifactSource,
        note: String?,
    ): MemoryArtifact {
        val artifact = MemoryArtifact(
            id = id,
            type = ArtifactType.PHOTO,
            localPath = path,
            checksum = storage.sha256(path),
            capturedAt = Instant.now(),
            note = note,
            category = category,
            source = source,
        )
        repository.saveMemoryArtifact(artifact)
        return artifact
    }
}
