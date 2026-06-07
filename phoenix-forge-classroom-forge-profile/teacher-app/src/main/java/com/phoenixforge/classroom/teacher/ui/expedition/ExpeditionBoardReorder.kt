package com.phoenixforge.classroom.teacher.ui.expedition

import com.phoenixforge.classroom.teacher.domain.model.IntentTile

/** Returns a new list with [fromIndex] moved to [toIndex] and sortOrder renumbered. */
fun applyTileReorder(
    tiles: List<IntentTile>,
    fromIndex: Int,
    toIndex: Int,
): List<IntentTile> {
    if (tiles.isEmpty()) return tiles
    if (fromIndex !in tiles.indices || toIndex !in tiles.indices) return tiles
    if (fromIndex == toIndex) return tiles
    val mutable = tiles.toMutableList()
    val moved = mutable.removeAt(fromIndex)
    mutable.add(toIndex, moved)
    return mutable.mapIndexed { index, tile -> tile.copy(sortOrder = index) }
}
