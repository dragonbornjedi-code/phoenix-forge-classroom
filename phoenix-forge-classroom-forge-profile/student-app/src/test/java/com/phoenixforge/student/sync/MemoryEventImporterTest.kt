package com.phoenixforge.student.sync

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MemoryEventImporterTest {

    @Test
    fun parseContractLineAcceptsForgeWorldShape() {
        val line = """
            {
              "eventId": "fw-00000001-0001-0001-0001-000000000001",
              "version": "1.0",
              "capturedAt": "2026-06-08T14:30:00Z",
              "childAgeMonths": 72,
              "sourceShell": "FORGE_WORLD_GODOT_V1",
              "eventType": "MILESTONE",
              "narrative": {
                "title": "Quest complete",
                "summary": "Star Cartographer Ezra!",
                "childMood": "HAPPY"
              },
              "context": {
                "locationId": "realm_thinking-tower",
                "weather": null,
                "involvedEntities": ["IGNAVARR"]
              },
              "assets": [],
              "impact": { "threadRef": null, "wispAffinities": {} }
            }
        """.trimIndent().replace("\n", "")

        val importer = MemoryEventContractParser
        val parsed = importer.parseContractLine(line)
        assertTrue(parsed is MemoryEventParseResult.Valid)
        val draft = (parsed as MemoryEventParseResult.Valid).draft
        assertEquals("MILESTONE", draft.eventType)
        assertEquals("Star Cartographer Ezra!", draft.summary)
    }

    @Test
    fun parseContractLineRejectsWrongSourceShell() {
        val line = """{"eventId":"fw-12345678","version":"1.0","capturedAt":"2026-06-08T14:30:00Z","sourceShell":"STUDENT_APK_V1","eventType":"DISCOVERY","narrative":{"title":"t","summary":"s","childMood":"HAPPY"},"context":{"locationId":"x"}}"""
        val parsed = MemoryEventContractParser.parseContractLine(line)
        assertTrue(parsed is MemoryEventParseResult.Quarantined)
    }
}
