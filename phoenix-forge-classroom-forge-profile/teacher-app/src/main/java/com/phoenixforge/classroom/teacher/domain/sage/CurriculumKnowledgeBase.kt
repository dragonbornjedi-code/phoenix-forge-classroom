package com.phoenixforge.classroom.teacher.domain.sage

import com.phoenixforge.classroom.teacher.data.local.CurriculumAuditStore
import com.phoenixforge.classroom.teacher.data.repository.TileRepository
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainCatalog
import com.phoenixforge.classroom.teacher.domain.curriculum.StarterLessonsPack01
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurriculumKnowledgeBase @Inject constructor(
    private val tileRepository: TileRepository,
    private val auditStore: CurriculumAuditStore
) {
    suspend fun buildContext(maxChars: Int = 24_000): String {
        val sb = StringBuilder()
        sb.appendLine("DESIGN RULES:")
        CurriculumDomainCatalog.designRules.forEach { sb.appendLine("- $it") }
        sb.appendLine()

        CurriculumDomainCatalog.domains.forEach { domain ->
            sb.appendLine("DOMAIN ${domain.id.sectionNumber}: ${domain.id.emoji} ${domain.id.displayName}")
            sb.appendLine("Focus: ${domain.focusLine}")
            sb.appendLine("Subdomains (${domain.subcategoryCount}):")
            domain.subdomains.forEach { sub ->
                sb.appendLine("  • ${sub.name} [${sub.id}]: ${sub.summary}")
                if (sub.topics.isNotEmpty()) {
                    sb.appendLine("    Topics: ${sub.topics.joinToString(", ")}")
                }
            }
            sb.appendLine()
        }

        sb.appendLine("PACK 01 STARTER LESSONS:")
        StarterLessonsPack01.lessons.forEach { lesson ->
            sb.appendLine("- ${lesson.title} (${lesson.domainId.displayName}): ${lesson.objective}")
            sb.appendLine("  Mission: ${lesson.studentMission}")
        }
        sb.appendLine()

        val tiles = tileRepository.observeAll().first()
        if (tiles.isNotEmpty()) {
            sb.appendLine("CURRENT EXPEDITION TILES:")
            tiles.take(20).forEach { tile ->
                sb.appendLine("- ${tile.title} [${tile.status}]: ${tile.description}")
                if (tile.studentMission.isNotBlank()) sb.appendLine("  Student mission: ${tile.studentMission}")
            }
            sb.appendLine()
        }

        val audit = auditStore.loadDraft()
        if (audit.savedAtEpochMs > 0L) {
            sb.appendLine("SAVED WEEKLY AUDIT NOTES:")
            audit.sections.values.forEach { section ->
                if (section.winObserved.isNotBlank() || section.frictionPoint.isNotBlank()) {
                    sb.appendLine("- ${section.domainId.displayName}: win=${section.winObserved}; friction=${section.frictionPoint}")
                }
            }
            sb.appendLine()
        }

        val text = sb.toString()
        return if (text.length <= maxChars) text else text.take(maxChars) + "\n…(truncated)"
    }
}
