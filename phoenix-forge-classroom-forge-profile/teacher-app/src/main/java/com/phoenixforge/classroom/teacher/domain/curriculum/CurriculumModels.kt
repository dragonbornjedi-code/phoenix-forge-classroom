package com.phoenixforge.classroom.teacher.domain.curriculum

data class CurriculumDomain(
    val id: CurriculumDomainId,
    val teacherFraming: String,
    val studentFraming: String,
    val lessonPatterns: List<String>,
    val progressMetrics: List<String>,
    val teacherCues: List<String>,
    val supportMethods: List<String>,
    val subcategoryCount: Int
)

data class StarterLesson(
    val id: String,
    val title: String,
    val domainId: CurriculumDomainId,
    val subcategories: List<String>,
    val primaryMethods: List<String>,
    val studentMission: String,
    val durationStandard: String,
    val durationLowDemand: String,
    val materials: String,
    val objective: String,
    val whyItMatters: String,
    val setup: String,
    val steps: List<String>,
    val questions: List<String>,
    val metric: String,
    val evidence: String,
    val supports: String,
    val makeEasier: String,
    val makeHarder: String,
    val recovery: String,
    val studentEditionNotes: String,
    val lessonPatternId: String
)

data class WeeklyAuditSectionEntry(
    val domainId: CurriculumDomainId,
    val winObserved: String = "",
    val frictionPoint: String = "",
    val methodWorked: String = "",
    val methodToTryNext: String = "",
    val metricToTrack: String = ""
)

data class WeeklyAuditOverall(
    val bestEnergyWindow: String = "",
    val hardestTransition: String = "",
    val strongestMotivator: String = "",
    val overloadSigns: String = "",
    val nextAdjustment: String = ""
)

data class WeeklyAuditDraft(
    val sections: Map<CurriculumDomainId, WeeklyAuditSectionEntry> = emptyMap(),
    val overall: WeeklyAuditOverall = WeeklyAuditOverall(),
    val savedAtEpochMs: Long = 0L
)
