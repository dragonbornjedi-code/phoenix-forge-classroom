package com.phoenixforge.classroom.teacher.data.bridge

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class LanForgeEvent(
    val eventId: String,
    val eventType: String,
    val scope: String,
    val actorApp: String,
    val logicalClock: Long,
    val epochMs: Long,
)

@Serializable
data class LanEventsResponse(
    @SerialName("schema_version") val schemaVersion: String = "",
    val studentUid: String = "",
    @SerialName("event_count") val eventCount: Int = 0,
    val events: List<LanForgeEvent> = emptyList(),
)

@Singleton
class ForgeProfileLanEventsClient @Inject constructor() {

    private val json = Json { ignoreUnknownKeys = true }

    private val http = OkHttpClient.Builder()
        .connectTimeout(2, TimeUnit.SECONDS)
        .readTimeout(3, TimeUnit.SECONDS)
        .build()

    suspend fun fetchEvents(studentUid: String, host: String = LOCAL_HOST): LanEventsResponse? =
        withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("http://$host:$PORT/api/v1/events/$studentUid")
                .get()
                .build()
            runCatching {
                http.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) return@withContext null
                    val body = response.body?.string().orEmpty()
                    if (body.isBlank()) return@withContext null
                    json.decodeFromString(LanEventsResponse.serializer(), body)
                }
            }.getOrNull()
        }

    suspend fun ping(host: String = LOCAL_HOST): Boolean = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("http://$host:$PORT/api/v1/ping")
            .get()
            .build()
        runCatching {
            http.newCall(request).execute().use { it.isSuccessful }
        }.getOrDefault(false)
    }

    private companion object {
        const val LOCAL_HOST = "127.0.0.1"
        const val PORT = 7433
    }
}
