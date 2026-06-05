package com.phoenixforge.classroom.teacher.domain.curriculum

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurriculumRepository @Inject constructor() {

    fun designRules(): List<String> = CurriculumDomainCatalog.designRules

    fun allDomains(): List<CurriculumDomain> = CurriculumDomainCatalog.domains

    fun domain(id: CurriculumDomainId): CurriculumDomain? =
        CurriculumDomainCatalog.domainById(id)

    fun pack01Lessons(): List<StarterLesson> = StarterLessonsPack01.lessons

    fun starterLesson(id: String): StarterLesson? = StarterLessonsPack01.lessonById(id)

    fun lessonsForDomain(domainId: CurriculumDomainId): List<StarterLesson> =
        StarterLessonsPack01.lessonsForDomain(domainId)
}
