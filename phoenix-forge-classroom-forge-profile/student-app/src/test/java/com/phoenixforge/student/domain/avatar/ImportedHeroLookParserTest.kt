package com.phoenixforge.student.domain.avatar

import org.junit.Assert.assertEquals
import org.junit.Test

class ImportedHeroLookParserTest {

    @Test
    fun parsesHeroSummaryLine() {
        val parsed = ImportedHeroLookParser.parse("artist|purple|medium|mage|v3")
        requireNotNull(parsed)
        assertEquals("artist", parsed.style)
        assertEquals("purple", parsed.color)
        assertEquals(3, parsed.version)
    }
}
