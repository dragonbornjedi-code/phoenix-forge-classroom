package com.phoenixforge.classroom.teacher.domain.manifest

import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.TileStatus
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ManifestWriterTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `build creates one day row per stack tile with stub quest ids`() {
        val manifest = ManifestWriter.build(
            tiles = listOf(
                tile("First quest", sortOrder = 0),
                tile("Second quest", sortOrder = 1),
                tile("Done", sortOrder = 2, status = TileStatus.COMPLETED),
            ),
            studentUid = "child-uid-123",
            deviceId = "teacher-device-1",
            today = LocalDate.of(2026, 6, 9),
        )

        assertEquals("manifest_child-uid-123_2026-06-09", manifest.manifestId)
        assertEquals("2026-06-09", manifest.validFromDate)
        assertEquals(2, manifest.days.size)
        assertEquals("First quest", manifest.days[0].narrativeTitle)
        assertEquals("Second quest", manifest.days[1].narrativeTitle)
        assertTrue(manifest.days[0].quests.single().startsWith("stub_tile_"))
    }

    @Test
    fun `build prefers starterLessonId for quest stub`() {
        val manifest = ManifestWriter.build(
            tiles = listOf(
                tile("Literacy", starterLessonId = "hv_literacy_01"),
            ),
            studentUid = "uid",
            deviceId = "device",
            today = LocalDate.of(2026, 6, 9),
        )

        assertEquals(listOf("hv_literacy_01"), manifest.days.single().quests)
    }

    @Test
    fun `encode produces schemaVersion and studentUid`() {
        val encoded = ManifestWriter.encode(
            ManifestWriter.build(emptyList(), "uid-abc", "dev-1", LocalDate.of(2026, 6, 9)),
        )

        assertTrue(encoded.contains("\"schemaVersion\": 1"))
        assertTrue(encoded.contains("\"studentUid\": \"uid-abc\""))
        assertFalse(encoded.contains("syncVersion"))
    }

    @Test
    fun `filterStackTiles excludes completed and deferred`() {
        val filtered = ManifestWriter.filterStackTiles(
            listOf(
                tile("Planned", status = TileStatus.PLANNED),
                tile("Active", status = TileStatus.ACTIVE),
                tile("Sent", status = TileStatus.SENT),
                tile("Done", status = TileStatus.COMPLETED),
                tile("Later", status = TileStatus.DEFERRED),
            ),
        )

        assertEquals(3, filtered.size)
        assertEquals(listOf("Planned", "Active", "Sent"), filtered.map { it.title })
    }

    @Test
    fun `manifest file name uses iso date`() {
        assertEquals(
            "lesson_manifest_2026-06-09.json",
            ManifestSyncPaths.manifestFileName("2026-06-09"),
        )
    }

    @Test
    fun `filterStackTiles excludes morning and night routines`() {
        val filtered = ManifestWriter.filterStackTiles(
            listOf(
                tile("Morning circle", routineKind = "morning_routine"),
                tile("Nature walk"),
                tile("Night wind-down", routineKind = "night_routine"),
            ),
        )
        assertEquals(1, filtered.size)
        assertEquals("Nature walk", filtered.single().title)
    }

    private fun tile(
        title: String,
        sortOrder: Int = 0,
        status: TileStatus = TileStatus.PLANNED,
        starterLessonId: String? = null,
        routineKind: String = "",
    ) = IntentTile(
        id = "tile-${title.replace(" ", "-").lowercase()}",
        title = title,
        domain = ForgeDomain.LANGUAGE.name,
        sortOrder = sortOrder,
        status = status.name,
        starterLessonId = starterLessonId,
        routineKind = routineKind,
    )
}
