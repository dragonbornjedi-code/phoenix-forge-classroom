package com.phoenixforge.classroom.teacher.domain.chariot

import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.TileStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ChariotExportTest {

    @Test
    fun carFriendlyTileIncludedInStack() {
        val stack = ChariotExport.build(
            listOf(
                tile(title = "Road signs", carFriendly = true, mission = "Spot three signs."),
                tile(title = "Done", status = TileStatus.COMPLETED, carFriendly = true),
            ),
        )
        assertEquals(1, stack.missions.size)
        assertEquals("Road signs", stack.missions.first().title)
    }

    @Test
    fun studentMissionIncludesTileWithoutExplicitCarFlag() {
        val stack = ChariotExport.build(
            listOf(tile(title = "Decoder", mission = "Match labels in the car.")),
        )
        assertEquals(1, stack.missions.size)
        assertTrue(stack.missions.first().studentMission.contains("Match labels"))
    }

    @Test
    fun jsonRoundTripPreservesMission() {
        val original = ChariotExport.build(
            listOf(tile(title = "Brain", carFriendly = true, mission = "Count to ten.")),
        )
        val parsed = ChariotQuestStack.fromJson(original.toJson())
        assertEquals(original.missions.first().title, parsed.missions.first().title)
        assertEquals(original.welcomeLine, parsed.welcomeLine)
    }

    @Test
    fun exportTextMentionsDeepLinks() {
        val text = ChariotExport.buildExportText(ChariotExport.build(emptyList()))
        assertTrue(text.contains("phoenixforge://chariot/listening"))
        assertFalse(text.contains("null"))
    }

    @Test
    fun sageSessionExportIncludesClipIdsAndMissions() {
        val session = ChariotSession(
            id = "session-sunny-001",
            title = "Sunrise Adventure",
            bodyWeatherFlags = listOf("sunny"),
            welcomeClipId = "welcome.mp3",
            celebrationClipId = "celebration-1.mp3",
            story = "Ignavarr welcomes the ride.",
            activities = listOf(
                ChariotActivity(
                    realm = "adventure-grounds",
                    voiceId = "steward_physical",
                    narrationClipId = "movement-stretches.mp3",
                    narrationCategory = "movement",
                    narrationText = "Squeeze your hands tight.",
                    inputType = "done",
                    xp = 5,
                ),
            ),
        )
        val stack = ChariotExport.buildFromSageSession(session, "sunny")
        assertEquals(ChariotExport.MODE_SAGE_SESSION, stack.exportMode)
        assertEquals("session-sunny-001", stack.sessionId)
        assertEquals("welcome.mp3", stack.welcomeClipId)
        assertEquals(1, stack.missions.size)
        assertEquals("movement-stretches.mp3", stack.missions.first().narrationClipId)
        assertEquals("steward_physical", stack.missions.first().voiceId)
    }

    @Test
    fun bundledSageSessionsLoadForSunnyWeather() {
        val json = ChariotExport.loadBundledSessionsJson()
        assertTrue(json.contains("session-sunny-001"))
        val stack = ChariotExport.buildSageSessionForWeather(json, "sunny")
        assertEquals(ChariotExport.MODE_SAGE_SESSION, stack.exportMode)
        assertTrue(stack.missions.isNotEmpty())
    }

    @Test
    fun sageSessionJsonRoundTrip() {
        val session = ChariotSession(
            id = "session-cloudy-001",
            title = "Cloudy",
            bodyWeatherFlags = listOf("cloudy"),
            activities = listOf(
                ChariotActivity(
                    realm = "thinking-tower",
                    voiceId = "steward_cognitive",
                    narrationClipId = "math-addition-3plus2.mp3",
                    narrationCategory = "math",
                    narrationText = "How many apples?",
                    inputType = "number",
                ),
            ),
        )
        val stack = ChariotExport.buildFromSageSession(session)
        val parsed = ChariotQuestStack.fromJson(stack.toJson())
        assertEquals(stack.sessionId, parsed.sessionId)
        assertEquals(stack.missions.first().narrationClipId, parsed.missions.first().narrationClipId)
    }

    private fun tile(
        title: String,
        carFriendly: Boolean = false,
        mission: String = "",
        status: TileStatus = TileStatus.PLANNED,
    ) = IntentTile(
        title = title,
        domain = ForgeDomain.LANGUAGE.name,
        carFriendly = carFriendly,
        studentMission = mission,
        status = status.name,
    )
}
