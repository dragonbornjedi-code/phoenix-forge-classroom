package com.phoenixforge.profile.domain.avatar

/**
 * Age-milestone shard tiers (0–6). Higher tiers gate wacky traits in Forge World (future).
 * Master step 0.62.
 */
object AvatarShardCatalog {

    data class ShardTier(
        val level: Int,
        val title: String,
        val hint: String,
    )

    val tiers: List<ShardTier> = listOf(
        ShardTier(0, "Spark seed", "Base hero"),
        ShardTier(1, "Ember", "Simple flair"),
        ShardTier(2, "Hearth", "Hat hints"),
        ShardTier(3, "Trail", "Cape slot"),
        ShardTier(4, "Forge", "Accessory pair"),
        ShardTier(5, "Realm", "Wacky preview"),
        ShardTier(6, "Phoenix", "Full expression"),
    )

    fun clamp(level: Int): Int = level.coerceIn(0, 6)

    /** Suggested shard from profile age — steward may override in Avatar Studio. */
    fun suggestFromAge(ageYears: Int?): Int = when {
        ageYears == null -> 2
        ageYears < 3 -> 0
        ageYears < 5 -> 1
        ageYears < 7 -> 2
        ageYears < 9 -> 3
        ageYears < 11 -> 4
        ageYears < 13 -> 5
        else -> 6
    }

    fun tier(level: Int): ShardTier = tiers[clamp(level)]
}
