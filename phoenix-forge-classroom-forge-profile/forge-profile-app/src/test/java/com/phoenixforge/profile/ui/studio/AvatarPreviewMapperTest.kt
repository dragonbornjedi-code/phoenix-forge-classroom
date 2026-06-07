package com.phoenixforge.profile.ui.studio

import com.phoenixforge.profile.domain.model.Avatar
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class AvatarPreviewMapperTest {

    @Test
    fun mapsKayKitHeroStyleTokens() {
        val look = AvatarPreviewMapper.fromAvatar(
            Avatar(
                id = "1",
                hairType = "explorer",
                eyeColor = "gold",
                skinTone = "medium",
                clothingId = "knight",
                version = 1,
                timestamp = Instant.now(),
            ),
        )

        assertEquals(HeroStyle.EXPLORER, look.heroStyle)
        assertEquals(ClothingStyle.ARMOR, look.clothingStyle)
    }

    @Test
    fun mapsLegacyHairTokensWhenNotHeroStyle() {
        val look = AvatarPreviewMapper.fromAvatar(
            Avatar(
                id = "1",
                hairType = "Curly",
                eyeColor = "Brown",
                skinTone = "light",
                clothingId = "Shirt",
                version = 1,
                timestamp = Instant.now(),
            ),
        )

        assertEquals(HairStyle.CURLY, look.hairStyle)
        assertEquals(ClothingStyle.SHIRT, look.clothingStyle)
    }

    @Test
    fun nullAvatarUsesExplorerDefaults() {
        val look = AvatarPreviewMapper.fromAvatar(null)
        assertEquals(HeroStyle.EXPLORER, look.heroStyle)
        assertEquals(ClothingStyle.ARMOR, look.clothingStyle)
    }
}
