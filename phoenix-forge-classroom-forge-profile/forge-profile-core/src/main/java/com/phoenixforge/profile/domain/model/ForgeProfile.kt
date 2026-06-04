package com.phoenixforge.profile.domain.model

import java.time.Instant

data class ForgeProfile(
    val uid: String,
    val forgeName: String,
    val realName: String?,
    val birthDate: Instant?,
    val pronouns: String?,
    val favoriteColor: String?,
    val currentTitle: String?,
    val currentStage: String,
    val sparkMaturationTier: Int
)

data class Avatar(
    val id: String,
    val hairType: String,
    val eyeColor: String,
    val skinTone: String,
    val clothingId: String,
    val version: Int,
    val timestamp: Instant
)

data class IdentitySnapshot(
    val id: String,
    val profileUid: String,
    val fieldName: String,
    val oldValue: String?,
    val newValue: String,
    val timestamp: Instant
)

data class AboutMeEntry(
    val id: String,
    val prompt: String,
    val answer: String,
    val timestamp: Instant
)

data class FavoriteEntry(
    val id: String,
    val category: String,
    val item: String,
    val timestamp: Instant
)

data class DreamEntry(
    val id: String,
    val type: String,
    val content: String,
    val timestamp: Instant
)

data class MemoryArtifact(
    val id: String,
    val type: ArtifactType,
    val localPath: String,
    val checksum: String,
    val capturedAt: Instant,
    val note: String?
)

enum class ArtifactType { PHOTO, AUDIO, DRAWING, SCAN, PROJECT }

enum class EventType {
    PROFILE_CHANGE,
    MEMORY_CAPTURED,
    ABOUT_ME_UPDATED,
    FAVORITE_UPDATED,
    DREAM_UPDATED,
    AVATAR_UPDATED
}

data class TimelineEvent(
    val id: String,
    val title: String,
    val type: EventType,
    val timestamp: Instant,
    val metadata: List<TimelineMetadata>
)

data class ProjectRecord(
    val id: String,
    val title: String,
    val description: String,
    val type: String,
    val timestamp: Instant,
    val artifactId: String?
)

data class SkillRecord(
    val id: String,
    val title: String,
    val domain: String,
    val level: Int,
    val lastReinforced: Instant
)

data class BadgeRecord(
    val id: String,
    val title: String,
    val iconId: String,
    val dateEarned: Instant,
    val criteria: String
)

data class JournalEntry(
    val id: String,
    val content: String,
    val mood: String,
    val timestamp: Instant
)
