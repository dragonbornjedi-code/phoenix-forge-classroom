package com.phoenixforge.student.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phoenixforge.student.data.local.entity.BehaviorSignalsEntity
import com.phoenixforge.student.data.local.entity.DreamEntryEntity
import com.phoenixforge.student.data.local.entity.HouseStateEntity
import com.phoenixforge.student.data.local.entity.ImportedProfileSnapshotEntity
import com.phoenixforge.student.data.local.entity.LifeEventEntity
import com.phoenixforge.student.data.local.entity.MemoryArtifactEntity
import com.phoenixforge.student.data.local.entity.MemoryEventDraftEntity
import com.phoenixforge.student.data.local.entity.NpcEntity
import com.phoenixforge.student.data.local.entity.QuestEntity
import com.phoenixforge.student.data.local.entity.StoryFragmentEntity
import com.phoenixforge.student.data.local.entity.StudentProgressEntity
import com.phoenixforge.student.data.local.entity.WorldStateMetaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Query("SELECT * FROM student_progress WHERE id = 1")
    fun observeProgress(): Flow<StudentProgressEntity?>

    @Query("SELECT * FROM student_progress WHERE id = 1")
    suspend fun getProgress(): StudentProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProgress(entity: StudentProgressEntity)

    @Query("SELECT * FROM house_state WHERE id = 1")
    fun observeHouseState(): Flow<HouseStateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertHouseState(entity: HouseStateEntity)

    @Query("SELECT * FROM memory_artifacts ORDER BY capturedAtEpochMillis DESC")
    fun observeMemories(): Flow<List<MemoryArtifactEntity>>

    @Query("SELECT * FROM memory_artifacts ORDER BY capturedAtEpochMillis DESC LIMIT :limit")
    fun observeRecentMemories(limit: Int): Flow<List<MemoryArtifactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemory(entity: MemoryArtifactEntity)

    @Query("SELECT * FROM memory_event_drafts ORDER BY importedAtEpochMillis DESC")
    fun observeMemoryEventDrafts(): Flow<List<MemoryEventDraftEntity>>

    @Query("SELECT COUNT(*) FROM memory_event_drafts")
    fun observeMemoryEventDraftCount(): Flow<Int>

    @Query("SELECT eventId FROM memory_event_drafts")
    suspend fun listMemoryEventDraftIds(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMemoryEventDraft(entity: MemoryEventDraftEntity): Long

    @Query("SELECT * FROM npc_states ORDER BY type ASC")
    fun observeNpcs(): Flow<List<NpcEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNpc(entity: NpcEntity)

    @Query("SELECT * FROM quests ORDER BY createdAtEpochMillis DESC")
    fun observeQuests(): Flow<List<QuestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertQuest(entity: QuestEntity)

    @Query("SELECT * FROM imported_profile_snapshots ORDER BY importedAtEpochMillis DESC")
    fun observeImportedProfiles(): Flow<List<ImportedProfileSnapshotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImportedProfile(entity: ImportedProfileSnapshotEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLifeEvent(entity: LifeEventEntity)

    @Query("SELECT * FROM life_events ORDER BY timestampEpochMillis DESC LIMIT :limit")
    fun observeLifeEvents(limit: Int): Flow<List<LifeEventEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoryFragment(entity: StoryFragmentEntity)

    @Query("SELECT * FROM story_fragments ORDER BY timestampEpochMillis DESC LIMIT :limit")
    fun observeStoryFragments(limit: Int): Flow<List<StoryFragmentEntity>>

    @Query("SELECT * FROM story_fragments ORDER BY timestampEpochMillis DESC LIMIT 1")
    fun observeLatestStory(): Flow<StoryFragmentEntity?>

    @Query("SELECT * FROM world_state_meta WHERE id = 1")
    fun observeWorldMeta(): Flow<WorldStateMetaEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWorldMeta(entity: WorldStateMetaEntity)

    @Query("SELECT * FROM behavior_signals WHERE id = 1")
    fun observeBehaviorSignals(): Flow<BehaviorSignalsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBehaviorSignals(entity: BehaviorSignalsEntity)

    @Query("SELECT * FROM dream_entries ORDER BY timestampEpochMillis DESC")
    fun observeDreamEntries(): Flow<List<DreamEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDreamEntry(entity: DreamEntryEntity)
}
