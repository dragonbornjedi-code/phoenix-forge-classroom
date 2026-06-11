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

    fun lessonsForSubdomain(domainId: CurriculumDomainId, subdomain: CurriculumSubdomain): List<StarterLesson> {
        val tokens = (listOf(subdomain.name, subdomain.id.replace('_', ' ')) + subdomain.topics)
            .map { it.lowercase().trim() }
            .filter { it.isNotEmpty() }
        return StarterLessonsPack01.lessons.filter { lesson ->
            if (lesson.domainId != domainId) return@filter false
            lesson.subcategories.any { sub ->
                val lower = sub.lowercase()
                tokens.any { token -> lower.contains(token) || token.contains(lower) }
            }
        }
    }

    fun subdomain(id: CurriculumDomainId, subdomainId: String): CurriculumSubdomain? =
        domain(id)?.subdomains?.firstOrNull { it.id == subdomainId }
}
