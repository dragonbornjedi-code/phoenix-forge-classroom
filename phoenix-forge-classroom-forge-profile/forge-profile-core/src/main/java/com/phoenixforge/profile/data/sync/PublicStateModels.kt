package com.phoenixforge.profile.data.sync

import kotlinx.serialization.Serializable

object ForgeEventTypes {
    const val QUEST_COMPLETED = "QUEST_COMPLETED"
    const val XP_EARNED = "XP_EARNED"
    const val LEVEL_UP = "LEVEL_UP"
    const val ROOM_UNLOCKED = "ROOM_UNLOCKED"
    const val UNLOCK_FLAG_SET = "UNLOCK_FLAG_SET"
    const val CURRENCY_EARNED = "CURRENCY_EARNED"
    const val CURRENCY_SPENT = "CURRENCY_SPENT"
    const val INVENTORY_ITEM_ADDED = "INVENTORY_ITEM_ADDED"
}

@Serializable
data class PublicStateSnapshot(
    val schemaVersion: Int = 1,
    val studentUid: String,
    val forgeName: String = "",
    val level: Int = 1,
    val xp: Int = 0,
    val currency: Map<String, Int> = emptyMap(),
    val unlockedRooms: List<String> = emptyList(),
    val unlockFlags: Map<String, Boolean> = emptyMap(),
    val companions: List<CompanionSnapshot> = emptyList(),
    val whisps: List<CompanionSnapshot> = emptyList(),
    val pets: List<CompanionSnapshot> = emptyList(),
    val inventory: List<InventoryItemSnapshot> = emptyList(),
    val lastUpdatedEpochMs: Long = 0L,
    val aggregatedFromEventCount: Int = 0,
)

@Serializable
data class CompanionSnapshot(
    val id: String,
    val name: String,
    val stage: Int,
    val mood: String = "calm",
    val isUnlocked: Boolean,
)

@Serializable
data class InventoryItemSnapshot(
    val itemId: String,
    val zone: String,
    val meshHint: String = "",
)

object DigitalHomeWire {
    const val CURRENCY_FORGE_TOKENS = "forge_tokens"
    const val COMPANION_SPARK = "companion.spark"
    const val WHISP_LUMEN = "whisp.lumen"
}
