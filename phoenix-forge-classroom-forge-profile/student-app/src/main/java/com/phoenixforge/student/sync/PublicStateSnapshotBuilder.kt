package com.phoenixforge.student.sync

import com.phoenixforge.student.domain.model.HouseRoomType
import com.phoenixforge.student.domain.model.InventoryItem
import com.phoenixforge.student.domain.model.NpcState
import com.phoenixforge.student.domain.model.NpcType
import com.phoenixforge.student.domain.model.StudentProgress
import com.phoenixforge.student.domain.repository.StudentRepository
import com.phoenixforge.student.domain.session.StudentSessionStore
import com.phoenixforge.student.domain.world.WorldEngine
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class PublicStateSnapshotBuilder @Inject constructor(
    private val repository: StudentRepository,
    private val sessionStore: StudentSessionStore,
) {
    suspend fun build(eventCount: Int = 0): PublicStateSnapshot? {
        val studentUid = sessionStore.getActiveImportUid()?.trim().orEmpty()
        if (studentUid.isEmpty()) return null

        val progress = repository.observeProgress().first()
        val house = repository.observeHouse().first()
        val npcs = repository.observeNpcs().first()
        val profiles = repository.observeImportedProfiles().first()
        val forgeName = profiles.firstOrNull()?.forgeName.orEmpty()

        val unlockFlags = buildUnlockFlags(progress, house, npcs)
        return PublicStateSnapshot(
            studentUid = studentUid,
            forgeName = forgeName,
            level = progress.level,
            xp = progress.xp,
            currency = progress.currency,
            unlockedRooms = house.unlockedRoomTypes.map { it.name }.sorted(),
            unlockFlags = unlockFlags,
            companions = npcs.filter { it.type == NpcType.COMPANION }.map { it.toSnapshot() },
            whisps = npcs.filter { it.type == NpcType.WHISP }.map { it.toSnapshot() },
            pets = npcs.filter { it.type == NpcType.PET }.map { it.toSnapshot() },
            inventory = house.inventory.map {
                InventoryItemSnapshot(it.itemId, it.zone, it.meshHint)
            },
            lastUpdatedEpochMs = System.currentTimeMillis(),
            aggregatedFromEventCount = eventCount,
        )
    }

    private fun buildUnlockFlags(
        progress: StudentProgress,
        house: com.phoenixforge.student.domain.model.HouseState,
        npcs: List<NpcState>,
    ): Map<String, Boolean> {
        val flags = linkedMapOf<String, Boolean>()
        house.unlockedRoomTypes.forEach { room ->
            flags["room.${room.name}"] = true
        }
        npcs.forEach { npc ->
            val wireId = npcWireId(npc)
            flags[wireId] = npc.isUnlocked
        }
        progress.unlockFlags.forEach { flag ->
            flags[flag] = true
        }
        return flags
    }

    private fun NpcState.toSnapshot(): CompanionSnapshot = CompanionSnapshot(
        id = npcWireId(this),
        name = name,
        stage = evolutionStage,
        mood = mood,
        isUnlocked = isUnlocked,
    )

    companion object {
        fun npcWireId(npc: NpcState): String = when (npc.id) {
            "companion_spark" -> DigitalHomeWire.NpcIds.COMPANION_SPARK
            "whisp_lumen" -> DigitalHomeWire.NpcIds.WHISP_LUMEN
            else -> npc.id.replace('_', '.')
        }

        fun roomsUnlockedAtLevel(level: Int): List<String> =
            HouseRoomType.entries.filter { it.unlockLevel == level }.map { it.name }
    }
}
