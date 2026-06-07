package com.phoenixforge.classroom.teacher.ui.expedition

import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.TileStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExpeditionBoardFilterTest {
    private val planned =
        IntentTile(
            id = "a",
            title = "Planned",
            domain = ForgeDomain.LANGUAGE.name,
            status = TileStatus.PLANNED.name,
        )
    private val done =
        IntentTile(
            id = "b",
            title = "Done",
            domain = ForgeDomain.MOTOR.name,
            status = TileStatus.COMPLETED.name,
        )

    @Test
    fun activeFilterExcludesCompleted() {
        val filtered = filterExpeditionTiles(listOf(planned, done), ExpeditionBoardFilter.ACTIVE)
        assertEquals(listOf(planned), filtered)
    }

    @Test
    fun doneFilterIncludesOnlyCompleted() {
        val filtered = filterExpeditionTiles(listOf(planned, done), ExpeditionBoardFilter.DONE)
        assertEquals(listOf(done), filtered)
    }

    @Test
    fun emptyBoardHasNoVisibleTiles() {
        assertTrue(filterExpeditionTiles(emptyList(), ExpeditionBoardFilter.ALL).isEmpty())
    }
}
