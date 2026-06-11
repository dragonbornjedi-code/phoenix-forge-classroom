package com.phoenixforge.classroom.teacher.domain.chariot

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ChariotActivity(
    val realm: String = "",
    @SerialName("voice_id") val voiceId: String = "",
    @SerialName("activity_type") val activityType: String = "",
    @SerialName("narration_clip_id") val narrationClipId: String = "",
    @SerialName("narration_category") val narrationCategory: String = "",
    @SerialName("narration_text") val narrationText: String = "",
    @SerialName("input_type") val inputType: String = "done",
    val xp: Int = 0,
    @SerialName("sensory_load") val sensoryLoad: Int = 1,
    @SerialName("ef_demand") val efDemand: Int = 1,
    @SerialName("deep_target") val deepTarget: String = "",
    @SerialName("correct_answer") val correctAnswer: String = "",
    val hints: List<String> = emptyList(),
    val choices: List<String> = emptyList(),
)

@Serializable
data class ChariotSession(
    val id: String,
    val title: String,
    @SerialName("body_weather_flags") val bodyWeatherFlags: List<String> = emptyList(),
    @SerialName("duration_minutes") val durationMinutes: Int = 15,
    @SerialName("total_xp") val totalXp: Int = 0,
    @SerialName("welcome_voice_id") val welcomeVoiceId: String = "ignavarr",
    @SerialName("welcome_clip_id") val welcomeClipId: String = "welcome.mp3",
    @SerialName("welcome_category") val welcomeCategory: String = "welcome",
    @SerialName("celebration_clip_id") val celebrationClipId: String = "celebration-1.mp3",
    @SerialName("celebration_category") val celebrationCategory: String = "celebration",
    val story: String = "",
    @SerialName("deep_objectives") val deepObjectives: Map<String, String> = emptyMap(),
    val scaffolding: Map<String, String> = emptyMap(),
    @SerialName("parent_tips") val parentTips: List<String> = emptyList(),
    val activities: List<ChariotActivity> = emptyList(),
) {
    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun listFromJson(raw: String): List<ChariotSession> =
            json.decodeFromString<List<ChariotSession>>(raw)

        fun findForWeather(sessions: List<ChariotSession>, weather: String): ChariotSession? {
            val normalized = weather.lowercase()
            return sessions.firstOrNull { session ->
                session.bodyWeatherFlags.any { it.equals(normalized, ignoreCase = true) }
            } ?: sessions.firstOrNull()
        }
    }
}
