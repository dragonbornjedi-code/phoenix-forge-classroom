package com.phoenixforge.profile.data.projection

import android.util.Log
import com.phoenixforge.profile.data.local.dao.EventRecordDao
import com.phoenixforge.profile.data.serialization.ForgeProfileJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Read-only projection of ingested [forge_events] rows for LAN consumers.
 * All Room access runs on [Dispatchers.IO] to avoid blocking callers.
 */
@Singleton
class StateProjector @Inject constructor(
    private val eventRecordDao: EventRecordDao,
    private val forgeProfileJson: ForgeProfileJson,
) {
    suspend fun projectEvents(studentUid: String): EventsProjectionResponse = withContext(Dispatchers.IO) {
        val normalizedUid = studentUid.trim()
        require(normalizedUid.isNotEmpty()) { "studentUid required" }

        val rows = eventRecordDao.listEventsForStudent(normalizedUid)
        val json = forgeProfileJson.json
        val events = rows.map { it.toProjectedEvent(json) }

        Log.d(
            TAG,
            "StateProjector projected studentUid=$normalizedUid eventCount=${events.size}",
        )

        EventsProjectionResponse(
            updatedAt = turnStateUpdatedAt(),
            studentUid = normalizedUid,
            syncPaths = syncPathsForStudent(normalizedUid),
            eventCount = events.size,
            events = events,
        )
    }

    /** NanoHTTPD serves on its own thread pool — bridge with IO dispatcher only. */
    fun projectEventsBlocking(studentUid: String): EventsProjectionResponse =
        runBlocking { projectEvents(studentUid) }

    private companion object {
        const val TAG = "ForgeProfileProjection"
    }
}
