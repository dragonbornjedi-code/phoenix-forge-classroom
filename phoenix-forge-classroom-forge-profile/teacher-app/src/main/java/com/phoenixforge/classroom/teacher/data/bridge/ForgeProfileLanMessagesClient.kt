package com.phoenixforge.classroom.teacher.data.bridge

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Serializable
data class LanMessagePostBody(
    @SerialName("schemaVersion") val schemaVersion: Int = 1,
    @SerialName("messageId") val messageId: String,
    @SerialName("threadId") val threadId: String,
    @SerialName("direction") val direction: String,
    @SerialName("fromDeviceId") val fromDeviceId: String,
    @SerialName("fromDisplayName") val fromDisplayName: String = "",
    @SerialName("toStudentUid") val toStudentUid: String,
    @SerialName("targetApp") val targetApp: String,
    @SerialName("epochMs") val epochMs: Long,
    @SerialName("logicalClock") val logicalClock: Long = 0,
    @SerialName("subject") val subject: String,
    @SerialName("bodyMarkdown") val bodyMarkdown: String,
    @SerialName("readEpochMs") val readEpochMs: Long? = null,
    @SerialName("replyToMessageId") val replyToMessageId: String? = null,
)

@Singleton
class ForgeProfileLanMessagesClient @Inject constructor() {

    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
    private val http = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .writeTimeout(5, TimeUnit.SECONDS)
        .build()

    suspend fun postMessage(
        host: String,
        body: LanMessagePostBody,
        port: Int = PORT,
    ): Boolean = withContext(Dispatchers.IO) {
        val payload = json.encodeToString(body)
        val request = Request.Builder()
            .url("http://$host:$port/api/v1/messages/${body.toStudentUid}")
            .post(payload.toRequestBody(JSON_MEDIA))
            .build()
        runCatching {
            http.newCall(request).execute().use { it.isSuccessful }
        }.getOrDefault(false)
    }

    suspend fun ping(host: String): Boolean = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("http://$host:$PORT/api/v1/ping")
            .get()
            .build()
        runCatching {
            http.newCall(request).execute().use { it.isSuccessful }
        }.getOrDefault(false)
    }

    private companion object {
        const val PORT = 7433
        val JSON_MEDIA = "application/json; charset=utf-8".toMediaType()
    }
}
