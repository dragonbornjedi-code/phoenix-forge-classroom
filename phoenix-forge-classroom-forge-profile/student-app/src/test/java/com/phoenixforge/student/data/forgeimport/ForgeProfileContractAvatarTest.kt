package com.phoenixforge.student.data.forgeimport

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Step 0.61 — Student mirror columns stay aligned with forge-profile-core [ProfileContract].
 */
class ForgeProfileContractAvatarTest {

    @Test
    fun avatarColumnsMatchProviderContract() {
        val expected = listOf(
            "hair_type",
            "eye_color",
            "skin_tone",
            "clothing_id",
            "avatar_version",
            "avatar_shard_level",
            "hero_style",
            "hero_color",
            "godot_model_path",
            "avatar_summary",
            "avatar_config_json",
        )
        val actual = listOf(
            ForgeProfileContract.Columns.AVATAR_HAIR,
            ForgeProfileContract.Columns.AVATAR_EYES,
            ForgeProfileContract.Columns.AVATAR_SKIN,
            ForgeProfileContract.Columns.AVATAR_CLOTHING,
            ForgeProfileContract.Columns.AVATAR_VERSION,
            ForgeProfileContract.Columns.AVATAR_SHARD_LEVEL,
            ForgeProfileContract.Columns.HERO_STYLE,
            ForgeProfileContract.Columns.HERO_COLOR,
            ForgeProfileContract.Columns.GODOT_MODEL_PATH,
            ForgeProfileContract.Columns.AVATAR_SUMMARY,
            ForgeProfileContract.Columns.AVATAR_CONFIG_JSON,
        )
        assertEquals(expected, actual)
    }
}
