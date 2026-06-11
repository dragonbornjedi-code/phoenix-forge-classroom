package com.phoenixforge.profile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.phoenixforge.profile.domain.model.ArtifactSource
import com.phoenixforge.profile.domain.model.ArtifactType
import com.phoenixforge.profile.domain.model.EventType
import com.phoenixforge.profile.domain.model.MemoryCategory

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val uid: String,
    val forgeName: String,
    val realName: String?,
    val birthDate: Long?,
    val ageYears: Int?,
    val pronouns: String?,
    val favoriteColor: String?,
    val currentTitle: String?,
    val currentStage: String,
    val sparkMaturationTier: Int,
    val profileRole: String?
)

@Entity(tableName = "avatars")
data class AvatarEntity(
    @PrimaryKey val id: String,
    val profileUid: String,
    val hairType: String,
    val eyeColor: String,
    val skinTone: String,
    val clothingId: String,
    val version: Int,
    val shardLevel: Int = 0,
    val timestamp: Long,
)

@Entity(tableName = "identity_snapshots")
data class IdentitySnapshotEntity(
    @PrimaryKey val id: String,
    val profileUid: String,
    val fieldName: String,
    val oldValue: String?,
    val newValue: String,
    val timestamp: Long
)

@Entity(tableName = "memory_artifacts")
data class MemoryArtifactEntity(
    @PrimaryKey val id: String,
    val profileUid: String,
    val type: ArtifactType,
    val localPath: String,
    val checksum: String,
    val capturedAt: Long,
    val note: String?,
    val category: MemoryCategory = MemoryCategory.FAMILY,
    val source: ArtifactSource = ArtifactSource.DEVICE_GALLERY,
    val syncedToStudent: Boolean = false,
)

@Entity(tableName = "timeline_events")
data class TimelineEventEntity(
    @PrimaryKey val id: String,
    val profileUid: String,
    val title: String,
    val type: EventType,
    val timestamp: Long,
    val metadataJson: String
)

@Entity(tableName = "teacher_metadata")
data class TeacherMetadataEntity(
    @PrimaryKey val id: String,
    val profileUid: String,
    val key: String,
    val value: String,
    val category: String,
    val timestamp: Long
)

@Entity(tableName = "about_me")
data class AboutMeEntryEntity(
    @PrimaryKey val id: String,
    val profileUid: String,
    val prompt: String,
    val answer: String,
    val timestamp: Long
)

@Entity(tableName = "favorites")
data class FavoriteEntryEntity(
    @PrimaryKey val id: String,
    val profileUid: String,
    val category: String,
    val item: String,
    val timestamp: Long
)

@Entity(tableName = "dreams")
data class DreamEntryEntity(
    @PrimaryKey val id: String,
    val profileUid: String,
    val type: String,
    val content: String,
    val timestamp: Long
)
