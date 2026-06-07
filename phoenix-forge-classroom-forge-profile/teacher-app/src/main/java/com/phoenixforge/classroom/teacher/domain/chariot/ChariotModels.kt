package com.phoenixforge.classroom.teacher.domain.chariot

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
)

@Serializable
data class ChariotQuestStack(
    val schemaVersion: Int = 1,
    val welcomeLine: String = "Welcome to the Chariot of Champions! Car quest time!",
    val missions: List<ChariotMission> = emptyList(),
) {
    fun toJson(): String = chariotJson.encodeToString(this)

    companion object {
        private val chariotJson = Json { ignoreUnknownKeys = true; prettyPrint = true }

        fun fromJson(raw: String): ChariotQuestStack =
            chariotJson.decodeFromString<ChariotQuestStack>(raw)
    }
}
