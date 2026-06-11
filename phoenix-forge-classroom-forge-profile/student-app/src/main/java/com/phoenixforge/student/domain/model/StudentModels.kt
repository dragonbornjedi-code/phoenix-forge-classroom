package com.phoenixforge.student.domain.model

import kotlinx.serialization.Serializable

enum class HouseRoomType(val displayName: String, val unlockLevel: Int) {
    BEDROOM("Bedroom", 1),
    STUDY("Study", 2),
    GARDEN("Garden", 3),
    MEMORY_VAULT("Memory Vault", 2),
    GALLERY("Gallery", 2),
    QUEST_BOARD("Quest Board", 1),
    PET_SPACE("Pet Space", 4)
}

data class RoomNode(
    val type: HouseRoomType,
    val isUnlocked: Boolean,
    val decorationIds: List<String> = emptyList()
)

data class HouseState(
    val rooms: List<RoomNode>,
    val unlockedRoomTypes: Set<HouseRoomType>,
    val decorations: List<String>,
    val inventory: List<InventoryItem> = emptyList(),
)

@Serializable
data class InventoryItem(
    val itemId: String,
    val zone: String,
    val meshHint: String = "",
)

enum class LifeChapter(val displayName: String, val minLevel: Int) {
    EARLY_EXPLORER("Early Explorer", 1),
    DISCOVERY_PHASE("Discovery Phase", 3),
    SKILL_GROWTH("Skill Growth", 6),
    MASTERY_ARC("Mastery Arc", 10)
}

enum class PhotoTag(val displayName: String) {
    NATURE("Nature"),
    ACHIEVEMENT("Achievement"),
    FAMILY("Family"),
    SCHOOL("School"),
    ADVENTURE("Adventure")
}

data class MemoryArtifact(
    val id: String,
    val mediaUri: String,
    val tag: PhotoTag,
    val chapter: LifeChapter,
    val capturedAtEpochMillis: Long,
    val note: String?,
    val isSealed: Boolean,
    val sealedUntilEpochMillis: Long?,
    val source: MemorySource
)

enum class MemorySource { DEVICE_GALLERY, MANUAL, FORGE_PROFILE_IMPORT }

data class StudentProgress(
    val xp: Int,
    val level: Int,
    val streakDays: Int,
    val lastVisitEpochMillis: Long,
    val unlockFlags: Set<String>,
    val achievementIds: Set<String>,
    val currency: Map<String, Int> = emptyMap(),
)

enum class NpcType { COMPANION, WHISP, PET }

data class NpcState(
    val id: String,
    val type: NpcType,
    val name: String,
    val evolutionStage: Int,
    val isUnlocked: Boolean,
    val lastReaction: String?,
    val mood: String,
    val personalityTraits: List<String> = emptyList(),
    val memoryGraph: List<NpcMemoryNode> = emptyList()
)

enum class QuestType { DAILY, SIDE, EXPLORATION, CREATIVE }

enum class QuestStatus { AVAILABLE, IN_PROGRESS, COMPLETED, CLAIMED }

data class Quest(
    val id: String,
    val type: QuestType,
    val title: String,
    val description: String,
    val status: QuestStatus,
    val xpReward: Int,
    val unlockRewardId: String?,
    val createdAtEpochMillis: Long
)

data class ImportedProfileSnapshot(
    val id: String,
    val uid: String,
    val forgeName: String,
    val currentStage: String?,
    val currentTitle: String?,
    val avatarSummary: String?,
    val timelineSummary: String,
    val importedAtEpochMillis: Long
)

data class StudentInboxMessage(
    val messageId: String,
    val subject: String,
    val body: String,
    val fromDisplayName: String,
    val epochMs: Long,
    val isRead: Boolean,
    val direction: String,
)

data class GalleryPhoto(
    val id: Long,
    val uri: String,
    val displayName: String?,
    val dateTakenEpochMillis: Long
)

data class LifeEvent(
    val type: LifeEventType,
    val payload: String,
    val timestampEpochMillis: Long = System.currentTimeMillis()
)

enum class LifeEventType {
    PHOTO_IMPORTED,
    QUEST_COMPLETED,
    DAILY_RETURN,
    ACHIEVEMENT_LOGGED,
    FORGE_PROFILE_IMPORTED,
    FORGE_WORLD_MEMORY_IMPORTED,
    LEVEL_UP
}

data class RewardGrant(
    val xp: Int,
    val unlockIds: List<String>,
    val npcReactions: List<String>,
    val message: String
)

data class DreamEntry(
    val id: String,
    val type: String,
    val content: String,
    val timestampEpochMillis: Long
)

data class StudentWorldState(
    val house: HouseState,
    val progress: StudentProgress,
    val world: WorldState,
    val activeCompanion: NpcState?,
    val activeQuests: List<Quest>,
    val recentMemories: List<MemoryArtifact>,
    val latestStory: StoryFragment?,
    val importedForgeName: String? = null,
    val importedHeroSummary: String? = null,
)
