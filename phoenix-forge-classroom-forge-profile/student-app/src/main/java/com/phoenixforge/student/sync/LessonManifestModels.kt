package com.phoenixforge.student.sync

import kotlinx.serialization.Serializable
import java.time.LocalDate

enum class QuestRoutineCategory(val storageKey: String, val label: String) {
    MORNING("morning_routine", "Morning routine"),
    NIGHT("night_routine", "Nighttime routine"),
    DAILY("daily_quest", "Today's quests");

    companion object {
        fun fromRoutineKind(kind: String): QuestRoutineCategory = when (kind.trim()) {
            "morning_routine" -> MORNING
            "night_routine" -> NIGHT
            else -> DAILY
        }
    }
}

fun LessonManifest.todayItems(
    today: LocalDate = LocalDate.now(),
    category: QuestRoutineCategory? = null,
): List<TodayManifestItem> {
    val dateIso = today.toString()
    if (!isValidForDate(today)) return emptyList()

    return days
        .filter { it.date == dateIso }
        .filter { day ->
            category == null || QuestRoutineCategory.fromRoutineKind(day.routineKind) == category
        }
        .sortedBy { it.dayIndex }
        .mapNotNull { day ->
            val questId = day.quests.firstOrNull()?.takeIf { it.isNotBlank() } ?: return@mapNotNull null
            TodayManifestItem(
                title = day.narrativeTitle,
                questId = questId,
                detailText = day.narrativeText,
                dayIndex = day.dayIndex,
                routineKind = day.routineKind,
            )
        }
}

fun LessonManifest.isValidForDate(today: LocalDate): Boolean {
    val from = runCatching { LocalDate.parse(validFromDate) }.getOrNull() ?: return false
    val to = runCatching { LocalDate.parse(validToDate) }.getOrNull() ?: return false
    return !today.isBefore(from) && !today.isAfter(to)
}

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

data class TodayManifestItem(
    val title: String,
    val questId: String,
    val detailText: String,
    val dayIndex: Int,
    val routineKind: String = "daily_quest",
)

sealed class ManifestReadResult {
    data class Ready(
        val manifest: LessonManifest,
        val items: List<TodayManifestItem>,
        val sourcePath: String,
    ) : ManifestReadResult()

    data object NotSignedIn : ManifestReadResult()

    data class NotFound(val studentUid: String, val searchedPaths: List<String>) : ManifestReadResult()

    data class Invalid(val reason: String, val sourcePath: String?) : ManifestReadResult()
}
