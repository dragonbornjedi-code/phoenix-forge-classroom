package com.phoenixforge.profile.data.provider

/**
 * Process-safe DTOs for cross-app ContentProvider queries. Never expose Room entities.
 */
data class ProfileExportDto(
    val uid: String,
    val forgeName: String,
    val currentStage: String,
    val currentTitle: String?,
    val ageYears: Int?,
    val profileRole: String?
)

data class AvatarExportDto(
    val hairType: String,
    val eyeColor: String,
    val skinTone: String,
    val clothingId: String,
    val version: Int,
    val shardLevel: Int,
    val heroStyle: String,
    val heroColor: String,
    val godotModelPath: String,
    val avatarSummary: String,
    val avatarConfigJson: String,
)

data class TimelineEventExportDto(
    val title: String,
    val type: String,
    val timestampEpochMillis: Long
)

data class MemoryExportDto(
    val id: String,
    val type: String,
    val category: String,
    val source: String,
    val capturedAtEpochMillis: Long,
    val note: String?,
    val checksum: String,
    val syncedToStudent: Boolean,
    val contentUri: String,
)
