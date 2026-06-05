package com.phoenixforge.classroom.teacher.data.repository

import com.phoenixforge.classroom.teacher.data.local.IntentTileDao
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainCatalog
import com.phoenixforge.classroom.teacher.domain.curriculum.StarterLessonsPack01
import com.phoenixforge.classroom.teacher.domain.curriculum.StarterLesson
import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.TileStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TileRepository @Inject constructor(
    private val dao: IntentTileDao
) {
    fun observeAll(): Flow<List<IntentTile>> = dao.observeAll()

    suspend fun findById(id: String): IntentTile? = dao.findById(id)

    suspend fun save(tile: IntentTile) {
        val order = if (tile.sortOrder == 0) {
            (dao.observeAll().first().maxOfOrNull { it.sortOrder } ?: -1) + 1
        } else {
            tile.sortOrder
        }
        dao.upsert(tile.copy(sortOrder = order))
    }

    suspend fun update(tile: IntentTile) = dao.update(tile)

    suspend fun markComplete(id: String, notes: String) {
        val tile = dao.findById(id) ?: return
        dao.update(
            tile.copy(
                status = TileStatus.COMPLETED.name,
                completedAt = System.currentTimeMillis(),
                evidenceNotes = notes.trim()
            )
        )
    }

    suspend fun delete(id: String) = dao.deleteById(id)

    suspend fun createFromStarterLesson(lesson: StarterLesson): IntentTile {
        val forgeDomain = CurriculumDomainCatalog.forgeDomainFor(lesson.domainId)
        val tile = IntentTile(
            title = lesson.title,
            description = lesson.objective,
            domain = forgeDomain.name,
            curriculumDomainId = lesson.domainId.name,
            starterLessonId = lesson.id,
            studentMission = lesson.studentMission,
            lessonPatternId = lesson.lessonPatternId,
            materials = lesson.materials,
            coachingCues = buildString {
                append("Recovery: ${lesson.recovery}\n")
                append("Make easier: ${lesson.makeEasier}\n")
                append("Supports: ${lesson.supports}")
            }
        )
        save(tile)
        return tile
    }

    suspend fun importPack01IfEmpty(): Int {
        val existing = dao.observeAll().first()
        val alreadyImported = existing.any { it.starterLessonId?.startsWith("pack01_") == true }
        if (alreadyImported) return 0
        StarterLessonsPack01.lessons.forEach { lesson ->
            createFromStarterLesson(lesson)
        }
        return StarterLessonsPack01.lessons.size
    }

    suspend fun hasPack01Tiles(): Boolean =
        dao.observeAll().first().any { it.starterLessonId?.startsWith("pack01_") == true }

    suspend fun ensureSeedData() {
        if (dao.observeAll().first().isNotEmpty()) return
        val samples = listOf(
            Triple("Morning circle", "Connection and calm start", ForgeDomain.SOCIAL),
            Triple("Nature walk", "Outdoor motor and observation", ForgeDomain.MOTOR),
            Triple("Reading nest", "Literacy in a quiet space", ForgeDomain.LANGUAGE),
            Triple("Build project", "Creation and problem-solving", ForgeDomain.CREATIVE)
        )
        samples.forEachIndexed { index, (title, desc, domain) ->
            dao.upsert(
                IntentTile(
                    title = title,
                    description = desc,
                    domain = domain.name,
                    sortOrder = index
                )
            )
        }
    }
}
