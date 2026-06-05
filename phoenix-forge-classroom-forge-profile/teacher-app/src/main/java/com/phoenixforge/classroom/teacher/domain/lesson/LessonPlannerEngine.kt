package com.phoenixforge.classroom.teacher.domain.lesson

import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainCatalog
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainId
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumSubdomain
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonPlannerEngine @Inject constructor() {

    fun subdomainOptions(domainId: CurriculumDomainId): List<CurriculumSubdomain> =
        CurriculumDomainCatalog.domainById(domainId)?.subdomains.orEmpty()

    fun generatePlan(domainId: CurriculumDomainId, subdomainId: String): LessonPlanDraft? {
        val domain = CurriculumDomainCatalog.domainById(domainId) ?: return null
        val subdomain = domain.subdomains.firstOrNull { it.id == subdomainId } ?: return null
        val pattern = domain.lessonPatterns.firstOrNull() ?: "Guided Practice"
        val patternSlug = pattern.lowercase().replace(' ', '_')
        val topicSample = subdomain.topics.take(4).joinToString(", ")
        val metric = domain.progressMetrics.firstOrNull() ?: "Observable skill use"

        val steps = buildList {
            add("Frame: \"Today we practice ${subdomain.name.lowercase()} — ${subdomain.summary.lowercase()}\"")
            add("Model: demonstrate one ${subdomain.topics.firstOrNull() ?: "topic"} with objects in the room.")
            subdomain.topics.drop(1).take(3).forEachIndexed { i, topic ->
                add("Practice ${i + 2}: child tries $topic with your cue.")
            }
            add("Retrieve: ask one question from the ${pattern} pattern before closing.")
            add("Evidence: photo or short note — $metric")
        }

        val questDraft = QuestDraftGenerator.fromSubdomain(subdomain, domainId)

        return LessonPlanDraft(
            domainId = domainId,
            subdomainId = subdomain.id,
            subdomainName = subdomain.name,
            title = "${subdomain.name} — ${pattern}",
            objective = "Ezra will demonstrate ${subdomain.name.lowercase()}: $metric.",
            studentMission = questDraft.studentMission,
            materials = "Household objects for: $topicSample. Optional visual cards.",
            steps = steps,
            questions = domain.teacherCues.take(3),
            durationStandard = "12–18 minutes",
            durationLowDemand = "5 minutes · one topic only",
            supports = domain.supportMethods.take(2).joinToString(" "),
            recovery = CurriculumDomainCatalog.designRules.lastOrNull()
                ?: "If frustration rises, shrink the step and retry tomorrow.",
            narrativeHook = questDraft.narrativeHook,
            questTitle = questDraft.title,
            questDescription = questDraft.description,
            lessonPatternId = patternSlug
        )
    }
}
