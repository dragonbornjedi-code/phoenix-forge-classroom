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
                currentTitle = profile.currentTitle
            )
        }

    suspend fun readLatestAvatar(): AvatarExportDto? =
        repository.getAvatarHistory().firstOrNull()?.firstOrNull()?.let { avatar ->
            AvatarExportDto(
                hairType = avatar.hairType,
                eyeColor = avatar.eyeColor,
                skinTone = avatar.skinTone,
                clothingId = avatar.clothingId,
                version = avatar.version
            )
        }

    suspend fun readTimeline(): List<TimelineEventExportDto> =
        repository.getTimelineEvents().firstOrNull().orEmpty().map { event ->
            TimelineEventExportDto(
                title = event.title,
                type = event.type.name,
                timestampEpochMillis = event.timestamp.toEpochMilli()
            )
        }
}
