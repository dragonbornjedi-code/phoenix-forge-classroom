package com.phoenixforge.profile.data.provider

import com.phoenixforge.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileExportReader @Inject constructor(
    private val repository: ProfileRepository
) {

    suspend fun readProfile(): ProfileExportDto? =
        repository.getProfile().firstOrNull()?.let { profile ->
            ProfileExportDto(
                uid = profile.uid,
                forgeName = profile.forgeName,
                currentStage = profile.currentStage,
                currentTitle = profile.currentTitle,
                ageYears = profile.ageYears,
                profileRole = profile.profileRole
            )
        }

    suspend fun readLatestAvatar(): AvatarExportDto? {
        val profile = repository.getProfile().firstOrNull() ?: return null
        val avatar = repository.getAvatarHistory().firstOrNull()?.firstOrNull() ?: return null
        return AvatarExportPayload.build(profile.uid, profile.forgeName, avatar)
    }

    suspend fun readTimeline(): List<TimelineEventExportDto> =
        repository.getTimelineEvents().firstOrNull().orEmpty().map { event ->
            TimelineEventExportDto(
                title = event.title,
                type = event.type.name,
                timestampEpochMillis = event.timestamp.toEpochMilli()
            )
        }

    suspend fun readMemories(): List<MemoryExportDto> =
        repository.getMemoryArtifacts().firstOrNull().orEmpty().map { artifact ->
            MemoryExportDto(
                id = artifact.id,
                type = artifact.type.name,
                category = artifact.category.name,
                source = artifact.source.name,
                capturedAtEpochMillis = artifact.capturedAt.toEpochMilli(),
                note = artifact.note,
                checksum = artifact.checksum,
                syncedToStudent = artifact.syncedToStudent,
                contentUri = ProfileContract.memoryFileUri(artifact.id).toString(),
            )
        }
}
