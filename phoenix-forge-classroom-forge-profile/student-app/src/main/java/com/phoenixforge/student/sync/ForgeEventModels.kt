package com.phoenixforge.student.sync

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

enum class EventScope(val wireName: String) {
    PUBLIC("PUBLIC"),
    LESSON("LESSON"),
    PROTECTED("PROTECTED");

    companion object {
        /** Student Edition may only emit PUBLIC (and later LESSON) — never PROTECTED. */
        fun studentMayWrite(scope: EventScope): Boolean =
            scope == PUBLIC || scope == LESSON
    }
}

object ForgeEventTypes {
    const val QUEST_STARTED = "QUEST_STARTED"
    const val QUEST_COMPLETED = "QUEST_COMPLETED"
    const val XP_EARNED = "XP_EARNED"
    const val LEVEL_UP = "LEVEL_UP"
    const val ROOM_UNLOCKED = "ROOM_UNLOCKED"
    const val UNLOCK_FLAG_SET = "UNLOCK_FLAG_SET"
    const val CURRENCY_EARNED = "CURRENCY_EARNED"
    const val CURRENCY_SPENT = "CURRENCY_SPENT"
    const val INVENTORY_ITEM_ADDED = "INVENTORY_ITEM_ADDED"
    const val COMPANION_EVOLVED = "COMPANION_EVOLVED"
}

@Serializable
data class ForgeEventRecord(
    val schemaVersion: Int = 1,
    val eventId: String,
    val eventType: String,
    val scope: String,
    val actorApp: String,
    val actorDeviceId: String,
    val studentUid: String,
    val logicalClock: Long,
    val epochMs: Long,
    val payload: JsonElement,
)

@Serializable
data class QuestStartedPayload(val questId: String)

@Serializable
data class QuestCompletedPayload(
    val questId: String,
    val score: Int? = null,
)

@Serializable
data class XpEarnedPayload(val xpAmount: Int, val reason: String)

@Serializable
data class LevelUpPayload(val newLevel: Int, val previousLevel: Int)

@Serializable
data class RoomUnlockedPayload(val roomId: String)

@Serializable
data class UnlockFlagSetPayload(val flagId: String, val value: Boolean = true)

@Serializable
data class CurrencyEarnedPayload(val currencyId: String, val amount: Int, val reason: String)

@Serializable
data class CurrencySpentPayload(
    val currencyId: String,
    val amount: Int,
    val itemId: String,
    val merchantId: String,
)

@Serializable
data class InventoryItemAddedPayload(
    val itemId: String,
    val category: String = "garden",
    val placementZone: String = DigitalHomeWire.Rooms.GARDEN,
    val meshHint: String = "",
)

@Serializable
data class CompanionEvolvedPayload(val companionId: String, val stage: Int)

data class EventWriteResult(
    val record: ForgeEventRecord,
    val writtenPaths: List<String>,
    val message: String,
) {
    val success: Boolean get() = writtenPaths.isNotEmpty()
}
