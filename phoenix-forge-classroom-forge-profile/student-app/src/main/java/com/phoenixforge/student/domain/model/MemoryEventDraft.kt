package com.phoenixforge.student.domain.model

data class MemoryEventDraft(
    val eventId: String,
    val version: String,
    val capturedAt: String,
    val sourceShell: String,
    val eventType: String,
    val title: String,
    val summary: String,
    val childMood: String,
    val locationId: String,
    val contractJson: String,
    val importedAtEpochMillis: Long,
    val importSource: String,
)
