package com.phoenixforge.student.domain.engine

import com.phoenixforge.student.domain.house.DigitalHouse
import com.phoenixforge.student.domain.model.HouseRoomType
import com.phoenixforge.student.domain.model.StudentProgress
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RewardEngine @Inject constructor(
    private val progressionEngine: ProgressionEngine,
    private val digitalHouse: DigitalHouse,
    private val npcEngine: NPCEngine
) {
    suspend fun grantUnlock(unlockId: String, reason: String) {
        if (unlockId.isBlank()) return
        progressionEngine.addUnlockFlag(unlockId)
        when {
            unlockId.startsWith("decoration:") ->
                digitalHouse.addDecoration(unlockId.removePrefix("decoration:"))
            unlockId.startsWith("pet:") ->
                npcEngine.unlockPet(unlockId.removePrefix("pet:"))
            unlockId.startsWith("theme:") ->
                progressionEngine.addUnlockFlag(unlockId)
        }
    }

    fun unlockedRoomIds(progress: StudentProgress): List<String> =
        HouseRoomType.entries
            .filter { progress.level >= it.unlockLevel }
            .map { "room:${it.name}" }
}
