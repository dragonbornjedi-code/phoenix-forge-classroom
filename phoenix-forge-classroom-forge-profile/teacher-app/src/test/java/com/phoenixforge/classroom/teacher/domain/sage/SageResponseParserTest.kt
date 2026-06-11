package com.phoenixforge.classroom.teacher.domain.sage

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SageResponseParserTest {

    @Test
    fun parsesSageApplyBlockAndStripsFromDisplay() {
        val raw = """
            Here are three quests for today.

            ```sage_apply
            {"actions":[{"op":"create","title":"Label Hunt","domain":"LANGUAGE","student_mission":"Find the secret labels!"}]}
            ```
        """.trimIndent()

        val parsed = SageResponseParser.parse(raw)
        assertTrue(parsed.displayText.contains("three quests"))
        assertTrue(!parsed.displayText.contains("sage_apply"))
        assertEquals(1, parsed.actions.size)
        assertEquals("Label Hunt", parsed.actions.first().title)
    }
}
