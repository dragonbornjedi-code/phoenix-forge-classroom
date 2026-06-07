package com.phoenixforge.profile.domain.avatar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AvatarHeroCatalogTest {

    @Test
    fun mapsExplorerToKnightGlb() {
        assertTrue(
            AvatarHeroCatalog.godotModelPath("explorer")
                .endsWith("Knight.glb"),
        )
    }

    @Test
    fun summaryRoundTrip() {
        val avatar = AvatarHeroCatalog.defaultAvatar()
        val summary = AvatarHeroCatalog.summaryLine(avatar)
        val parsed = AvatarHeroCatalog.parseSummary(summary)
        requireNotNull(parsed)
        assertEquals("explorer", parsed.style)
        assertEquals("blue", parsed.color)
    }

    @Test
    fun randomizeIncrementsVersion() {
        val base = AvatarHeroCatalog.defaultAvatar()
        val next = AvatarHeroCatalog.randomAvatar(base)
        assertEquals(base.version + 1, next.version)
    }
}
