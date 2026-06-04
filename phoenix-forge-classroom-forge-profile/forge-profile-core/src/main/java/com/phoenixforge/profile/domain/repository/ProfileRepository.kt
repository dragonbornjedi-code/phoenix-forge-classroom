package com.phoenixforge.profile.domain.repository

import com.phoenixforge.profile.domain.model.AboutMeEntry
import com.phoenixforge.profile.domain.model.Avatar
import com.phoenixforge.profile.domain.model.DreamEntry
import com.phoenixforge.profile.domain.model.FavoriteEntry
import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.MemoryArtifact
import com.phoenixforge.profile.domain.model.TeacherMetadata
import com.phoenixforge.profile.domain.model.TimelineEvent
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(): Flow<ForgeProfile?>
    suspend fun updateProfile(profile: ForgeProfile)

    suspend fun saveAboutMeEntry(entry: AboutMeEntry)
    suspend fun saveFavoriteEntry(entry: FavoriteEntry)
    suspend fun saveDreamEntry(entry: DreamEntry)

    fun getAvatarHistory(): Flow<List<Avatar>>
    suspend fun saveAvatar(avatar: Avatar)

    fun getTimelineEvents(): Flow<List<TimelineEvent>>

    fun getTeacherMetadata(profileUid: String): Flow<List<TeacherMetadata>>

    fun getMemoryArtifacts(): Flow<List<MemoryArtifact>>
    suspend fun saveMemoryArtifact(artifact: MemoryArtifact)
}
