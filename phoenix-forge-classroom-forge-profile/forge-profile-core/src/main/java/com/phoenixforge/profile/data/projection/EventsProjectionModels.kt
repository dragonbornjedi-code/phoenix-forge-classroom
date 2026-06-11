package com.phoenixforge.profile.data.projection

import com.phoenixforge.profile.data.local.entity.EventRecordEntity
import com.phoenixforge.profile.data.sync.SyncPaths
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.Json
import java.time.Instant

@Serializable
data class SyncPathsSnapshot(
    @SerialName("profile_root") val profileRoot: String,
    @SerialName("events_dir") val eventsDir: String,
    @SerialName("manifests_dir") val manifestsDir: String,
    @SerialName("turn_state_path") val turnStatePath: String? = null,
)

@Serializable
data class ProjectedEventRecord(
    val eventId: String,
    val eventType: String,
    val scope: String,
    val actorApp: String,
    val actorDeviceId: String,
    val studentUid: String,
    val logicalClock: Long,
    val epochMs: Long,
    val payload: JsonElement,
    val ingestedAtMs: Long,
)

@Serializable
data class EventsProjectionResponse(
    @SerialName("schema_version") val schemaVersion: String = TURN_STATE_SCHEMA_VERSION,
    @SerialName("updated_at") val updatedAt: String,
    val lane: String = CLASSROOM_LANE,
    val studentUid: String,
    @SerialName("sync_paths") val syncPaths: SyncPathsSnapshot,
    @SerialName("event_count") val eventCount: Int,
    val events: List<ProjectedEventRecord>,
    @SerialName("master_step") val masterStep: String = "1.05",
)

fun EventRecordEntity.toProjectedEvent(json: Json): ProjectedEventRecord =
    ProjectedEventRecord(
        eventId = eventId,
        eventType = eventType,
        scope = scope,
        actorApp = actorApp,
        actorDeviceId = actorDeviceId,
        studentUid = studentUid,
        logicalClock = logicalClock,
        epochMs = epochMs,
        payload = json.parseToJsonElement(payloadJson),
        ingestedAtMs = ingestedAtMs,
    )

fun syncPathsForStudent(studentUid: String): SyncPathsSnapshot {
    val profileRoot = "${SyncPaths.PUBLIC_SYNC_ROOTS.first()}/$studentUid"
    return SyncPathsSnapshot(
        profileRoot = profileRoot,
        eventsDir = "$profileRoot/events",
        manifestsDir = "$profileRoot/manifests",
    )
}

fun turnStateUpdatedAt(): String = Instant.now().toString()

const val TURN_STATE_SCHEMA_VERSION = "turn_state_v1"
const val CLASSROOM_LANE = "classroom"
