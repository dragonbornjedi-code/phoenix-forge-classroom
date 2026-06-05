package com.phoenixforge.classroom.teacher.domain.lesson

import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainId
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumSubdomain

object QuestDraftGenerator {

    fun fromSubdomain(subdomain: CurriculumSubdomain, domainId: CurriculumDomainId): QuestDraft {
        val hook = narrativeHook(subdomain, domainId)
        val mission = studentMission(subdomain)
        return QuestDraft(
            title = "${subdomain.name} Mission",
            description = "${hook}\n\n$mission",
            narrativeHook = hook,
            studentMission = mission,
            sparkReactionSeed = sparkSeed(subdomain)
        )
    }

    fun fromPlan(plan: LessonPlanDraft): QuestDraft = QuestDraft(
        title = plan.questTitle,
        description = plan.questDescription,
        narrativeHook = plan.narrativeHook,
        studentMission = plan.studentMission,
        sparkReactionSeed = "Spark noticed your ${plan.subdomainName} work — want to show me?"
    )

    private fun narrativeHook(subdomain: CurriculumSubdomain, domainId: CurriculumDomainId): String =
        when (domainId) {
            CurriculumDomainId.COGNITIVE_ACADEMIC ->
                "A clue about ${subdomain.topics.firstOrNull() ?: subdomain.name} appeared in the study nook."
            CurriculumDomainId.EMOTIONAL_REGULATION ->
                "The house feels ${subdomain.topics.firstOrNull()?.lowercase() ?: "buzzy"} — time to read the signals."
            CurriculumDomainId.PRACTICAL_LIFE ->
                "Something in the home needs a ${subdomain.name.lowercase()} fix before lunch."
            CurriculumDomainId.SOCIAL_DYNAMICS ->
                "Two friends in the story both want the same thing — sound familiar?"
            CurriculumDomainId.PHYSICAL_MASTERY ->
                "The garden path has a ${subdomain.topics.firstOrNull()?.lowercase() ?: "challenge"} waiting outside."
            CurriculumDomainId.CREATIVE_CURIOSITY ->
                "A blank page is humming — it wants your ${subdomain.name.lowercase()} idea."
            CurriculumDomainId.ETHICS_CIVIC ->
                "The neighborhood question board posted: \"What is fair about ${subdomain.name.lowercase()}?\""
        }

    private fun studentMission(subdomain: CurriculumSubdomain): String {
        val topic = subdomain.topics.firstOrNull() ?: subdomain.name
        return "Try $topic in real life, then tell Spark one thing you noticed."
    }

    private fun sparkSeed(subdomain: CurriculumSubdomain): String =
        "I saw you working on ${subdomain.name} — that matters."
}
