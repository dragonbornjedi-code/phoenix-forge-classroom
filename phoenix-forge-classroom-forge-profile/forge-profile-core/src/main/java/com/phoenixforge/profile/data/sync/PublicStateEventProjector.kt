package com.phoenixforge.profile.data.sync

import com.phoenixforge.profile.data.local.dao.EventRecordDao
import com.phoenixforge.profile.data.serialization.ForgeProfileJson
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

@Singleton
class PublicStateEventProjector @Inject constructor(
    private val eventRecordDao: EventRecordDao,
    private val forgeProfileJson: ForgeProfileJson,
) {
    suspend fun project(studentUid: String): PublicStateSnapshot {
        val events = eventRecordDao.listEventsForStudent(studentUid)
            .filter { it.scope == ForgeEventScopes.PUBLIC }
            .sortedWith(compareBy({ it.logicalClock }, { it.eventId }))

        var xp = 0
        var level = 1
        val currency = mutableMapOf(DigitalHomeWire.CURRENCY_FORGE_TOKENS to 0)
        val unlockedRooms = linkedSetOf("BEDROOM", "QUEST_BOARD")
        val unlockFlags = linkedMapOf(
            DigitalHomeWire.COMPANION_SPARK to true,
            DigitalHomeWire.WHISP_LUMEN to false,
        )
        val inventory = mutableListOf<InventoryItemSnapshot>()
        var whispLumenUnlocked = false

        events.forEach { row ->
            val payload = forgeProfileJson.json.parseToJsonElement(row.payloadJson)
            when (row.eventType) {
                ForgeEventTypes.XP_EARNED -> {
                    xp += payload.intField("xpAmount")
                    level = levelForXp(xp)
                }
                ForgeEventTypes.LEVEL_UP -> {
                    level = payload.intField("newLevel").coerceAtLeast(level)
                }
                ForgeEventTypes.ROOM_UNLOCKED -> {
                    payload.stringField("roomId")?.let { unlockedRooms += it }
                }
                ForgeEventTypes.UNLOCK_FLAG_SET -> {
                    val flag = payload.stringField("flagId") ?: return@forEach
                    val value = payload.boolField("value") ?: true
                    unlockFlags[flag] = value
                    if (flag == DigitalHomeWire.WHISP_LUMEN && value) whispLumenUnlocked = true
                }
                ForgeEventTypes.CURRENCY_EARNED -> {
                    val id = payload.stringField("currencyId") ?: DigitalHomeWire.CURRENCY_FORGE_TOKENS
                    currency[id] = (currency[id] ?: 0) + payload.intField("amount")
                }
                ForgeEventTypes.CURRENCY_SPENT -> {
                    val id = payload.stringField("currencyId") ?: DigitalHomeWire.CURRENCY_FORGE_TOKENS
                    currency[id] = ((currency[id] ?: 0) - payload.intField("amount")).coerceAtLeast(0)
                }
                ForgeEventTypes.INVENTORY_ITEM_ADDED -> {
                    val itemId = payload.stringField("itemId") ?: return@forEach
                    if (inventory.none { it.itemId == itemId }) {
                        inventory += InventoryItemSnapshot(
                            itemId = itemId,
                            zone = payload.stringField("placementZone") ?: "GARDEN",
                            meshHint = payload.stringField("meshHint").orEmpty(),
                        )
                    }
                }
            }
        }

        level = levelForXp(xp).coerceAtLeast(level)
        (1..level).forEach { lvl -> roomsUnlockedAtLevel(lvl).forEach { unlockedRooms += it } }

        return PublicStateSnapshot(
            studentUid = studentUid,
            level = level,
            xp = xp,
            currency = currency,
            unlockedRooms = unlockedRooms.sorted(),
            unlockFlags = unlockFlags,
            companions = listOf(
                CompanionSnapshot(
                    id = DigitalHomeWire.COMPANION_SPARK,
                    name = "Spark",
                    stage = 1,
                    mood = "playful",
                    isUnlocked = true,
                ),
            ),
            whisps = listOf(
                CompanionSnapshot(
                    id = DigitalHomeWire.WHISP_LUMEN,
                    name = if (whispLumenUnlocked) "Lumen" else "???",
                    stage = if (whispLumenUnlocked) 1 else 0,
                    mood = "curious",
                    isUnlocked = whispLumenUnlocked,
                ),
            ),
            pets = emptyList(),
            inventory = inventory.toList(),
            lastUpdatedEpochMs = System.currentTimeMillis(),
            aggregatedFromEventCount = events.size,
        )
    }

    private fun levelForXp(xp: Int): Int {
        var level = 1
        var threshold = 0
        while (xp >= threshold + xpForLevel(level)) {
            threshold += xpForLevel(level)
            level++
        }
        return level
    }

    private fun xpForLevel(level: Int): Int = (50 * level.toDouble().pow(1.2)).toInt()

    private fun roomsUnlockedAtLevel(level: Int): List<String> = when (level) {
        1 -> listOf("BEDROOM", "QUEST_BOARD")
        2 -> listOf("STUDY", "MEMORY_VAULT", "GALLERY")
        3 -> listOf("GARDEN")
        4 -> listOf("PET_SPACE")
        else -> emptyList()
    }

    private fun JsonElement.intField(key: String): Int =
        (this as? JsonObject)?.get(key)?.jsonPrimitive?.intOrNull ?: 0

    private fun JsonElement.stringField(key: String): String? =
        (this as? JsonObject)?.get(key)?.let { element ->
            (element as? JsonPrimitive)?.content
        }

    private fun JsonElement.boolField(key: String): Boolean? =
        (this as? JsonObject)?.get(key)?.jsonPrimitive?.let { primitive ->
            when (primitive.content) {
                "true" -> true
                "false" -> false
                else -> null
            }
        }
}

@Singleton
class PublicStateSnapshotWriter @Inject constructor(
    private val projector: PublicStateEventProjector,
    private val forgeProfileJson: ForgeProfileJson,
) {
    suspend fun projectAndWrite(studentUid: String): List<String> {
        val snapshot = projector.project(studentUid)
        val encoded = forgeProfileJson.json.encodeToString(PublicStateSnapshot.serializer(), snapshot)
        val relative = SyncPaths.publicStateRelativePath(studentUid)
        return PublicSyncWriter.writeRelative(relative, encoded)
    }
}
