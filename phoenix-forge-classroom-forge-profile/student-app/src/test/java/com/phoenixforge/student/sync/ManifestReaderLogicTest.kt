package com.phoenixforge.student.sync

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ManifestReaderLogicTest {

    @Test
    fun `todayItems maps manifest days for matching date`() {
        val manifest = LessonManifest(
            manifestId = "manifest_test_2026-06-09",
            studentUid = "uid-1",
            createdByDeviceId = "teacher-1",
            createdEpochMs = 0L,
            validFromDate = "2026-06-09",
            validToDate = "2026-06-09",
            days = listOf(
                LessonManifestDay(
                    dayIndex = 0,
                    date = "2026-06-09",
                    narrativeTitle = "Morning literacy",
                    narrativeText = "Find three words.",
                    quests = listOf("stub_tile_abc"),
                ),
                LessonManifestDay(
                    dayIndex = 1,
                    date = "2026-06-09",
                    narrativeTitle = "Kindness helper",
                    quests = listOf("stub_tile_def"),
                ),
            ),
        )

        val items = manifest.todayItems(LocalDate.of(2026, 6, 9))

        assertEquals(2, items.size)
        assertEquals("Morning literacy", items[0].title)
        assertEquals("stub_tile_abc", items[0].questId)
        assertEquals("Kindness helper", items[1].title)
    }

    @Test
    fun `todayItems empty when date outside valid range`() {
        val manifest = LessonManifest(
            manifestId = "manifest_test",
            studentUid = "uid-1",
            createdByDeviceId = "teacher-1",
            createdEpochMs = 0L,
            validFromDate = "2026-06-08",
            validToDate = "2026-06-08",
            days = listOf(
                LessonManifestDay(
                    dayIndex = 0,
                    date = "2026-06-08",
                    narrativeTitle = "Yesterday",
                    quests = listOf("stub"),
                ),
            ),
        )

        val items = manifest.todayItems(LocalDate.of(2026, 6, 9))

        assertTrue(items.isEmpty())
    }
}
