package com.phoenixforge.classroom.teacher.domain.sage

import org.junit.Assert.assertTrue
import org.junit.Test

class SagePersonaTest {

    @Test
    fun systemPromptIncludesCanonicalQuestFrameworkAndReferenceMission() {
        val prompt = SagePersona.systemPrompt("DOMAIN 1: test")

        assertTrue(prompt.contains("SAGE QUEST FRAMEWORK"))
        assertTrue(prompt.contains("Sense of Urgency"))
        assertTrue(prompt.contains(SagePersona.REFERENCE_MISSION_TITLE))
        assertTrue(prompt.contains(SagePersona.REFERENCE_MISSION_SUMMARY))
        assertTrue(prompt.contains("observe, adjust, retry"))
        assertTrue(prompt.contains("Never diagnose"))
        assertTrue(prompt.contains("DOMAIN 1: test"))
    }

    @Test
    fun questInstructionsRequireStudentMissionStructure() {
        val block = SagePersona.questGenerationInstructions()

        assertTrue(block.contains("narrative_hook"))
        assertTrue(block.contains("student_mission"))
        assertTrue(block.contains("mission_cards"))
        assertTrue(block.contains("spark_reaction_seed"))
    }
}
