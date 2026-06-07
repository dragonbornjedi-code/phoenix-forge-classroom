package com.phoenixforge.classroom.teacher.ui.expedition

import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.TileStatus

enum class ExpeditionBoardFilter(val label: String) {
    ALL("All"),
    ACTIVE("Active"),
    DONE("Done"),
}

fun filterExpeditionTiles(
    tiles: List<IntentTile>,
    filter: ExpeditionBoardFilter,
): List<IntentTile> =
    when (filter) {
        ExpeditionBoardFilter.ALL -> tiles
        ExpeditionBoardFilter.ACTIVE ->
            tiles.filter { TileStatus.fromName(it.status) != TileStatus.COMPLETED }
        ExpeditionBoardFilter.DONE ->
            tiles.filter { TileStatus.fromName(it.status) == TileStatus.COMPLETED }
    }
