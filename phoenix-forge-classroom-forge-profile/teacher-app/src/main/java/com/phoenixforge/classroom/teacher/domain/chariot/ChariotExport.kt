package com.phoenixforge.classroom.teacher.domain.chariot

import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.TileStatus

/**
 * Builds the Chariot (Kia Soul head unit) quest stack from expedition tiles.
 * Ported from archive car-quest + system-initialization mission types.
 */
object ChariotExport {
    const val WELCOME_LINE = "Welcome to the Chariot of Champions! Car quest time!"
    const val MODE_EXPEDITION = "expedition_tiles"
    const val MODE_SAGE_SESSION = "sage_session"

    fun build(tiles: List<IntentTile>): ChariotQuestStack =
        buildExpedition(tiles)

    fun buildExpedition(tiles: List<IntentTile>): ChariotQuestStack {
        val missions = tiles
            .filter { isEligibleForChariot(it) }
            .sortedBy { it.sortOrder }
            .map { tile -> toMission(tile) }

        return ChariotQuestStack(
            exportMode = MODE_EXPEDITION,
            welcomeLine = WELCOME_LINE,
            missions = missions,
        )
    }

    fun buildFromSageSession(session: ChariotSession, weather: String = ""): ChariotQuestStack {
        val missions = session.activities.map { activity -> toMissionFromActivity(session, activity) }
        val weatherLabel = weather.ifBlank { session.bodyWeatherFlags.firstOrNull().orEmpty() }
        return ChariotQuestStack(
            exportMode = MODE_SAGE_SESSION,
            sessionId = session.id,
            weather = weatherLabel,
            bodyWeatherFlags = session.bodyWeatherFlags,
            welcomeLine = session.story.ifBlank { WELCOME_LINE },
            welcomeClipId = session.welcomeClipId,
            celebrationClipId = session.celebrationClipId,
            story = session.story,
            durationMinutes = session.durationMinutes,
            totalXp = session.totalXp,
            deepObjectives = session.deepObjectives,
            scaffolding = session.scaffolding,
            parentTips = session.parentTips,
            missions = missions,
        )
    }

    fun buildSageSessionForWeather(sessionsJson: String, weather: String): ChariotQuestStack {
        val sessions = ChariotSession.listFromJson(sessionsJson)
        val session = ChariotSession.findForWeather(sessions, weather)
            ?: error("No chariot session for weather: $weather")
        return buildFromSageSession(session, weather)
    }

    fun buildSageSessionJson(weather: String): String =
        buildSageSessionForWeather(loadBundledSessionsJson(), weather).toJson()

    fun loadBundledSessionsJson(): String =
        ChariotExport::class.java.classLoader
            ?.getResourceAsStream("chariot/sage-sessions-salvage.json")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: "[]"

    fun buildExportText(stack: ChariotQuestStack): String = buildString {
        appendLine("Phoenix Forge Classroom — Chariot Quest Stack")
        appendLine("Mode: ${stack.exportMode}")
        if (stack.sessionId.isNotBlank()) {
            appendLine("Session: ${stack.sessionId} (${stack.weather})")
        }
        appendLine(stack.welcomeLine)
        appendLine()
        if (stack.missions.isEmpty()) {
            appendLine("No car-friendly missions in today's stack.")
            appendLine("Tag tiles as Car-Friendly or add a student mission on the Expedition Board.")
        } else {
            stack.missions.forEachIndexed { index, mission ->
                appendLine("${index + 1}. ${mission.title}")
                appendLine("   Mission: ${mission.studentMission}")
                if (mission.missionCards.isNotEmpty()) {
                    appendLine("   Cards: ${mission.missionCards.joinToString(" · ")}")
                }
                appendLine("   XP: ${mission.xpReward}")
            }
        }
        appendLine()
        appendLine("Deep link (listening): phoenixforge://chariot/listening")
        appendLine("Deep link (deck): phoenixforge://chariot/deck")
        appendLine("JSON path on car: /sdcard/PhoenixForge/Chariot/quest-stack.json")
    }.trimEnd()

    fun isEligibleForChariot(tile: IntentTile): Boolean {
        if (TileStatus.fromName(tile.status) == TileStatus.COMPLETED) return false
        if (tile.carFriendly) return true
        if (tile.studentMission.isNotBlank()) return true
        val haystack = "${tile.title} ${tile.description} ${tile.materials}".lowercase()
        return CAR_KEYWORDS.any { haystack.contains(it) }
    }

    private val CAR_KEYWORDS = listOf(
        "car",
        "drive",
        "travel",
        "road",
        "backseat",
        "vehicle",
    )

    private fun toMissionFromActivity(session: ChariotSession, activity: ChariotActivity): ChariotMission {
        val missionText = activity.narrationText.ifBlank { session.title }
        return ChariotMission(
            tileId = "${session.id}:${activity.narrationClipId}",
            title = activity.activityType.ifBlank { session.title },
            studentMission = missionText,
            xpReward = activity.xp,
            missionCards = activity.hints.ifEmpty {
                listOf(activity.inputType, activity.realm).filter { it.isNotBlank() }
            },
            sparkLine = "Steward ${activity.voiceId} guides this car-safe step.",
            narrationClipId = activity.narrationClipId,
            narrationCategory = activity.narrationCategory,
            inputType = activity.inputType,
            voiceId = activity.voiceId,
            realm = activity.realm,
            sensoryLoad = activity.sensoryLoad,
            efDemand = activity.efDemand,
            correctAnswer = activity.correctAnswer,
            hints = activity.hints,
            choices = activity.choices,
        )
    }

    private fun toMission(tile: IntentTile): ChariotMission {
        val missionText = tile.studentMission.ifBlank {
            "Help with ${tile.title.lowercase()} on the ride."
        }
        val cards = missionCardsFrom(tile, missionText)
        return ChariotMission(
            tileId = tile.id,
            title = tile.title,
            studentMission = missionText,
            xpReward = 25,
            missionCards = cards,
            sparkLine = "Spark noticed your ${tile.title} work in the Chariot!",
        )
    }

    private fun missionCardsFrom(tile: IntentTile, missionText: String): List<String> {
        if (tile.title.isNotBlank()) return listOf(tile.title)
        return missionText.split('.', '!', '?')
            .map { it.trim() }
            .filter { it.length in 4..40 }
            .take(3)
            .ifEmpty { listOf("Car quest") }
    }
}
