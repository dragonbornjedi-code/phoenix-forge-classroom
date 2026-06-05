package com.phoenixforge.classroom.teacher.domain.lesson

import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainId

data class LessonPlanDraft(
    val domainId: CurriculumDomainId,
    val subdomainId: String,
    val subdomainName: String,
    val title: String,
    val objective: String,
    val studentMission: String,
    val materials: String,
    val steps: List<String>,
    val questions: List<String>,
    val durationStandard: String,
    val durationLowDemand: String,
    val supports: String,
    val recovery: String,
    val narrativeHook: String,
    val questTitle: String,
    val questDescription: String,
    val lessonPatternId: String
)

data class QuestDraft(
    val title: String,
    val description: String,
    val narrativeHook: String,
    val studentMission: String,
    val xpReward: Int = 25,
    val sparkReactionSeed: String
)
