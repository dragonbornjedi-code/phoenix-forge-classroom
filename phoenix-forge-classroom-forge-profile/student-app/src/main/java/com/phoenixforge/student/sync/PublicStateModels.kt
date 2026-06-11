package com.phoenixforge.student.sync

import kotlinx.serialization.Serializable

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
