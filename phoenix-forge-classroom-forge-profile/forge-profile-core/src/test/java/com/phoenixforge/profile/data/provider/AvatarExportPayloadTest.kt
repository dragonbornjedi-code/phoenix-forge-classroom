package com.phoenixforge.profile.data.provider

import com.phoenixforge.profile.domain.avatar.AvatarHeroCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AvatarExportPayloadTest {

    @Test
    fun fullPayloadIncludesGodotPathAndConfigJson() {
        val avatar = AvatarHeroCatalog.defaultAvatar()
        val dto = AvatarExportPayload.build(
            profileUid = "ezra-uid",
            forgeName = "Ezra",
            avatar = avatar,
        )
        assertEquals(0, dto.shardLevel)
        assertEquals("explorer", dto.heroStyle)
        assertEquals("blue", dto.heroColor)
        assertTrue(dto.godotModelPath.endsWith("Knight.glb"))
        assertTrue(dto.avatarConfigJson.contains("godotMeshHints"))
        assertEquals(AvatarHeroCatalog.summaryLine(avatar), dto.avatarSummary)
    }
}
