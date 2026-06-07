package com.phoenixforge.classroom.teacher.ui.expedition

import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import org.junit.Assert.assertEquals
import org.junit.Test

class ExpeditionBoardReorderTest {
    private fun tile(id: String, order: Int, title: String = id) =
        IntentTile(
            id = id,
            title = title,
            domain = ForgeDomain.LANGUAGE.name,
            sortOrder = order,
        )

    @Test
    fun moveFirstToLastRenumbersSortOrder() {
        val tiles = listOf(tile("a", 0), tile("b", 1), tile("c", 2))
        val reordered = applyTileReorder(tiles, fromIndex = 0, toIndex = 2)
        assertEquals(listOf("b", "c", "a"), reordered.map { it.id })
        assertEquals(listOf(0, 1, 2), reordered.map { it.sortOrder })
    }

    @Test
    fun invalidIndicesLeaveListUnchanged() {
        val tiles = listOf(tile("a", 0), tile("b", 1))
        assertEquals(tiles, applyTileReorder(tiles, fromIndex = -1, toIndex = 0))
        assertEquals(tiles, applyTileReorder(tiles, fromIndex = 0, toIndex = 9))
    }
}
