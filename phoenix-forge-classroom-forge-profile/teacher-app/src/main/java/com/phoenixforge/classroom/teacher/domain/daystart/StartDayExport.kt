package com.phoenixforge.classroom.teacher.domain.daystart

import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.TileStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Plain-text export for the five-minute morning launch ritual.
 * Stub for cross-app handoff — copy/share wiring comes later.
 */
object StartDayExport {
    private val dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMM d")

    fun build(tiles: List<IntentTile>, today: LocalDate = LocalDate.now()): String {
        val stack = tiles
            .filter { tile ->
                when (TileStatus.fromName(tile.status)) {
                    TileStatus.PLANNED, TileStatus.ACTIVE, TileStatus.SENT -> true
                    TileStatus.COMPLETED, TileStatus.DEFERRED -> false
                }
            }
            .filter { tile ->
                val kind = tile.routineKind.trim()
                kind != "morning_routine" && kind != "night_routine"
            }
            .sortedBy { it.sortOrder }

        return buildString {
            appendLine("Phoenix Forge Classroom — Start Day")
            appendLine(dateFormatter.format(today))
            appendLine()
            appendLine("Connection: Good morning. Are you ready for system check?")
            appendLine("Launch: Version today is starting. We can patch it anytime.")
            appendLine()
            if (stack.isEmpty()) {
                appendLine("Today's stack: (empty — add intent tiles on the Expedition Board)")
            } else {
                appendLine("Today's stack:")
                stack.forEachIndexed { index, tile ->
                    val domain = ForgeDomain.fromName(tile.domain)
                    appendLine("${index + 1}. ${tile.title} (${domain.displayName})")
                }
            }
            appendLine()
            appendLine("Mission types: Brain · Body · Helper · Maker · Kindness")
            appendLine("Note: Morning & night routines stay on Student → Quests (not pushed daily).")
        }.trimEnd()
    }
}
