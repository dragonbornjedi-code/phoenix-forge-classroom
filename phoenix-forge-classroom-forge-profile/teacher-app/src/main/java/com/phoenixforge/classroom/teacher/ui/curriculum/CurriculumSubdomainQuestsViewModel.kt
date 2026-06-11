package com.phoenixforge.classroom.teacher.ui.curriculum

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainId
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumRepository
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumSubdomain
import com.phoenixforge.classroom.teacher.domain.curriculum.StarterLesson
import com.phoenixforge.classroom.teacher.domain.lesson.QuestDraft
import com.phoenixforge.classroom.teacher.domain.lesson.QuestDraftGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class SubdomainQuestItem {
    abstract val id: String
    abstract val title: String

    data class PackLesson(val lesson: StarterLesson) : SubdomainQuestItem() {
        override val id: String = lesson.id
        override val title: String = lesson.title
    }

    data class TopicQuest(
        override val id: String,
        override val title: String,
        val topic: String,
        val draft: QuestDraft,
    ) : SubdomainQuestItem()
}

data class CurriculumSubdomainQuestsUiState(
    val domainId: CurriculumDomainId,
    val subdomain: CurriculumSubdomain? = null,
    val quests: List<SubdomainQuestItem> = emptyList(),
    val selectedQuest: SubdomainQuestItem? = null,
)

@HiltViewModel
class CurriculumSubdomainQuestsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val curriculumRepository: CurriculumRepository,
) : ViewModel() {

    private val domainId: CurriculumDomainId = CurriculumDomainId.fromId(
        savedStateHandle["domainId"],
    ) ?: CurriculumDomainId.COGNITIVE_ACADEMIC

    private val subdomainId: String = savedStateHandle["subdomainId"] ?: ""

    private val _state = MutableStateFlow(buildInitialState())
    val state: StateFlow<CurriculumSubdomainQuestsUiState> = _state.asStateFlow()

    private fun buildInitialState(): CurriculumSubdomainQuestsUiState {
        val subdomain = curriculumRepository.subdomain(domainId, subdomainId)
        val quests = buildQuestList(domainId, subdomain)
        return CurriculumSubdomainQuestsUiState(
            domainId = domainId,
            subdomain = subdomain,
            quests = quests,
        )
    }

    private fun buildQuestList(
        domainId: CurriculumDomainId,
        subdomain: CurriculumSubdomain?,
    ): List<SubdomainQuestItem> {
        if (subdomain == null) return emptyList()
        val packLessons = curriculumRepository.lessonsForSubdomain(domainId, subdomain)
            .map { SubdomainQuestItem.PackLesson(it) }
        val baseDraft = QuestDraftGenerator.fromSubdomain(subdomain, domainId)
        val topicQuests = subdomain.topics.mapIndexed { index, topic ->
            SubdomainQuestItem.TopicQuest(
                id = "${subdomain.id}_topic_$index",
                title = "$topic — practice mission",
                topic = topic,
                draft = baseDraft.copy(
                    title = "$topic Mission",
                    studentMission = "Try $topic in real life, then tell Spark one thing you noticed.",
                    narrativeHook = "Today's clue is about $topic inside ${subdomain.name}.",
                ),
            )
        }
        return packLessons + topicQuests
    }

    fun selectQuest(item: SubdomainQuestItem?) {
        _state.update { it.copy(selectedQuest = item) }
    }

    fun dismissDetail() {
        _state.update { it.copy(selectedQuest = null) }
    }
}
