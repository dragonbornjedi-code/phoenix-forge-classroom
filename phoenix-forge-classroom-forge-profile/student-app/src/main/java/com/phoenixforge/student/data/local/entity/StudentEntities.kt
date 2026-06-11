package com.phoenixforge.student.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_progress")
data class StudentProgressEntity(
    @PrimaryKey val id: Int = 1,
    val xp: Int,
    val level: Int,
    val streakDays: Int,
    val lastVisitEpochMillis: Long,
    val unlockFlagsJson: String,
    val achievementIdsJson: String,
    val currencyJson: String = "{}",
)

@Entity(tableName = "house_state")
data class HouseStateEntity(
    @PrimaryKey val id: Int = 1,
    val unlockedRoomsJson: String,
    val decorationsJson: String,
    val inventoryJson: String = "[]",
)

@Entity(tableName = "memory_artifacts")
data class MemoryArtifactEntity(
    @PrimaryKey val id: String,
    val mediaUri: String,
    val tag: String,
    val chapter: String,
    val capturedAtEpochMillis: Long,
    val note: String?,
    val isSealed: Boolean,
    val sealedUntilEpochMillis: Long?,
    val source: String
)

@Entity(tableName = "npc_states")
data class NpcEntity(
    @PrimaryKey val id: String,
    val type: String,
    val name: String,
    val evolutionStage: Int,
    val isUnlocked: Boolean,
    val lastReaction: String?,
    val mood: String,
    val personalityTraitsJson: String,
    val memoryGraphJson: String,
    val anchorsJson: String
)

@Entity(tableName = "quests")
data class QuestEntity(
    @PrimaryKey val id: String,
    val type: String,
    val title: String,
    val description: String,
    val status: String,
    val xpReward: Int,
    val unlockRewardId: String?,
    val createdAtEpochMillis: Long
)

@Entity(tableName = "imported_profile_snapshots")
data class ImportedProfileSnapshotEntity(
    @PrimaryKey val id: String,
    val uid: String,
    val forgeName: String,
    val currentStage: String?,
    val currentTitle: String?,
    val avatarSummary: String?,
    val timelineSummary: String,
    val importedAtEpochMillis: Long
)

@Entity(tableName = "life_events")
data class LifeEventEntity(
    @PrimaryKey val id: String,
    val type: String,
    val payload: String,
    val timestampEpochMillis: Long
)

@Entity(tableName = "story_fragments")
data class StoryFragmentEntity(
    @PrimaryKey val id: String,
    val narrative: String,
    val worldEventType: String,
    val xpAwarded: Int,
    val roomUnlocked: String?,
    val npcSpeaker: String?,
    val environmentChange: String?,
    val causeFragmentId: String?,
    val callbackLine: String?,
    val emotionalImpact: Float,
    val continuityThread: String?,
    val timestampEpochMillis: Long
)

@Entity(tableName = "memory_event_drafts")
data class MemoryEventDraftEntity(
    @PrimaryKey val eventId: String,
    val version: String,
    val capturedAt: String,
    val sourceShell: String,
    val eventType: String,
    val title: String,
    val summary: String,
    val childMood: String,
    val locationId: String,
    val contractJson: String,
    val importedAtEpochMillis: Long,
    val importSource: String,
)

@Entity(tableName = "dream_entries")
data class DreamEntryEntity(
    @PrimaryKey val id: String,
    val type: String,
    val content: String,
    val timestampEpochMillis: Long
)

@Entity(tableName = "world_state_meta")
data class WorldStateMetaEntity(
    @PrimaryKey val id: Int = 1,
    val lastActivity: String?,
    val environmentTheme: String,
    val driftJson: String
)
