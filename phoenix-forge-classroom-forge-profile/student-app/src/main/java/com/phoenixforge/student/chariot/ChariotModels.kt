package com.phoenixforge.student.chariot

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
        const val STACK_PATH = "/sdcard/PhoenixForge/Chariot/quest-stack.json"

        private val chariotJson = Json { ignoreUnknownKeys = true }

        fun fromJson(raw: String): ChariotQuestStack =
            chariotJson.decodeFromString<ChariotQuestStack>(raw)

        fun defaults(): ChariotQuestStack = ChariotQuestStack(
            missions = listOf(
                ChariotMission(
                    title = "Brain Mission",
                    studentMission = "Spot three signs or labels on the ride.",
                    missionCards = listOf("Find a sign", "Read a word", "Tell Spark"),
                    xpReward = 20,
                ),
                ChariotMission(
                    title = "Kindness Mission",
                    studentMission = "Say thank you to someone in the car.",
                    missionCards = listOf("Thank you"),
                    xpReward = 15,
                ),
            ),
        )
    }
}

object ChariotStackLoader {
    fun load(): ChariotQuestStack {
        val file = java.io.File(ChariotQuestStack.STACK_PATH)
        if (!file.exists()) return ChariotQuestStack.defaults()
        return runCatching { ChariotQuestStack.fromJson(file.readText()) }
            .getOrDefault(ChariotQuestStack.defaults())
    }
}

enum class ChariotMode { LISTENING, CELEBRATION, DECK, WELCOME }

data class ChariotDisplayArgs(
    val mode: ChariotMode,
    val xp: Int = 20,
    val questTitle: String = "Quest",
    val message: String = "",
)

object ChariotDeepLink {
    const val SCHEME = "phoenixforge"
    const val HOST = "chariot"

    fun parse(uri: android.net.Uri?): ChariotDisplayArgs? {
        if (uri == null || uri.scheme != SCHEME || uri.host != HOST) return null
        val segment = uri.pathSegments.firstOrNull()?.lowercase() ?: return null
        val mode = when (segment) {
            "listening" -> ChariotMode.LISTENING
            "celebration", "celebrate" -> ChariotMode.CELEBRATION
            "deck", "stack" -> ChariotMode.DECK
            "welcome" -> ChariotMode.WELCOME
            else -> return null
        }
        return ChariotDisplayArgs(
            mode = mode,
            xp = uri.getQueryParameter("xp")?.toIntOrNull() ?: 20,
            questTitle = uri.getQueryParameter("quest").orEmpty().ifBlank { "Quest" },
            message = uri.getQueryParameter("message").orEmpty(),
        )
    }
}
