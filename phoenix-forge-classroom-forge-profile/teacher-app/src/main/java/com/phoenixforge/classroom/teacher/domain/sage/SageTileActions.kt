package com.phoenixforge.classroom.teacher.domain.sage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SageApplyPayload(
    val actions: List<SageTileAction> = emptyList(),
)

@Serializable
data class SageTileAction(
    /** create | update */
    val op: String = "create",
    val title: String? = null,
    val domain: String? = null,
    val description: String? = null,
    @SerialName("student_mission") val studentMission: String? = null,
    val materials: String? = null,
    @SerialName("coaching_cues") val coachingCues: String? = null,
    @SerialName("field_guide_examples") val fieldGuideExamples: String? = null,
    @SerialName("field_guide_supports") val fieldGuideSupports: String? = null,
    @SerialName("field_guide_recovery") val fieldGuideRecovery: String? = null,
    @SerialName("routine_kind") val routineKind: String? = null,
    @SerialName("lesson_pattern_id") val lessonPatternId: String? = null,
    @SerialName("match_title") val matchTitle: String? = null,
    @SerialName("match_id") val matchId: String? = null,
)

data class ParsedSageResponse(
    val displayText: String,
    val actions: List<SageTileAction>,
)

object SageResponseParser {
    private val json = Json { ignoreUnknownKeys = true }
    private val blockRegex = Regex("```sage_apply\\s*([\\s\\S]*?)```", RegexOption.IGNORE_CASE)

    fun parse(raw: String): ParsedSageResponse {
        val match = blockRegex.find(raw)
        val block = match?.groupValues?.getOrNull(1)?.trim().orEmpty()
        val display = raw.replace(blockRegex, "").trim()
        val actions = parseActions(block)
        return ParsedSageResponse(displayText = display.ifBlank { raw.trim() }, actions = actions)
    }

    private fun parseActions(block: String): List<SageTileAction> {
        if (block.isBlank()) return emptyList()
        return runCatching {
            json.decodeFromString(SageApplyPayload.serializer(), block).actions
        }.getOrDefault(emptyList())
    }
}

data class SageApplyResult(
    val created: Int,
    val updated: Int,
    val skipped: Int,
    val message: String,
    val createdTileIds: List<String> = emptyList(),
)
