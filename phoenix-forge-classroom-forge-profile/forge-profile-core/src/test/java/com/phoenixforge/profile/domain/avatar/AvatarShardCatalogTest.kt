package com.phoenixforge.profile.domain.avatar

import org.junit.Assert.assertEquals
import org.junit.Test

class AvatarShardCatalogTest {

    @Test
    fun clampKeepsRange() {
        assertEquals(0, AvatarShardCatalog.clamp(-1))
        assertEquals(6, AvatarShardCatalog.clamp(99))
    }

    @Test
    fun suggestFromAgeMapsMilestones() {
        assertEquals(0, AvatarShardCatalog.suggestFromAge(2))
        assertEquals(2, AvatarShardCatalog.suggestFromAge(6))
        assertEquals(6, AvatarShardCatalog.suggestFromAge(14))
    }

    @Test
    fun shardFlowsIntoAvatarConfig() {
        val avatar = AvatarHeroCatalog.buildAvatar(
            style = "artist",
            color = "purple",
            version = 1,
            shardLevel = 4,
        )
        val config = AvatarHeroCatalog.toConfig("uid", "Ezra", avatar)
        assertEquals(4, config.shardLevel)
    }
}
