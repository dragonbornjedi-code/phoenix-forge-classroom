package com.phoenixforge.student.chariot

import org.junit.Assert.assertEquals
import org.junit.Test

class ChariotStackTest {

    @Test
    fun fromJsonRestoresMissions() {
        val json = """
            {
              "welcomeLine": "Car quest time!",
              "missions": [
                {
                  "title": "Brain Mission",
                  "studentMission": "Spot three signs.",
                  "xpReward": 20,
                  "missionCards": ["Sign 1", "Sign 2"]
                }
              ]
            }
        """.trimIndent()

        val stack = ChariotQuestStack.fromJson(json)
        assertEquals("Car quest time!", stack.welcomeLine)
        assertEquals(1, stack.missions.size)
        assertEquals("Brain Mission", stack.missions.first().title)
        assertEquals(2, stack.missions.first().missionCards.size)
    }

    @Test
    fun defaultsProvideFallbackMissions() {
        assertEquals(2, ChariotQuestStack.defaults().missions.size)
    }
}
