package com.phoenixforge.profile.data.provider

/**
 * Process-safe DTOs for cross-app ContentProvider queries. Never expose Room entities.
 */
data class ProfileExportDto(
    val uid: String,
    val forgeName: String,
    val currentStage: String,
    val currentTitle: String?
)

data class AvatarExportDto(
    val hairType: String,
    val eyeColor: String,
    val skinTone: String,
    val clothingId: String,
    val version: Int
)

data class TimelineEventExportDto(
    val title: String,
    val type: String,
    val timestampEpochMillis: Long
)
