package com.phoenixforge.student.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "behavior_signals")
data class BehaviorSignalsEntity(
    @PrimaryKey val id: Int = 1,
    val photosThisWeek: Int,
    val questsCompletedThisWeek: Int,
    val returnsThisWeek: Int,
    val lastPhotoEpochMillis: Long,
    val lastQuestEpochMillis: Long,
    val lastVisitEpochMillis: Long,
    val weekAnchorEpochMillis: Long
)
