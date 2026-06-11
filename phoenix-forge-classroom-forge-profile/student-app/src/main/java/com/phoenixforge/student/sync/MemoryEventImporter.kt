package com.phoenixforge.student.sync

import com.phoenixforge.student.domain.model.LifeEvent
import com.phoenixforge.student.domain.model.LifeEventType
import com.phoenixforge.student.domain.model.MemoryEventDraft
import com.phoenixforge.student.domain.repository.StudentRepository
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

data class MemoryEventImportResult(
    val imported: Int,
    val skipped: Int,
    val quarantined: Int,
    val message: String,
)

sealed class MemoryEventParseResult {
    data class Valid(val draft: MemoryEventDraft) : MemoryEventParseResult()
    data class Quarantined(val reason: String) : MemoryEventParseResult()
}

/**
 * Ingests Forge World NDJSON MemoryEvents into the Student draft buffer.
 * Forge World emits; Student buffers; Profile owns permanence.
 */
@Singleton
class MemoryEventImporter @Inject constructor(
    private val repository: StudentRepository,
) {
    fun exportPaths(): List<String> = EXPORT_PATHS

    suspend fun importNewEvents(): MemoryEventImportResult {
        val existingIds = repository.listMemoryEventDraftIds().toSet()
        var imported = 0
        var skipped = 0
        var quarantined = 0
        var sourcePath = ""

        for (path in EXPORT_PATHS) {
            val file = File(path)
            if (!file.exists()) continue
            sourcePath = path
            file.readLines().forEach { raw ->
                val line = raw.trim()
                if (line.isEmpty()) return@forEach
                when (val parsed = MemoryEventContractParser.parseContractLine(line)) {
                    is MemoryEventParseResult.Valid -> {
                        if (parsed.draft.eventId in existingIds) {
                            skipped++
                        } else if (repository.insertMemoryEventDraftIfNew(parsed.draft)) {
                            imported++
                        } else {
                            skipped++
                        }
                    }
                    is MemoryEventParseResult.Quarantined -> quarantined++
                }
            }
            break
        }

        if (imported > 0) {
            repository.recordLifeEvent(
                LifeEvent(
                    type = LifeEventType.FORGE_WORLD_MEMORY_IMPORTED,
                    payload = "imported=$imported source=$sourcePath",
                )
            )
        }

        return MemoryEventImportResult(
            imported = imported,
            skipped = skipped,
            quarantined = quarantined,
            message = when {
                sourcePath.isEmpty() ->
                    "No Forge World export found. Play in Godot, then sync ${EXPORT_PATHS.first()}."
                imported > 0 -> "Imported $imported Forge World memory draft(s)."
                quarantined > 0 -> "Found export file but $quarantined line(s) failed contract validation."
                skipped > 0 -> "All Forge World events were already in the draft buffer."
                else -> "Export file was empty."
            },
        )
    }

    companion object {
        const val FORGE_WORLD_SOURCE_SHELL = "FORGE_WORLD_GODOT_V1"
        const val EXPORT_FILENAME = "forge_world_memory_events.ndjson"

        val EXPORT_PATHS = listOf(
            "/sdcard/PhoenixForge/export/$EXPORT_FILENAME",
            "/storage/emulated/0/PhoenixForge/export/$EXPORT_FILENAME",
        )
    }
}
