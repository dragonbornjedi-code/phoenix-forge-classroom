package com.phoenixforge.student.data.repository

import com.phoenixforge.student.data.local.dao.StudentDao
import com.phoenixforge.student.data.local.entity.BehaviorSignalsEntity
import com.phoenixforge.student.data.local.entity.HouseStateEntity
import com.phoenixforge.student.data.local.entity.ImportedProfileSnapshotEntity
import com.phoenixforge.student.data.local.entity.LifeEventEntity
import com.phoenixforge.student.data.local.entity.MemoryArtifactEntity
import com.phoenixforge.student.data.local.entity.NpcEntity
import com.phoenixforge.student.data.local.entity.QuestEntity
import com.phoenixforge.student.data.local.entity.StoryFragmentEntity
import com.phoenixforge.student.data.local.entity.StudentProgressEntity
import com.phoenixforge.student.data.local.entity.WorldStateMetaEntity
import com.phoenixforge.student.data.serialization.StudentJson
import com.phoenixforge.student.domain.model.HouseState
import com.phoenixforge.student.domain.model.HouseRoomType
import com.phoenixforge.student.domain.model.ImportedProfileSnapshot
import com.phoenixforge.student.domain.model.LifeChapter
import com.phoenixforge.student.domain.model.LifeEvent
import com.phoenixforge.student.domain.model.MemoryArtifact
import com.phoenixforge.student.domain.model.MemorySource
import com.phoenixforge.student.domain.model.NpcState
import com.phoenixforge.student.domain.model.NpcType
import com.phoenixforge.student.domain.model.PhotoTag
import com.phoenixforge.student.domain.model.Quest
import com.phoenixforge.student.domain.model.QuestStatus
import com.phoenixforge.student.domain.model.QuestType
import com.phoenixforge.student.domain.model.RoomNode
import com.phoenixforge.student.domain.model.BehaviorSignals
import com.phoenixforge.student.domain.model.StoryFragment
import com.phoenixforge.student.domain.model.StudentProgress
import com.phoenixforge.student.domain.model.WorldDriftState
import com.phoenixforge.student.domain.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StudentRepositoryImpl @Inject constructor(
    private val dao: StudentDao,
    private val studentJson: StudentJson
) : StudentRepository {

    override fun observeProgress(): Flow<StudentProgress> =
        dao.observeProgress().map { it?.toDomain() ?: defaultProgress() }

    override suspend fun saveProgress(progress: StudentProgress) {
        dao.upsertProgress(progress.toEntity(studentJson))
    }

    override fun observeHouse(): Flow<HouseState> =
        dao.observeHouseState().map { it?.toDomain(studentJson) ?: defaultHouse() }

    override suspend fun saveHouse(state: HouseState) {
        dao.upsertHouseState(state.toEntity(studentJson))
    }

    override fun observeMemories(): Flow<List<MemoryArtifact>> =
        dao.observeMemories().map { list -> list.map { it.toDomain() } }

    override fun observeRecentMemories(limit: Int): Flow<List<MemoryArtifact>> =
        dao.observeRecentMemories(limit).map { list -> list.map { it.toDomain() } }

    override suspend fun saveMemory(memory: MemoryArtifact) {
        dao.insertMemory(memory.toEntity())
    }

    override fun observeNpcs(): Flow<List<NpcState>> =
        dao.observeNpcs().map { list -> list.map { it.toDomain() } }

    override suspend fun saveNpc(npc: NpcState) {
        dao.upsertNpc(npc.toEntity())
    }

    override fun observeQuests(): Flow<List<Quest>> =
        dao.observeQuests().map { list -> list.map { it.toDomain() } }

    override suspend fun saveQuest(quest: Quest) {
        dao.upsertQuest(quest.toEntity())
    }

    override fun observeImportedProfiles(): Flow<List<ImportedProfileSnapshot>> =
        dao.observeImportedProfiles().map { list -> list.map { it.toDomain() } }

    override suspend fun saveImportedProfile(snapshot: ImportedProfileSnapshot) {
        dao.insertImportedProfile(snapshot.toEntity())
    }

    override suspend fun recordLifeEvent(event: LifeEvent) {
        dao.insertLifeEvent(
            LifeEventEntity(
                id = UUID.randomUUID().toString(),
                type = event.type.name,
                payload = event.payload,
                timestampEpochMillis = event.timestampEpochMillis
            )
        )
    }

    override fun observeLifeEvents(limit: Int): Flow<List<LifeEvent>> =
        dao.observeLifeEvents(limit).map { list ->
            list.map {
                LifeEvent(
                    type = com.phoenixforge.student.domain.model.LifeEventType.valueOf(it.type),
                    payload = it.payload,
                    timestampEpochMillis = it.timestampEpochMillis
                )
            }
        }

    override fun observeStoryFragments(limit: Int): Flow<List<StoryFragment>> =
        dao.observeStoryFragments(limit).map { list -> list.map { it.toDomain() } }

    override fun observeLatestStory(): Flow<StoryFragment?> =
        dao.observeLatestStory().map { it?.toDomain() }

    override suspend fun saveStoryFragment(fragment: StoryFragment) {
        dao.insertStoryFragment(fragment.toEntity())
    }

    override fun observeWorldMeta(): Flow<Triple<String?, String, WorldDriftState?>> =
        dao.observeWorldMeta().map { meta ->
            Triple(
                meta?.lastActivity,
                meta?.environmentTheme ?: "forest_dawn",
                meta?.driftJson?.let { studentJson.decodeDrift(it) }
            )
        }

    override suspend fun saveWorldMeta(
        lastActivity: String?,
        environmentTheme: String,
        drift: WorldDriftState?
    ) {
        dao.upsertWorldMeta(
            WorldStateMetaEntity(
                lastActivity = lastActivity,
                environmentTheme = environmentTheme,
                driftJson = drift?.let { studentJson.encodeDrift(it) } ?: ""
            )
        )
    }

    override fun observeBehaviorSignals(): Flow<BehaviorSignals> =
        dao.observeBehaviorSignals().map { it?.toDomain() ?: defaultBehaviorSignals() }

    override suspend fun saveBehaviorSignals(signals: BehaviorSignals) {
        dao.upsertBehaviorSignals(signals.toEntity())
    }

    private fun defaultProgress() = StudentProgress(
        xp = 0,
        level = 1,
        streakDays = 0,
        lastVisitEpochMillis = 0L,
        unlockFlags = emptySet(),
        achievementIds = emptySet()
    )

    private fun defaultHouse(): HouseState {
        val unlocked = setOf(HouseRoomType.BEDROOM, HouseRoomType.QUEST_BOARD)
        return HouseState(
            rooms = HouseRoomType.entries.map { type ->
                RoomNode(type = type, isUnlocked = type in unlocked)
            },
            unlockedRoomTypes = unlocked,
            decorations = emptyList()
        )
    }

    private fun StudentProgressEntity.toDomain() = StudentProgress(
        xp = xp,
        level = level,
        streakDays = streakDays,
        lastVisitEpochMillis = lastVisitEpochMillis,
        unlockFlags = studentJson.decodeStringSet(unlockFlagsJson),
        achievementIds = studentJson.decodeStringSet(achievementIdsJson)
    )

    private fun StudentProgress.toEntity(json: StudentJson) = StudentProgressEntity(
        xp = xp,
        level = level,
        streakDays = streakDays,
        lastVisitEpochMillis = lastVisitEpochMillis,
        unlockFlagsJson = json.encodeStringSet(unlockFlags),
        achievementIdsJson = json.encodeStringSet(achievementIds)
    )

    private fun HouseStateEntity.toDomain(json: StudentJson): HouseState {
        val unlockedNames = json.decodeStringList(unlockedRoomsJson).toSet()
        val unlocked = unlockedNames.mapNotNull { runCatching { HouseRoomType.valueOf(it) }.getOrNull() }.toSet()
        val decorations = json.decodeStringList(decorationsJson)
        return HouseState(
            rooms = HouseRoomType.entries.map { type ->
                RoomNode(type = type, isUnlocked = type in unlocked, decorationIds = decorations.filter { it.startsWith(type.name) })
            },
            unlockedRoomTypes = unlocked,
            decorations = decorations
        )
    }

    private fun HouseState.toEntity(json: StudentJson) = HouseStateEntity(
        unlockedRoomsJson = json.encodeStringList(unlockedRoomTypes.map { it.name }),
        decorationsJson = json.encodeStringList(decorations)
    )

    private fun MemoryArtifactEntity.toDomain() = MemoryArtifact(
        id = id,
        mediaUri = mediaUri,
        tag = PhotoTag.valueOf(tag),
        chapter = LifeChapter.valueOf(chapter),
        capturedAtEpochMillis = capturedAtEpochMillis,
        note = note,
        isSealed = isSealed,
        sealedUntilEpochMillis = sealedUntilEpochMillis,
        source = MemorySource.valueOf(source)
    )

    private fun MemoryArtifact.toEntity() = MemoryArtifactEntity(
        id = id,
        mediaUri = mediaUri,
        tag = tag.name,
        chapter = chapter.name,
        capturedAtEpochMillis = capturedAtEpochMillis,
        note = note,
        isSealed = isSealed,
        sealedUntilEpochMillis = sealedUntilEpochMillis,
        source = source.name
    )

    private fun NpcEntity.toDomain() = NpcState(
        id = id,
        type = NpcType.valueOf(type),
        name = name,
        evolutionStage = evolutionStage,
        isUnlocked = isUnlocked,
        lastReaction = lastReaction,
        mood = mood,
        personalityTraits = studentJson.decodeStringList(personalityTraitsJson),
        memoryGraph = studentJson.decodeMemoryGraph(memoryGraphJson)
    )

    private fun NpcState.toEntity() = NpcEntity(
        id = id,
        type = type.name,
        name = name,
        evolutionStage = evolutionStage,
        isUnlocked = isUnlocked,
        lastReaction = lastReaction,
        mood = mood,
        personalityTraitsJson = studentJson.encodeStringList(personalityTraits),
        memoryGraphJson = studentJson.encodeMemoryGraph(memoryGraph),
        anchorsJson = studentJson.encodeAnchors(
            memoryGraph.filter { it.isAnchor }.map {
                com.phoenixforge.student.domain.model.NpcMemoryAnchor(
                    summary = it.summary,
                    eventType = it.eventType,
                    strength = it.strength,
                    weekAnchorEpochMillis = it.timestampEpochMillis
                )
            }
        )
    )

    private fun QuestEntity.toDomain() = Quest(
        id = id,
        type = QuestType.valueOf(type),
        title = title,
        description = description,
        status = QuestStatus.valueOf(status),
        xpReward = xpReward,
        unlockRewardId = unlockRewardId,
        createdAtEpochMillis = createdAtEpochMillis
    )

    private fun Quest.toEntity() = QuestEntity(
        id = id,
        type = type.name,
        title = title,
        description = description,
        status = status.name,
        xpReward = xpReward,
        unlockRewardId = unlockRewardId,
        createdAtEpochMillis = createdAtEpochMillis
    )

    private fun ImportedProfileSnapshotEntity.toDomain() = ImportedProfileSnapshot(
        id = id,
        uid = uid,
        forgeName = forgeName,
        currentStage = currentStage,
        currentTitle = currentTitle,
        avatarSummary = avatarSummary,
        timelineSummary = timelineSummary,
        importedAtEpochMillis = importedAtEpochMillis
    )

    private fun ImportedProfileSnapshot.toEntity() = ImportedProfileSnapshotEntity(
        id = id,
        uid = uid,
        forgeName = forgeName,
        currentStage = currentStage,
        currentTitle = currentTitle,
        avatarSummary = avatarSummary,
        timelineSummary = timelineSummary,
        importedAtEpochMillis = importedAtEpochMillis
    )

    private fun StoryFragmentEntity.toDomain() = StoryFragment(
        id = id,
        narrative = narrative,
        worldEventType = com.phoenixforge.student.domain.model.WorldEventType.valueOf(worldEventType),
        xpAwarded = xpAwarded,
        roomUnlocked = roomUnlocked,
        npcSpeaker = npcSpeaker,
        environmentChange = environmentChange,
        timestampEpochMillis = timestampEpochMillis,
        causeFragmentId = causeFragmentId,
        callbackLine = callbackLine,
        emotionalImpact = emotionalImpact,
        continuityThread = continuityThread
    )

    private fun StoryFragment.toEntity() = StoryFragmentEntity(
        id = id,
        narrative = narrative,
        worldEventType = worldEventType.name,
        xpAwarded = xpAwarded,
        roomUnlocked = roomUnlocked,
        npcSpeaker = npcSpeaker,
        environmentChange = environmentChange,
        causeFragmentId = causeFragmentId,
        callbackLine = callbackLine,
        emotionalImpact = emotionalImpact,
        continuityThread = continuityThread,
        timestampEpochMillis = timestampEpochMillis
    )

    private fun defaultBehaviorSignals() = BehaviorSignals()

    private fun BehaviorSignalsEntity.toDomain() = BehaviorSignals(
        photosThisWeek = photosThisWeek,
        questsCompletedThisWeek = questsCompletedThisWeek,
        returnsThisWeek = returnsThisWeek,
        lastPhotoEpochMillis = lastPhotoEpochMillis,
        lastQuestEpochMillis = lastQuestEpochMillis,
        lastVisitEpochMillis = lastVisitEpochMillis,
        weekAnchorEpochMillis = weekAnchorEpochMillis
    )

    private fun BehaviorSignals.toEntity() = BehaviorSignalsEntity(
        photosThisWeek = photosThisWeek,
        questsCompletedThisWeek = questsCompletedThisWeek,
        returnsThisWeek = returnsThisWeek,
        lastPhotoEpochMillis = lastPhotoEpochMillis,
        lastQuestEpochMillis = lastQuestEpochMillis,
        lastVisitEpochMillis = lastVisitEpochMillis,
        weekAnchorEpochMillis = weekAnchorEpochMillis
    )
}
