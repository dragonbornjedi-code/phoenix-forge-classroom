package com.phoenixforge.classroom.teacher.domain.daystart

import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.TileStatus
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class StartDayExportTest {

    @Test
    fun `build lists active stack in sort order`() {
        val text = StartDayExport.build(
            tiles = listOf(
                tile("Second", sortOrder = 1),
                tile("First", sortOrder = 0),
                tile("Done", sortOrder = 2, status = TileStatus.COMPLETED),
            ),
            today = LocalDate.of(2026, 6, 5),
        )

        assertTrue(text.contains("1. First"))
        assertTrue(text.contains("2. Second"))
        assertFalse(text.contains("Done"))
    }

    @Test
    fun `build includes launch line and date`() {
        val text = StartDayExport.build(emptyList(), today = LocalDate.of(2026, 6, 5))

        assertTrue(text.contains("Version today is starting"))
        assertTrue(text.contains("Jun 5"))
        assertTrue(text.contains("(empty"))
    }

    private fun tile(
        title: String,
        sortOrder: Int,
        status: TileStatus = TileStatus.PLANNED,
    ) = IntentTile(
        title = title,
        domain = ForgeDomain.SOCIAL.name,
        sortOrder = sortOrder,
        status = status.name,
    )
}
