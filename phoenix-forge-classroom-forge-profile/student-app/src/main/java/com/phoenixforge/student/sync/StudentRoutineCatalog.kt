package com.phoenixforge.student.sync

/**
 * Built-in morning/night routines — always on Student Quests tab, never in daily manifest push.
 */
object StudentRoutineCatalog {
    fun itemsFor(category: QuestRoutineCategory): List<TodayManifestItem> = when (category) {
        QuestRoutineCategory.MORNING -> morningRoutine()
        QuestRoutineCategory.NIGHT -> nightRoutine()
        QuestRoutineCategory.DAILY -> emptyList()
    }

    private fun morningRoutine(): List<TodayManifestItem> = listOf(
        TodayManifestItem(
            title = "Morning circle",
            questId = "morning_circle_routine",
            detailText = """
                Do morning circle with your grown-up: breathe, stretch, and share one feeling.
                Pick calm music or quiet stretch tones. Pause between poses until everyone taps Start.
                Close with Questions Corner: one wonder, one grateful thing, one plan for the day.
            """.trimIndent(),
            dayIndex = 0,
            routineKind = "morning_routine",
        ),
    )

    private fun nightRoutine(): List<TodayManifestItem> = listOf(
        TodayManifestItem(
            title = "Night wind-down",
            questId = "night_wind_down_routine",
            detailText = """
                Pick one calm thing: bath, story, or quiet music — then lights-out plan.
                Three slow breaths → one grateful thing → one tomorrow hope → goodnight.
            """.trimIndent(),
            dayIndex = 0,
            routineKind = "night_routine",
        ),
    )
}
