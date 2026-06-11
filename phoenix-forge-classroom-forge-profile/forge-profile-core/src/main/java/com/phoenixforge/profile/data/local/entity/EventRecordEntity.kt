package com.phoenixforge.profile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "forge_events")
data class EventRecordEntity(
    @PrimaryKey val eventId: String,
    val eventType: String,
    val scope: String,
    val actorApp: String,
    val actorDeviceId: String,
    val studentUid: String,
    val logicalClock: Long,
    val epochMs: Long,
    val payloadJson: String,
    val sourcePath: String,
    val ingestedAtMs: Long = System.currentTimeMillis(),
)
