package com.phoenixforge.profile.domain.model

import java.time.Instant

data class ForgeProfile(
    val uid: String,
    val forgeName: String,
    val realName: String?,
    val birthDate: Instant?,
    val ageYears: Int?,
    val pronouns: String?,
    val favoriteColor: String?,
    val currentTitle: String?,
    val currentStage: String,
    val sparkMaturationTier: Int,
    val profileRole: String?
)

data class Avatar(
    val id: String,
    val hairType: String,
    val eyeColor: String,
    val skinTone: String,
    val clothingId: String,
    val version: Int,
    val shardLevel: Int = 0,
    val timestamp: Instant,
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

/** Where a memory belongs in Ezra's life archive (master step 0.63–0.65). */
enum class MemoryCategory(val displayName: String) {
    SELF("Ezra"),
    FAMILY("Family time"),
    SCHOOL("School & outings"),
}

/** How the artifact entered the capsule — offline-first by default. */
enum class ArtifactSource(val displayName: String) {
    CAMERA("Camera"),
    DEVICE_GALLERY("Phone gallery"),
    GOOGLE_DRIVE("Google Drive"),
    AUDIO_MIC("Voice memo"),
}

data class MemoryArtifact(
    val id: String,
    val type: ArtifactType,
    val localPath: String,
    val checksum: String,
    val capturedAt: Instant,
    val note: String?,
    val category: MemoryCategory = MemoryCategory.FAMILY,
    val source: ArtifactSource = ArtifactSource.DEVICE_GALLERY,
    val syncedToStudent: Boolean = false,
)

enum class ArtifactType { PHOTO, AUDIO, DRAWING, SCAN, PROJECT }

enum class EventType {
    PROFILE_CHANGE,
    MEMORY_CAPTURED,
    ABOUT_ME_UPDATED,
    FAVORITE_UPDATED,
    DREAM_UPDATED,
    AVATAR_UPDATED,
    /** Ingested from EVT_*.json (Student Edition, Forge World, future routines). */
    SYNC_EVENT,
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
