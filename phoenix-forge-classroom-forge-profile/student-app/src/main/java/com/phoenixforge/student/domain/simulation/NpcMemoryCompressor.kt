package com.phoenixforge.student.domain.simulation

import com.phoenixforge.student.domain.model.EmotionalImpact
import com.phoenixforge.student.domain.model.NpcMemoryAnchor
import com.phoenixforge.student.domain.model.NpcMemoryNode
import com.phoenixforge.student.domain.model.NpcState
import com.phoenixforge.student.domain.model.WorldEvent
import kotlinx.serialization.Serializable
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
private data class NpcAnchorStore(val anchors: List<NpcMemoryAnchor> = emptyList())

@Singleton
class NpcMemoryCompressor @Inject constructor(
    private val emotionalImpactSpine: EmotionalImpactSpine
) {
    private val maxActiveMemories = 12
    private val maxAnchors = 3
    private val decayPerWeek = 0.12f

    fun compressAfterEvent(
        npc: NpcState,
        event: WorldEvent,
        impact: EmotionalImpact,
        weekAnchor: Long
    ): NpcState {
        val strength = emotionalImpactSpine.memoryStrength(impact, event)
        val newNode = NpcMemoryNode(
            eventType = event.type.name,
            summary = event.payload.entries.joinToString { "${it.key}=${it.value}" }.ifBlank { event.type.name },
            emotionalWeight = (impact.score * 10).toInt().coerceIn(1, 10),
            strength = strength,
            isAnchor = strength >= 0.75f,
            timestampEpochMillis = event.timestampEpochMillis,
            interpretedAs = interpret(event, npc, impact)
        )

        val merged = (npc.memoryGraph + newNode)
            .map { decayMemory(it, weekAnchor) }
            .filter { it.strength >= 0.2f }
            .let { resolveContradictions(it) }
            .sortedByDescending { it.strength }
            .take(maxActiveMemories)

        promoteAnchors(npc, merged, weekAnchor)

        return npc.copy(memoryGraph = merged)
    }

    fun anchorSummaries(npc: NpcState, anchors: List<NpcMemoryAnchor>): List<NpcMemoryAnchor> {
        val existing = anchors
        val promoted = npc.memoryGraph
            .filter { it.isAnchor || it.strength >= 0.7f }
            .take(maxAnchors)
            .map { node ->
                NpcMemoryAnchor(
                    summary = node.summary,
                    eventType = node.eventType,
                    strength = node.strength,
                    weekAnchorEpochMillis = node.timestampEpochMillis
                )
            }
        return (existing + promoted)
            .distinctBy { it.summary }
            .sortedByDescending { it.strength }
            .take(maxAnchors)
    }

    private fun decayMemory(node: NpcMemoryNode, weekAnchor: Long): NpcMemoryNode {
        val ageWeeks = ((weekAnchor - node.timestampEpochMillis).coerceAtLeast(0L) / (7L * 86_400_000L)).toInt()
        val decay = 1f - (ageWeeks * decayPerWeek)
        return node.copy(strength = (node.strength * decay).coerceAtLeast(0.1f))
    }

    private fun resolveContradictions(memories: List<NpcMemoryNode>): List<NpcMemoryNode> {
        val byType = memories.groupBy { it.eventType }
        return byType.flatMap { (_, group) ->
            if (group.size <= 1) return@flatMap group
            val strongest = group.maxBy { it.strength }
            group.map { node ->
                if (node == strongest) node
                else node.copy(
                    strength = node.strength * 0.6f,
                    interpretedAs = "remembered differently: ${node.interpretedAs ?: node.summary}"
                )
            }
        }
    }

    private fun promoteAnchors(
        npc: NpcState,
        memories: List<NpcMemoryNode>,
        weekAnchor: Long
    ): List<NpcMemoryAnchor> = memories
        .filter { it.isAnchor }
        .take(maxAnchors)
        .map {
            NpcMemoryAnchor(
                summary = it.summary,
                eventType = it.eventType,
                strength = it.strength,
                weekAnchorEpochMillis = weekAnchor
            )
        }

    private fun interpret(event: WorldEvent, npc: NpcState, impact: EmotionalImpact): String {
        val trait = npc.personalityTraits.firstOrNull() ?: "curious"
        return when {
            impact.valence == com.phoenixforge.student.domain.model.EmotionalValence.LONGING ->
                "$trait lens: felt the gap before you returned"
            impact.tone == com.phoenixforge.student.domain.model.NarrativeTone.BRIGHT ->
                "$trait lens: brightened by momentum"
            else -> "$trait lens: quietly noted"
        }
    }
}
