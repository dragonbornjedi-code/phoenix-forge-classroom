package com.phoenixforge.student.domain.gallery

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.phoenixforge.student.domain.model.GalleryPhoto
import com.phoenixforge.student.domain.model.LifeChapter
import com.phoenixforge.student.domain.model.LifeEvent
import com.phoenixforge.student.domain.model.LifeEventType
import com.phoenixforge.student.domain.model.MemoryArtifact
import com.phoenixforge.student.domain.model.MemorySource
import com.phoenixforge.student.domain.model.PhotoTag
import com.phoenixforge.student.data.network.WifiGate
import com.phoenixforge.student.domain.repository.StudentRepository
import com.phoenixforge.student.domain.vault.MemoryVault
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentGallery @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: StudentRepository,
    private val memoryVault: MemoryVault,
    private val wifiGate: WifiGate,
) {
    fun canImportFromCloud(): Boolean = wifiGate.isOnWifi()
    suspend fun loadDevicePhotos(limit: Int = 60): List<GalleryPhoto> = withContext(Dispatchers.IO) {
        val photos = mutableListOf<GalleryPhoto>()
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN
        )
        val sort = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        context.contentResolver.query(collection, projection, null, null, sort)?.use { cursor ->
            val idCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            var count = 0
            while (cursor.moveToNext() && count < limit) {
                val id = cursor.getLong(idCol)
                val contentUri = Uri.withAppendedPath(collection, id.toString())
                photos += GalleryPhoto(
                    id = id,
                    uri = contentUri.toString(),
                    displayName = cursor.getString(nameCol),
                    dateTakenEpochMillis = cursor.getLong(dateCol).coerceAtLeast(0L)
                )
                count++
            }
        }
        photos
    }

    suspend fun importToVault(
        photo: GalleryPhoto,
        tag: PhotoTag,
        chapter: LifeChapter,
        note: String? = null,
        sealUntilEpochMillis: Long? = null
    ): MemoryArtifact {
        val artifact = MemoryArtifact(
            id = UUID.randomUUID().toString(),
            mediaUri = photo.uri,
            tag = tag,
            chapter = chapter,
            capturedAtEpochMillis = photo.dateTakenEpochMillis.takeIf { it > 0 }
                ?: System.currentTimeMillis(),
            note = note,
            isSealed = sealUntilEpochMillis != null,
            sealedUntilEpochMillis = sealUntilEpochMillis,
            source = MemorySource.DEVICE_GALLERY
        )
        repository.saveMemory(artifact)
        repository.recordLifeEvent(
            LifeEvent(
                type = LifeEventType.PHOTO_IMPORTED,
                payload = "tag=${tag.name};uri=${photo.uri}"
            )
        )
        return artifact
    }

    suspend fun suggestChapter(): LifeChapter {
        val progress = repository.observeProgress().first()
        return memoryVault.chapterForProgress(progress)
    }

    suspend fun importCloudPhoto(
        sourceUri: Uri,
        tag: PhotoTag,
        chapter: LifeChapter,
        note: String? = null,
    ): MemoryArtifact {
        if (!wifiGate.isOnWifi()) error("Google Drive import requires Wi‑Fi")
        val id = UUID.randomUUID().toString()
        val dir = java.io.File(context.filesDir, "vault_imports").apply { mkdirs() }
        val dest = java.io.File(dir, "$id.jpg")
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            dest.outputStream().use { output -> input.copyTo(output) }
        } ?: error("Could not read cloud photo")
        val artifact = MemoryArtifact(
            id = id,
            mediaUri = Uri.fromFile(dest).toString(),
            tag = tag,
            chapter = chapter,
            capturedAtEpochMillis = System.currentTimeMillis(),
            note = note,
            isSealed = false,
            sealedUntilEpochMillis = null,
            source = MemorySource.DEVICE_GALLERY,
        )
        repository.saveMemory(artifact)
        repository.recordLifeEvent(
            LifeEvent(
                type = LifeEventType.PHOTO_IMPORTED,
                payload = "cloud_tag=${tag.name};uri=${artifact.mediaUri}",
            )
        )
        return artifact
    }
}
