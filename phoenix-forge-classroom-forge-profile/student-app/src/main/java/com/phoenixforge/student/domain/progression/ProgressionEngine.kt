package com.phoenixforge.student.domain.progression

import com.phoenixforge.student.domain.model.HouseRoomType
import com.phoenixforge.student.domain.model.HouseState
import com.phoenixforge.student.domain.model.InventoryItem
import com.phoenixforge.student.domain.model.NpcState
import com.phoenixforge.student.domain.model.NpcType
import com.phoenixforge.student.domain.model.RoomNode
import com.phoenixforge.student.domain.model.StudentProgress
import com.phoenixforge.student.domain.repository.StudentRepository
import com.phoenixforge.student.domain.world.WorldEngine
import com.phoenixforge.student.sync.DigitalHomeWire
import com.phoenixforge.student.sync.EventWriter
import com.phoenixforge.student.sync.PublicStateSnapshotBuilder
import com.phoenixforge.student.sync.PublicStateWriter
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

data class ProgressionOutcome(
    val message: String,
    val leveledUp: Boolean = false,
    val roomsUnlocked: List<String> = emptyList(),
)

@Singleton
class ProgressionEngine @Inject constructor(
    private val repository: StudentRepository,
    private val worldEngine: WorldEngine,
    private val eventWriter: EventWriter,
    private val publicStateWriter: PublicStateWriter,
) {
    suspend fun applyQuestCompletedRewards(questId: String): Result<ProgressionOutcome> = runCatching {
        val xpReward = 25
        val tokenReward = 5
        val progress = repository.observeProgress().first()
        val house = repository.observeHouse().first()
        val npcs = repository.observeNpcs().first()

        eventWriter.writeXpEarned(xpReward, "quest:$questId").getOrThrow()
        eventWriter.writeCurrencyEarned(
            DigitalHomeWire.CURRENCY_FORGE_TOKENS,
            tokenReward,
            "quest:$questId",
        ).getOrThrow()

        val newXp = progress.xp + xpReward
        val newLevel = worldEngine.levelForXp(newXp)
        val leveledUp = newLevel > progress.level
        val newCurrency = progress.currency.toMutableMap().apply {
            val key = DigitalHomeWire.CURRENCY_FORGE_TOKENS
            put(key, (get(key) ?: 0) + tokenReward)
        }

        var unlockedRooms = house.unlockedRoomTypes.toMutableSet()
        val roomsFromLevel = if (leveledUp) {
            eventWriter.writeLevelUp(newLevel, progress.level).getOrThrow()
            PublicStateSnapshotBuilder.roomsUnlockedAtLevel(newLevel).also { roomNames ->
                roomNames.forEach { roomId ->
                    eventWriter.writeRoomUnlocked(roomId).getOrThrow()
                    HouseRoomType.entries.firstOrNull { it.name == roomId }?.let { unlockedRooms += it }
                }
            }
        } else {
            emptyList()
        }

        val updatedFlags = progress.unlockFlags.toMutableSet()
        val updatedNpcs = npcs.toMutableList()
        if (!updatedFlags.contains(DigitalHomeWire.Flags.WHISP_LUMEN)) {
            eventWriter.writeUnlockFlag(DigitalHomeWire.Flags.WHISP_LUMEN).getOrThrow()
            updatedFlags += DigitalHomeWire.Flags.WHISP_LUMEN
            val lumenIndex = updatedNpcs.indexOfFirst { it.id == "whisp_lumen" }
            if (lumenIndex >= 0) {
                updatedNpcs[lumenIndex] = updatedNpcs[lumenIndex].copy(
                    isUnlocked = true,
                    lastReaction = "A soft glow flickers — Lumen woke up!",
                )
            }
        }

        HouseRoomType.entries.filter { newLevel >= it.unlockLevel }.forEach { unlockedRooms += it }

        repository.saveProgress(
            progress.copy(
                xp = newXp,
                level = newLevel,
                currency = newCurrency,
                unlockFlags = updatedFlags,
            ),
        )
        repository.saveHouse(
            HouseState(
                rooms = HouseRoomType.entries.map { type ->
                    RoomNode(type = type, isUnlocked = type in unlockedRooms)
                },
                unlockedRoomTypes = unlockedRooms,
                decorations = house.decorations,
                inventory = house.inventory,
            ),
        )
        updatedNpcs.filter { it.id == "whisp_lumen" }.forEach { npc ->
            if (npc.isUnlocked) repository.saveNpc(npc)
        }

        publicStateWriter.writeFromLocal().getOrThrow()

        ProgressionOutcome(
            message = buildString {
                append("+$xpReward XP, +$tokenReward tokens")
                if (leveledUp) append(" · Level $newLevel!")
                if (roomsFromLevel.isNotEmpty()) append(" · Unlocked ${roomsFromLevel.joinToString()}")
            },
            leveledUp = leveledUp,
            roomsUnlocked = roomsFromLevel,
        )
    }

    suspend fun purchaseGardenItem(itemId: String): Result<ProgressionOutcome> = runCatching {
        val item = GardenMerchantCatalog.find(itemId)
            ?: error("Unknown garden item")
        val house = repository.observeHouse().first()
        if (!house.unlockedRoomTypes.contains(HouseRoomType.GARDEN)) {
            error("Garden is still locked — finish more quests first.")
        }
        if (house.inventory.any { it.itemId == item.itemId }) {
            error("You already have ${item.displayName}.")
        }

        val progress = repository.observeProgress().first()
        val tokenKey = DigitalHomeWire.CURRENCY_FORGE_TOKENS
        val balance = progress.currency[tokenKey] ?: 0
        if (balance < item.cost) {
            error("Need ${item.cost} tokens (you have $balance).")
        }

        eventWriter.writeCurrencySpent(
            currencyId = tokenKey,
            amount = item.cost,
            itemId = item.itemId,
            merchantId = GardenMerchantCatalog.MERCHANT_ID,
        ).getOrThrow()
        eventWriter.writeInventoryItemAdded(
            itemId = item.itemId,
            meshHint = item.meshHint,
        ).getOrThrow()

        val newCurrency = progress.currency.toMutableMap().apply {
            put(tokenKey, balance - item.cost)
        }
        val newInventory = house.inventory + InventoryItem(
            itemId = item.itemId,
            zone = DigitalHomeWire.Rooms.GARDEN,
            meshHint = item.meshHint,
        )

        repository.saveProgress(progress.copy(currency = newCurrency))
        repository.saveHouse(
            house.copy(
                inventory = newInventory,
                rooms = house.rooms,
            ),
        )
        publicStateWriter.writeFromLocal().getOrThrow()

        ProgressionOutcome(message = "Bought ${item.displayName}! It will appear in Forge World too.")
    }
}
