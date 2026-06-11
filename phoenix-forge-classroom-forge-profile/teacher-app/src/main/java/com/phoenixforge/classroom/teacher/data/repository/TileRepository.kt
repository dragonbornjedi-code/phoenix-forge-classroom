package com.phoenixforge.classroom.teacher.data.repository

import com.phoenixforge.classroom.teacher.data.local.IntentTileDao
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainCatalog
import com.phoenixforge.classroom.teacher.domain.curriculum.FieldGuideDefaults
import com.phoenixforge.classroom.teacher.domain.curriculum.StarterLessonsPack01
import com.phoenixforge.classroom.teacher.domain.curriculum.StarterLesson
import com.phoenixforge.classroom.teacher.domain.lesson.LessonPlanDraft
import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.StewardReflection
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

    suspend fun markComplete(id: String, notes: String, reflection: StewardReflection = StewardReflection()) {
        val tile = dao.findById(id) ?: return
        val fields = reflection.toTileFields()
        dao.update(
            tile.copy(
                status = TileStatus.COMPLETED.name,
                completedAt = System.currentTimeMillis(),
                evidenceNotes = notes.trim(),
                reflectionMental = fields.reflectionMental,
                reflectionEmotional = fields.reflectionEmotional,
                reflectionPhysical = fields.reflectionPhysical,
                reflectionEducational = fields.reflectionEducational,
                reflectionBehavioral = fields.reflectionBehavioral,
            )
        )
    }

    suspend fun delete(id: String) = dao.deleteById(id)

    suspend fun persistSortOrder(tiles: List<IntentTile>) {
        val normalized = tiles.mapIndexed { index, tile -> tile.copy(sortOrder = index) }
        dao.updateAll(normalized)
    }

    suspend fun createFromLessonPlan(plan: LessonPlanDraft): IntentTile {
        val forgeDomain = CurriculumDomainCatalog.forgeDomainFor(plan.domainId)
        val tile = IntentTile(
            title = plan.title,
            description = plan.objective,
            domain = forgeDomain.name,
            curriculumDomainId = plan.domainId.name,
            studentMission = plan.studentMission,
            lessonPatternId = plan.lessonPatternId,
            materials = plan.materials,
            coachingCues = buildString {
                append("Narrative hook: ${plan.narrativeHook}\n")
                append("Quest: ${plan.questTitle}\n")
                append("Steps: ${plan.steps.joinToString(" → ")}")
            },
            fieldGuideExamples = plan.steps.joinToString("\n") { "• $it" },
            fieldGuideSupports = plan.supports,
            fieldGuideRecovery = plan.recovery,
        )
        save(tile)
        return tile
    }

    suspend fun createFromStarterLesson(lesson: StarterLesson): IntentTile {
        val forgeDomain = CurriculumDomainCatalog.forgeDomainFor(lesson.domainId)
        val guide = FieldGuideDefaults.fromStarterLesson(lesson)
        val tile = IntentTile(
            title = lesson.title,
            description = lesson.objective,
            domain = forgeDomain.name,
            curriculumDomainId = lesson.domainId.name,
            starterLessonId = lesson.id,
            studentMission = lesson.studentMission,
            lessonPatternId = lesson.lessonPatternId,
            materials = guide.materials,
            coachingCues = guide.coachingCues,
            fieldGuideExamples = guide.examples,
            evidenceNotes = guide.evidenceNotes,
            fieldGuideSupports = guide.supports,
            fieldGuideRecovery = guide.recovery,
            routineKind = guide.routineKind,
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
        FieldGuideDefaults.seedTiles.forEachIndexed { index, seed ->
            dao.upsert(
                IntentTile(
                    title = seed.title,
                    description = seed.description,
                    domain = seed.domain.name,
                    studentMission = seed.studentMission,
                    materials = seed.materials,
                    coachingCues = seed.coachingCues,
                    fieldGuideExamples = seed.examples,
                    evidenceNotes = seed.evidenceNotes,
                    fieldGuideSupports = seed.supports,
                    fieldGuideRecovery = seed.recovery,
                    lessonPatternId = seed.lessonPatternId,
                    routineKind = seed.routineKind,
                    sortOrder = index,
                )
            )
        }
    }

    /** Backfill empty field guides on tiles created before pre-fill shipped. */
    suspend fun backfillEmptyFieldGuides(): Int {
        var updated = 0
        val tiles = dao.observeAll().first()
        for (tile in tiles) {
            if (tile.materials.isNotBlank() && tile.coachingCues.isNotBlank()) continue
            val seed = FieldGuideDefaults.seedTiles.firstOrNull { it.title.equals(tile.title, ignoreCase = true) }
                ?: tile.starterLessonId?.let { id ->
                    StarterLessonsPack01.lessons.firstOrNull { it.id == id }
                        ?.let(FieldGuideDefaults::fromStarterLesson)
                }
                ?: continue
            dao.update(
                tile.copy(
                    materials = tile.materials.ifBlank { seed.materials },
                    coachingCues = tile.coachingCues.ifBlank { seed.coachingCues },
                    fieldGuideExamples = tile.fieldGuideExamples.ifBlank { seed.examples },
                    evidenceNotes = tile.evidenceNotes.ifBlank { seed.evidenceNotes },
                    fieldGuideSupports = tile.fieldGuideSupports.ifBlank { seed.supports },
                    fieldGuideRecovery = tile.fieldGuideRecovery.ifBlank { seed.recovery },
                    studentMission = tile.studentMission.ifBlank { seed.studentMission },
                    routineKind = tile.routineKind.ifBlank { seed.routineKind },
                    lessonPatternId = tile.lessonPatternId.ifBlank { seed.lessonPatternId },
                )
            )
            updated++
        }
        return updated
    }
}
