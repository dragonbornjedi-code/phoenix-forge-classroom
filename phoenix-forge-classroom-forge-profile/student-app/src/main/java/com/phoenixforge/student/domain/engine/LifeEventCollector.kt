package com.phoenixforge.student.domain.engine

import com.phoenixforge.student.domain.gallery.StudentGallery
import com.phoenixforge.student.domain.model.GalleryPhoto
import com.phoenixforge.student.domain.model.ImportedProfileSnapshot
import com.phoenixforge.student.domain.model.PhotoTag
import com.phoenixforge.student.domain.model.Quest
import com.phoenixforge.student.domain.model.WorldEventResult
import com.phoenixforge.student.domain.repository.StudentRepository
import com.phoenixforge.student.domain.world.WorldOrchestrator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LifeEventCollector @Inject constructor(
    private val repository: StudentRepository,
    private val worldOrchestrator: WorldOrchestrator,
    private val studentGallery: StudentGallery
) {
    suspend fun onAppOpened(): WorldEventResult = worldOrchestrator.onDailyReturn()

    suspend fun onPhotoImported(photo: GalleryPhoto, tag: PhotoTag): WorldEventResult {
        val chapter = studentGallery.suggestChapter()
        studentGallery.importToVault(photo, tag, chapter)
        val result = worldOrchestrator.onPhotoUploaded(tag.displayName)
        worldOrchestrator.completeMatchingQuest("Capture a Moment")
        return result
    }

    suspend fun onAchievementLogged(title: String): WorldEventResult =
        worldOrchestrator.onAchievementLogged(title)

    suspend fun onForgeProfileImported(snapshot: ImportedProfileSnapshot): WorldEventResult {
        repository.saveImportedProfile(snapshot)
        return worldOrchestrator.onForgeProfileImported(
            forgeName = snapshot.forgeName,
            currentStage = snapshot.currentStage,
            uid = snapshot.uid
        )
    }

    suspend fun onQuestCompleted(quest: Quest): WorldEventResult =
        worldOrchestrator.onQuestComplete(quest)
}
