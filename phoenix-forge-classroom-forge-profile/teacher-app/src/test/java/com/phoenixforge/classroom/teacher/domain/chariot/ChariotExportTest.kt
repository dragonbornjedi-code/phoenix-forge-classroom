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
