package com.phoenixforge.classroom.teacher.domain.chariot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class ChariotMission(
    val tileId: String = "",
    val title: String,
    val studentMission: String,
    val xpReward: Int = 25,
    val missionCards: List<String> = emptyList(),
    val sparkLine: String = "",
    @SerialName("narration_clip_id") val narrationClipId: String = "",
    @SerialName("narration_category") val narrationCategory: String = "",
    @SerialName("input_type") val inputType: String = "done",
    @SerialName("voice_id") val voiceId: String = "",
    val realm: String = "",
    @SerialName("sensory_load") val sensoryLoad: Int = 1,
    @SerialName("ef_demand") val efDemand: Int = 1,
    @SerialName("correct_answer") val correctAnswer: String = "",
    val hints: List<String> = emptyList(),
    val choices: List<String> = emptyList(),
)

@Serializable
data class ChariotQuestStack(
    @SerialName("schema_version") val schemaVersion: Int = 1,
    @SerialName("export_mode") val exportMode: String = "expedition_tiles",
    @SerialName("session_id") val sessionId: String = "",
    val weather: String = "",
    @SerialName("body_weather_flags") val bodyWeatherFlags: List<String> = emptyList(),
    @SerialName("welcome_line") val welcomeLine: String = "Welcome to the Chariot of Champions! Car quest time!",
    @SerialName("welcome_clip_id") val welcomeClipId: String = "",
    @SerialName("celebration_clip_id") val celebrationClipId: String = "",
    val story: String = "",
    @SerialName("duration_minutes") val durationMinutes: Int = 0,
    @SerialName("total_xp") val totalXp: Int = 0,
    @SerialName("deep_objectives") val deepObjectives: Map<String, String> = emptyMap(),
    val scaffolding: Map<String, String> = emptyMap(),
    @SerialName("parent_tips") val parentTips: List<String> = emptyList(),
    val missions: List<ChariotMission> = emptyList(),
) {
    fun toJson(): String = chariotJson.encodeToString(this)

    companion object {
        private val chariotJson = Json { ignoreUnknownKeys = true; prettyPrint = true }

        fun fromJson(raw: String): ChariotQuestStack =
            chariotJson.decodeFromString<ChariotQuestStack>(raw)
    }
}
