package com.phoenixforge.student.ui.quests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.phoenixforge.student.domain.engine.QuestEngine
import com.phoenixforge.student.domain.model.Quest
import com.phoenixforge.student.domain.progression.ProgressionEngine
import com.phoenixforge.student.domain.progression.ProgressionOutcome
import com.phoenixforge.student.sync.EventWriter
import com.phoenixforge.student.sync.EventWriteResult
import com.phoenixforge.student.sync.ManifestReadResult
import com.phoenixforge.student.sync.ManifestReader
import com.phoenixforge.student.sync.QuestRoutineCategory
import com.phoenixforge.student.sync.StudentRoutineCatalog
import com.phoenixforge.student.sync.TodayManifestItem
import com.phoenixforge.student.sync.todayItems
import com.phoenixforge.student.ui.theme.StudentKidCopy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuestsUiState(
    val isLoading: Boolean = true,
    val selectedCategory: QuestRoutineCategory? = null,
    val items: List<TodayManifestItem> = emptyList(),
    val sideQuests: List<Quest> = emptyList(),
    val manifestQuestIds: Set<String> = emptySet(),
    val statusMessage: String? = null,
    val sourcePath: String? = null,
    val selectedItem: TodayManifestItem? = null,
    val selectedSideQuest: Quest? = null,
    val questRunStatuses: Map<String, QuestRunStatus> = emptyMap(),
    val eventWriting: Boolean = false,
    val eventFeedback: String? = null,
)

