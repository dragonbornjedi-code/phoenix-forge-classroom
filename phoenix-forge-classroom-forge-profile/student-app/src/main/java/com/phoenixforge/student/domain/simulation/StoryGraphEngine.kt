package com.phoenixforge.student.domain.simulation

import com.phoenixforge.student.domain.model.EmotionalImpact
import com.phoenixforge.student.domain.model.NarrativeTone
import com.phoenixforge.student.domain.model.NpcReaction
import com.phoenixforge.student.domain.model.StoryFragment
import com.phoenixforge.student.domain.model.StoryGraphNode
import com.phoenixforge.student.domain.model.WorldEvent
import com.phoenixforge.student.domain.model.WorldEventType
import com.phoenixforge.student.domain.model.WorldState
import com.phoenixforge.student.domain.model.payload
import com.phoenixforge.student.domain.repository.StudentRepository
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoryGraphEngine @Inject constructor(
    private val repository: StudentRepository
) {
    suspend fun buildGraph(limit: Int = 40): List<StoryGraphNode> {
        val fragments = repository.observeStoryFragments(limit).first()
        val byId = fragments.associateBy { it.id }
        return fragments.map { fragment ->
            StoryGraphNode(
                fragment = fragment,
                causeFragment = fragment.causeFragmentId?.let { byId[it] },
                linkedNpcMood = fragment.npcSpeaker
            )
        }
    }

    suspend fun findCauseFragment(event: WorldEvent, world: WorldState): StoryFragment? {
        val recent = repository.observeStoryFragments(8).first()
        return when (event.type) {
            WorldEventType.PHOTO_UPLOADED -> recent.firstOrNull { it.worldEventType == WorldEventType.DAILY_RETURN }
            WorldEventType.QUEST_COMPLETE -> recent.firstOrNull {
                it.worldEventType == WorldEventType.PHOTO_UPLOADED || it.worldEventType == WorldEventType.DAILY_RETURN
            }
            WorldEventType.ABSENCE_RETURNED -> recent.firstOrNull { it.worldEventType != WorldEventType.DAILY_RETURN }
            else -> recent.firstOrNull()
        }
    }

    fun composeFragment(
        event: WorldEvent,
        world: WorldState,
        impact: EmotionalImpact,
        npcReactions: List<NpcReaction>,
        cause: StoryFragment?,
        speaker: String?
    ): StoryFragment {
        val callback = buildCallback(event, cause, impact, speaker, npcReactions)
        val narrative = buildLinkedNarrative(event, world, impact, callback, npcReactions, speaker)
        val thread = continuityThread(event, cause)

        return StoryFragment(
            id = UUID.randomUUID().toString(),
            narrative = narrative,
            worldEventType = event.type,
            xpAwarded = event.xpReward,
            roomUnlocked = null,
            npcSpeaker = speaker,
            environmentChange = null,
            timestampEpochMillis = event.timestampEpochMillis,
            causeFragmentId = cause?.id,
            callbackLine = callback,
            emotionalImpact = impact.score,
            continuityThread = thread
        )
    }

    private fun buildCallback(
        event: WorldEvent,
        cause: StoryFragment?,
        impact: EmotionalImpact,
        speaker: String?,
        reactions: List<NpcReaction>
    ): String? {
        if (cause == null) return null
        val because = when (event.type) {
            WorldEventType.PHOTO_UPLOADED -> "because you came back to the house first"
            WorldEventType.QUEST_COMPLETE -> "because you were already exploring when you captured that moment"
            WorldEventType.ABSENCE_RETURNED -> "because the house noticed the quiet while you were away"
            else -> "because of what happened before: ${cause.worldEventType.name.lowercase().replace('_', ' ')}"
        }
        val mood = reactions.firstOrNull()?.dialogue
        return buildString {
            append("This happened $because.")
            if (impact.tone == NarrativeTone.REFLECTIVE) append(" The air still held that pause.")
            mood?.let { append(" $speaker remembers: \"$it\"") }
        }
    }

    private fun buildLinkedNarrative(
        event: WorldEvent,
        world: WorldState,
        impact: EmotionalImpact,
        callback: String?,
        reactions: List<NpcReaction>,
        speaker: String?
    ): String = buildString {
        callback?.let {
            append(it)
            append(" ")
        }
        append(baseNarrative(event, world, impact))
        reactions.firstOrNull()?.dialogue?.let { append(" $speaker: \"$it\"") }
    }

    private fun baseNarrative(event: WorldEvent, world: WorldState, impact: EmotionalImpact): String =
        when (event.type) {
            WorldEventType.ABSENCE_RETURNED -> {
                val days = event.payload("daysAway") ?: "several"
                "After $days days away, the house exhales. Dust becomes starlight again. " +
                    if (impact.intensity > 0.7f) "Spark waited by the door — not angry, just awake."
                    else "A gentle stillness lifts."
            }
            WorldEventType.PHOTO_UPLOADED -> {
                val tag = event.payload("tag") ?: "moment"
                "The $tag memory joins the thread of your story (tone: ${impact.tone.name.lowercase()})."
            }
            WorldEventType.QUEST_COMPLETE ->
                "Quest \"${event.payload("questTitle")}\" closes a loop in chapter ${world.level}."
            WorldEventType.IMPORT_PROFILE ->
                "Origin: ${event.payload("forgeName")} is woven into the house's memory — not imported, remembered."
            WorldEventType.STREAK_MILESTONE ->
                "Day ${event.payload("streakDays")}: streak-fire binds moments into a single glowing thread."
            WorldEventType.DAILY_RETURN ->
                if (impact.tone == NarrativeTone.REFLECTIVE) {
                    "You return; the house had been holding your place in the story."
                } else {
                    "Another step inside — the narrative continues."
                }
            WorldEventType.ACHIEVEMENT_LOGGED ->
                "Achievement \"${event.payload("title")}\" becomes part of the permanent ledger."
            WorldEventType.LEVEL_UP ->
                "Level ${world.level} — the architecture of your house learns a new shape."
        }

    private fun continuityThread(event: WorldEvent, cause: StoryFragment?): String? {
        val tag = event.type.name.lowercase()
        return cause?.continuityThread?.let { "$it → $tag" } ?: "thread:$tag"
    }
}
