package com.phoenixforge.student.domain.engine

import com.phoenixforge.student.domain.model.EmotionalImpact
import com.phoenixforge.student.domain.model.NpcReaction
import com.phoenixforge.student.domain.model.NpcState
import com.phoenixforge.student.domain.model.NpcType
import com.phoenixforge.student.domain.model.WorldEvent
import com.phoenixforge.student.domain.model.WorldEventType
import com.phoenixforge.student.domain.model.WorldState
import com.phoenixforge.student.domain.model.payload
import com.phoenixforge.student.domain.repository.StudentRepository
import com.phoenixforge.student.domain.simulation.NpcMemoryCompressor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NPCEngine @Inject constructor(
    private val repository: StudentRepository,
    private val memoryCompressor: NpcMemoryCompressor
) {
    fun observeNpcs(): Flow<List<NpcState>> = repository.observeNpcs()

    fun observeCompanion(): Flow<NpcState?> =
        observeNpcs().map { npcs -> npcs.firstOrNull { it.type == NpcType.COMPANION && it.isUnlocked } }

    suspend fun processEvent(
        event: WorldEvent,
        world: WorldState,
        impact: EmotionalImpact,
        weekAnchor: Long
    ): List<NpcReaction> {
        val reactions = mutableListOf<NpcReaction>()
        val npcs = repository.observeNpcs().first()
        val anchorMillis = weekAnchor.takeIf { it > 0L } ?: System.currentTimeMillis()

        npcs.filter { it.isUnlocked }.forEach { npc ->
            val reaction = react(npc, event, world, impact)
            if (reaction != null) {
                reactions += reaction
                val compressed = memoryCompressor.compressAfterEvent(npc, event, impact, anchorMillis)
                repository.saveNpc(
                    compressed.copy(
                        mood = reaction.moodAfter,
                        lastReaction = reaction.dialogue,
                        evolutionStage = evolvedStage(compressed, event)
                    )
                )
            }
        }

        if (event.type == WorldEventType.PHOTO_UPLOADED) {
            maybeSpawnWhisp(event.payload("tag") ?: "moment")
        }

        if (event.type == WorldEventType.IMPORT_PROFILE) {
            unlockWhispsIfNeeded()
        }

        return reactions
    }

    suspend fun unlockPet(petName: String) {
        repository.saveNpc(
            NpcState(
                id = "pet_${petName.lowercase()}",
                type = NpcType.PET,
                name = petName,
                evolutionStage = 1,
                isUnlocked = true,
                lastReaction = "$petName joined your Pet Space!",
                mood = "playful",
                personalityTraits = listOf("loyal", "curious")
            )
        )
    }

    private fun react(npc: NpcState, event: WorldEvent, world: WorldState, impact: EmotionalImpact): NpcReaction? {
        val dialogue = when (event.type) {
            WorldEventType.PHOTO_UPLOADED -> photoDialogue(npc, event)
            WorldEventType.QUEST_COMPLETE -> "Quest done! I knew you had it — ${event.payload("questTitle") ?: "nice work"}!"
            WorldEventType.STREAK_MILESTONE -> "You've come back ${world.streakDays} days running. I won't forget this."
            WorldEventType.IMPORT_PROFILE -> originDialogue(npc, event)
            WorldEventType.DAILY_RETURN -> greetingFor(npc, world, impact)
            WorldEventType.ABSENCE_RETURNED -> absenceDialogue(npc, event, impact)
            WorldEventType.ACHIEVEMENT_LOGGED -> "That achievement? It suits you — ${event.payload("title")}."
            WorldEventType.LEVEL_UP -> "Level ${world.level}! I can feel the house stretching taller."
        }

        val mood = moodFor(event, npc, impact)
        return NpcReaction(npc.id, npc.name, dialogue, mood)
    }

    private fun absenceDialogue(npc: NpcState, event: WorldEvent, impact: EmotionalImpact): String {
        val days = event.payload("daysAway") ?: "a while"
        val anchor = npc.memoryGraph.firstOrNull { it.isAnchor }?.interpretedAs
        return when {
            "empathetic" in npc.personalityTraits ->
                "I felt the quiet for $days days. I'm glad you're back${anchor?.let { " — $it" } ?: ""}."
            impact.intensity > 0.7f ->
                "The house held its breath for $days days. You came back — that matters."
            else -> "Welcome back. It was quieter without you ($days days)."
        }
    }

    private fun photoDialogue(npc: NpcState, event: WorldEvent): String {
        val tag = event.payload("tag") ?: "memory"
        return when {
            "empathetic" in npc.personalityTraits -> "That $tag photo feels important. I'll remember it with you."
            "playful" in npc.personalityTraits -> "Ooh, a $tag shot! The vault just got more interesting."
            else -> "A new $tag memory — safely kept."
        }
    }

    private fun originDialogue(npc: NpcState, event: WorldEvent): String {
        val name = event.payload("forgeName") ?: "you"
        return if (npc.type == NpcType.COMPANION) {
            "So you're $name… the house was waiting for your story to begin."
        } else {
            "A past echo arrives with $name's name on the wind."
        }
    }

    private fun greetingFor(npc: NpcState, world: WorldState, impact: EmotionalImpact): String {
        val anchors = npc.memoryGraph.count { it.isAnchor }
        return when {
            impact.tone == com.phoenixforge.student.domain.model.NarrativeTone.REFLECTIVE ->
                "Welcome back. The house kept your thread warm."
            anchors > 0 -> "Welcome back. I still carry ${anchors} moments from this week."
            world.streakDays > 3 -> "Streak day ${world.streakDays}! The house missed you."
            else -> "Good to see you again."
        }
    }

    private fun moodFor(event: WorldEvent, npc: NpcState, impact: EmotionalImpact): String = when (event.type) {
        WorldEventType.PHOTO_UPLOADED -> "warm"
        WorldEventType.QUEST_COMPLETE -> "celebrating"
        WorldEventType.STREAK_MILESTONE -> "proud"
        WorldEventType.IMPORT_PROFILE -> "wonder"
        WorldEventType.ABSENCE_RETURNED -> if (impact.intensity > 0.7f) "relieved" else "welcoming"
        WorldEventType.DAILY_RETURN -> "welcoming"
        WorldEventType.ACHIEVEMENT_LOGGED -> "proud"
        WorldEventType.LEVEL_UP -> "excited"
    }.let { base ->
        if ("calm" in npc.personalityTraits && event.importance > 2) "deeply_moved" else base
    }

    private fun evolvedStage(npc: NpcState, event: WorldEvent): Int {
        val anchorCount = npc.memoryGraph.count { it.isAnchor }
        val bonus = when (event.type) {
            WorldEventType.QUEST_COMPLETE, WorldEventType.IMPORT_PROFILE -> 1
            WorldEventType.ABSENCE_RETURNED -> 1
            else -> 0
        }
        return minOf(5, npc.evolutionStage + bonus + anchorCount)
    }

    private suspend fun maybeSpawnWhisp(trigger: String) {
        val whisps = repository.observeNpcs().first().filter { it.type == NpcType.WHISP }
        if (whisps.any { it.isUnlocked }) return
        repository.saveNpc(
            NpcState(
                id = "whisp_${UUID.randomUUID()}",
                type = NpcType.WHISP,
                name = "Lumen",
                evolutionStage = 1,
                isUnlocked = true,
                lastReaction = "A whisp appeared after $trigger",
                mood = "mysterious",
                personalityTraits = listOf("elusive", "curious")
            )
        )
    }

    private suspend fun unlockWhispsIfNeeded() {
        repository.observeNpcs().first()
            .filter { it.type == NpcType.WHISP && !it.isUnlocked }
            .forEach { whisp ->
                repository.saveNpc(whisp.copy(isUnlocked = true, lastReaction = "The import stirred me awake."))
            }
    }
}