@HiltViewModel
class QuestsViewModel @Inject constructor(
    private val manifestReader: ManifestReader,
    private val eventWriter: EventWriter,
    private val progressionEngine: ProgressionEngine,
    private val questEngine: QuestEngine,
) : ViewModel() {

    private val _state = MutableStateFlow(QuestsUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            questEngine.observeActiveQuests().collect { active ->
                val manifestIds = _state.value.manifestQuestIds
                _state.update { it.copy(sideQuests = filterSideQuests(active, manifestIds)) }
            }
        }
        viewModelScope.launch {
            questEngine.observeQuests().collect { all ->
                _state.update { prev ->
                    prev.copy(questRunStatuses = mergeRunStatuses(prev.questRunStatuses, sideQuestRunStatuses(all)))
                }
            }
        }
        refresh()
    }

    fun ensureCategory(category: QuestRoutineCategory) {
        if (_state.value.selectedCategory != category) {
            openCategory(category)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            val category = _state.value.selectedCategory
            val manifestIds = loadManifestQuestIds()
            val sideQuests = filterSideQuests(questEngine.observeActiveQuests().first(), manifestIds)
            _state.update {
                it.copy(
                    isLoading = true,
                    selectedItem = null,
                    selectedSideQuest = null,
                    eventFeedback = null,
                    manifestQuestIds = manifestIds,
                    sideQuests = sideQuests,
                )
            }

            if (category == QuestRoutineCategory.MORNING || category == QuestRoutineCategory.NIGHT) {
                val items = StudentRoutineCatalog.itemsFor(category)
                _state.update {
                    it.copy(
                        isLoading = false,
                        selectedCategory = category,
                        items = items,
                        statusMessage = null,
                        sourcePath = "built-in routine",
                    )
                }
                return@launch
            }

            when (val result = manifestReader.readActiveManifest()) {
                is ManifestReadResult.Ready -> {
                    val items = result.manifest.todayItems(category = category)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedCategory = category,
                            items = items,
                            statusMessage = when {
                                category == null -> null
                                items.isEmpty() -> emptyCategoryMessage(category)
                                else -> null
                            },
                            sourcePath = result.sourcePath,
                        )
                    }
                }
                ManifestReadResult.NotSignedIn -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedCategory = category,
                            statusMessage = StudentKidCopy.questNeedHero(),
                        )
                    }
                }
                is ManifestReadResult.NotFound -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedCategory = category,
                            statusMessage = StudentKidCopy.questEmptyDaily(),
                        )
                    }
                }
                is ManifestReadResult.Invalid -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            selectedCategory = category,
                            statusMessage = result.reason,
                            sourcePath = result.sourcePath,
                        )
                    }
                }
            }
        }
    }

    fun openCategory(category: QuestRoutineCategory) {
        _state.update { it.copy(selectedCategory = category) }
        refresh()
    }

    fun backToHub() {
        _state.update {
            it.copy(
                selectedCategory = null,
                items = emptyList(),
                statusMessage = null,
                selectedItem = null,
                selectedSideQuest = null,
                eventFeedback = null,
            )
        }
        refresh()
    }

    fun selectItem(item: TodayManifestItem?) {
        _state.update { it.copy(selectedItem = item, selectedSideQuest = null, eventFeedback = null) }
    }

    fun selectSideQuest(quest: Quest) {
        _state.update { it.copy(selectedSideQuest = quest, selectedItem = null, eventFeedback = null) }
    }

    fun dismissDetail() {
        _state.update { it.copy(selectedItem = null, selectedSideQuest = null, eventFeedback = null) }
    }

    fun startSelectedQuest() {
        val item = _state.value.selectedItem ?: return
        viewModelScope.launch {
            _state.update { it.copy(eventWriting = true, eventFeedback = null) }
            val result = eventWriter.writeQuestStarted(item.questId)
            _state.update {
                it.copy(
                    eventWriting = false,
                    eventFeedback = result.fold(
                        onSuccess = { r -> r.message },
                        onFailure = { e -> e.message ?: "Could not write QUEST_STARTED event." },
                    ),
                    questRunStatuses = markRunStatus(it.questRunStatuses, item.questId, QuestRunStatus.IN_PROGRESS),
                )
            }
        }
    }

    fun completeSelectedQuest() {
        val item = _state.value.selectedItem ?: return
        viewModelScope.launch {
            _state.update { it.copy(eventWriting = true, eventFeedback = null) }
            val questResult = eventWriter.writeQuestCompleted(item.questId)
            val rewardResult = progressionEngine.applyQuestCompletedRewards(item.questId)
            _state.update {
                it.copy(
                    eventWriting = false,
                    eventFeedback = buildEventFeedback(questResult, rewardResult),
                    questRunStatuses = markRunStatus(it.questRunStatuses, item.questId, QuestRunStatus.DONE),
                )
            }
        }
    }

    fun startSelectedSideQuest() {
        val quest = _state.value.selectedSideQuest ?: return
        viewModelScope.launch {
            _state.update { it.copy(eventWriting = true, eventFeedback = null) }
            val result = eventWriter.writeQuestStarted(quest.id)
            _state.update {
                it.copy(
                    eventWriting = false,
                    eventFeedback = result.fold(
                        onSuccess = { r -> r.message },
                        onFailure = { e -> e.message ?: "Could not write QUEST_STARTED event." },
                    ),
                    questRunStatuses = markRunStatus(it.questRunStatuses, quest.id, QuestRunStatus.IN_PROGRESS),
                )
            }
        }
    }

    fun completeSelectedSideQuest() {
        val quest = _state.value.selectedSideQuest ?: return
        viewModelScope.launch {
            _state.update { it.copy(eventWriting = true, eventFeedback = null) }
            questEngine.completeQuest(quest.id)
            val questResult = eventWriter.writeQuestCompleted(quest.id)
            val rewardResult = progressionEngine.applyQuestCompletedRewards(quest.id)
            _state.update {
                it.copy(
                    eventWriting = false,
                    eventFeedback = buildEventFeedback(questResult, rewardResult),
                    selectedSideQuest = null,
                    questRunStatuses = markRunStatus(it.questRunStatuses, quest.id, QuestRunStatus.DONE),
                )
            }
        }
    }

    private suspend fun loadManifestQuestIds(): Set<String> = when (val result = manifestReader.readActiveManifest()) {
        is ManifestReadResult.Ready ->
            result.manifest.todayItems(category = null).map { it.questId }.toSet()
        else -> emptySet()
    }

    private fun filterSideQuests(active: List<Quest>, manifestIds: Set<String>): List<Quest> =
        active.filter { it.id !in manifestIds }

    private fun markRunStatus(
        current: Map<String, QuestRunStatus>,
        questId: String,
        status: QuestRunStatus,
    ): Map<String, QuestRunStatus> = current + (questId to status)

    private fun mergeRunStatuses(
        session: Map<String, QuestRunStatus>,
        room: Map<String, QuestRunStatus>,
    ): Map<String, QuestRunStatus> {
        val merged = room.toMutableMap()
        session.forEach { (id, status) ->
            val existing = merged[id]
            if (existing == null || status.ordinal > existing.ordinal) {
                merged[id] = status
            }
        }
        return merged
    }

    private fun buildEventFeedback(
        questResult: Result<EventWriteResult>,
        rewardResult: Result<ProgressionOutcome>,
    ): String = buildString {
        questResult.fold(
            onSuccess = { append(it.message) },
            onFailure = { append(it.message ?: "Quest event failed.") },
        )
        rewardResult.fold(
            onSuccess = { append(" · "); append(it.message) },
            onFailure = { append(" · "); append(it.message ?: "Rewards failed.") },
        )
    }

    private fun emptyCategoryMessage(category: QuestRoutineCategory): String = when (category) {
        QuestRoutineCategory.MORNING -> StudentKidCopy.questEmptyMorning()
        QuestRoutineCategory.NIGHT -> StudentKidCopy.questEmptyNight()
        QuestRoutineCategory.DAILY -> StudentKidCopy.questEmptyDaily()
    }
}
