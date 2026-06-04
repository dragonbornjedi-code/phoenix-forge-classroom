package com.phoenixforge.student.domain.simulation

import com.phoenixforge.student.domain.model.BehaviorSignals
import com.phoenixforge.student.domain.model.WorldEvent
import com.phoenixforge.student.domain.model.WorldEventType
import com.phoenixforge.student.domain.repository.StudentRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BehaviorSignalTracker @Inject constructor(
    private val repository: StudentRepository
) {
    private val weekMillis = 7L * 86_400_000L

    suspend fun current(): BehaviorSignals = repository.observeBehaviorSignals().first()

    suspend fun recordEvent(event: WorldEvent): BehaviorSignals {
        var signals = current()
        val now = System.currentTimeMillis()
        if (signals.weekAnchorEpochMillis == 0L || now - signals.weekAnchorEpochMillis > weekMillis) {
            signals = signals.copy(
                photosThisWeek = 0,
                questsCompletedThisWeek = 0,
                returnsThisWeek = 0,
                weekAnchorEpochMillis = now
            )
        }
        signals = when (event.type) {
            WorldEventType.PHOTO_UPLOADED -> signals.copy(
                photosThisWeek = signals.photosThisWeek + 1,
                lastPhotoEpochMillis = now
            )
            WorldEventType.QUEST_COMPLETE -> signals.copy(
                questsCompletedThisWeek = signals.questsCompletedThisWeek + 1,
                lastQuestEpochMillis = now
            )
            WorldEventType.DAILY_RETURN, WorldEventType.ABSENCE_RETURNED, WorldEventType.STREAK_MILESTONE ->
                signals.copy(returnsThisWeek = signals.returnsThisWeek + 1, lastVisitEpochMillis = now)
            else -> signals.copy(lastVisitEpochMillis = now)
        }
        repository.saveBehaviorSignals(signals)
        return signals
    }

    fun daysSinceLastVisit(signals: BehaviorSignals, now: Long = System.currentTimeMillis()): Long {
        if (signals.lastVisitEpochMillis == 0L) return 0L
        return (now - signals.lastVisitEpochMillis) / 86_400_000L
    }
}
