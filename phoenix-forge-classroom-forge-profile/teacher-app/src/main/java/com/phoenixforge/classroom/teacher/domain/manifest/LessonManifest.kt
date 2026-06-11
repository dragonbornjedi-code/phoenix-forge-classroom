package com.phoenixforge.classroom.teacher.domain.manifest

import kotlinx.serialization.Serializable

@Serializable
data class LessonManifest(
    val schemaVersion: Int = 1,
    val manifestId: String,
    val studentUid: String,
    val createdByDeviceId: String,
    val createdEpochMs: Long,
    val validFromDate: String,
    val validToDate: String,
    val days: List<LessonManifestDay>,
)

@Serializable
data class LessonManifestDay(
    val dayIndex: Int,
    val date: String,
    val narrativeTitle: String,
    val narrativeText: String = "",
    /** morning_routine | night_routine | daily_quest | "" */
    val routineKind: String = "daily_quest",
    val quests: List<String>,
    val unlockConditions: List<LessonUnlockCondition> = emptyList(),
    val rewardItems: List<String> = emptyList(),
)

@Serializable
data class LessonUnlockCondition(
    val type: String,
    val questId: String? = null,
)
