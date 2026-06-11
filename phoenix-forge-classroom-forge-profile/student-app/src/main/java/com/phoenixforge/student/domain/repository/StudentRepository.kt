package com.phoenixforge.student.domain.repository

import com.phoenixforge.student.domain.model.HouseState
import com.phoenixforge.student.domain.model.ImportedProfileSnapshot
import com.phoenixforge.student.domain.model.LifeEvent
import com.phoenixforge.student.domain.model.MemoryArtifact
import com.phoenixforge.student.domain.model.MemoryEventDraft
import com.phoenixforge.student.domain.model.NpcState
import com.phoenixforge.student.domain.model.Quest
import com.phoenixforge.student.domain.model.BehaviorSignals
import com.phoenixforge.student.domain.model.DreamEntry
import com.phoenixforge.student.domain.model.StoryFragment
import com.phoenixforge.student.domain.model.StudentProgress
import com.phoenixforge.student.domain.model.WorldDriftState
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    fun observeProgress(): Flow<StudentProgress>
    suspend fun saveProgress(progress: StudentProgress)

    fun observeHouse(): Flow<HouseState>
    suspend fun saveHouse(state: HouseState)

    fun observeMemories(): Flow<List<MemoryArtifact>>
    fun observeRecentMemories(limit: Int): Flow<List<MemoryArtifact>>
    suspend fun saveMemory(memory: MemoryArtifact)

    fun observeMemoryEventDrafts(): Flow<List<MemoryEventDraft>>
    fun observeMemoryEventDraftCount(): Flow<Int>
    suspend fun listMemoryEventDraftIds(): List<String>
    suspend fun insertMemoryEventDraftIfNew(draft: MemoryEventDraft): Boolean

    fun observeNpcs(): Flow<List<NpcState>>
    suspend fun saveNpc(npc: NpcState)

    fun observeQuests(): Flow<List<Quest>>
    suspend fun saveQuest(quest: Quest)

    fun observeImportedProfiles(): Flow<List<ImportedProfileSnapshot>>
    suspend fun saveImportedProfile(snapshot: ImportedProfileSnapshot)

    suspend fun recordLifeEvent(event: LifeEvent)
    fun observeLifeEvents(limit: Int): Flow<List<LifeEvent>>

    fun observeStoryFragments(limit: Int): Flow<List<StoryFragment>>
    fun observeLatestStory(): Flow<StoryFragment?>
    suspend fun saveStoryFragment(fragment: StoryFragment)

    fun observeWorldMeta(): Flow<Triple<String?, String, WorldDriftState?>>
    suspend fun saveWorldMeta(lastActivity: String?, environmentTheme: String, drift: WorldDriftState?)

    fun observeBehaviorSignals(): Flow<BehaviorSignals>
    suspend fun saveBehaviorSignals(signals: BehaviorSignals)

    fun observeDreamEntries(): Flow<List<DreamEntry>>
    suspend fun saveDreamEntry(entry: DreamEntry)
}
