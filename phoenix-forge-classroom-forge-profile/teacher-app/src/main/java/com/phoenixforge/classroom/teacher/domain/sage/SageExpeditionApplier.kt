package com.phoenixforge.classroom.teacher.domain.sage

import com.phoenixforge.classroom.teacher.data.repository.TileRepository
import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.TileStatus
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SageExpeditionApplier @Inject constructor(
    private val tileRepository: TileRepository,
) {
    suspend fun apply(actions: List<SageTileAction>): SageApplyResult {
        if (actions.isEmpty()) {
            return SageApplyResult(0, 0, 0, "No expedition actions to apply.")
        }

        var created = 0
        var updated = 0
        var skipped = 0
        val createdTileIds = mutableListOf<String>()
        val tiles = tileRepository.observeAll().first()

        actions.forEach { action ->
            when (action.op.lowercase()) {
                "create" -> {
                    val title = action.title?.trim().orEmpty()
                    if (title.isBlank()) {
                        skipped++
                        return@forEach
                    }
                    val tile = action.toNewTile()
                    tileRepository.save(tile)
                    createdTileIds += tile.id
                    created++
                }
                "update" -> {
                    val existing = findTile(tiles, action)
                    if (existing == null) {
                        skipped++
                        return@forEach
                    }
                    tileRepository.update(existing.merge(action))
                    updated++
                }
                else -> skipped++
            }
        }

        val message = buildString {
            append("Applied to Expedition Board: ")
            append("$created created")
            if (updated > 0) append(", $updated updated")
            if (skipped > 0) append(", $skipped skipped")
            append(". Open Expedition to review, reorder, then Push today's stack.")
        }

        return SageApplyResult(created, updated, skipped, message, createdTileIds)
    }

    private fun findTile(tiles: List<IntentTile>, action: SageTileAction): IntentTile? {
        action.matchId?.trim()?.takeIf { it.isNotEmpty() }?.let { id ->
            tiles.firstOrNull { it.id == id }?.let { return it }
        }
        val title = action.matchTitle?.trim()?.takeIf { it.isNotEmpty() }
            ?: action.title?.trim()?.takeIf { it.isNotEmpty() }
        if (title == null) return null
        return tiles.firstOrNull { it.title.equals(title, ignoreCase = true) }
    }

    private fun SageTileAction.toNewTile(): IntentTile = IntentTile(
        title = title!!.trim(),
        description = description?.trim().orEmpty(),
        domain = ForgeDomain.fromName(domain ?: ForgeDomain.LANGUAGE.name).name,
        studentMission = studentMission?.trim().orEmpty(),
        materials = materials?.trim().orEmpty(),
        coachingCues = coachingCues?.trim().orEmpty(),
        fieldGuideExamples = fieldGuideExamples?.trim().orEmpty(),
        fieldGuideSupports = fieldGuideSupports?.trim().orEmpty(),
        fieldGuideRecovery = fieldGuideRecovery?.trim().orEmpty(),
        routineKind = routineKind?.trim().orEmpty(),
        lessonPatternId = lessonPatternId?.trim().orEmpty(),
        status = TileStatus.PLANNED.name,
    )

    private fun IntentTile.merge(action: SageTileAction): IntentTile = copy(
        title = action.title?.trim()?.takeIf { it.isNotEmpty() } ?: title,
        description = action.description?.trim()?.takeIf { it.isNotEmpty() } ?: description,
        domain = action.domain?.let { ForgeDomain.fromName(it).name } ?: domain,
        studentMission = action.studentMission?.trim()?.takeIf { it.isNotEmpty() } ?: studentMission,
        materials = action.materials?.trim()?.takeIf { it.isNotEmpty() } ?: materials,
        coachingCues = action.coachingCues?.trim()?.takeIf { it.isNotEmpty() } ?: coachingCues,
        fieldGuideExamples = action.fieldGuideExamples?.trim()?.takeIf { it.isNotEmpty() } ?: fieldGuideExamples,
        fieldGuideSupports = action.fieldGuideSupports?.trim()?.takeIf { it.isNotEmpty() } ?: fieldGuideSupports,
        fieldGuideRecovery = action.fieldGuideRecovery?.trim()?.takeIf { it.isNotEmpty() } ?: fieldGuideRecovery,
        routineKind = action.routineKind?.trim()?.takeIf { it.isNotEmpty() } ?: routineKind,
        lessonPatternId = action.lessonPatternId?.trim()?.takeIf { it.isNotEmpty() } ?: lessonPatternId,
    )
}
