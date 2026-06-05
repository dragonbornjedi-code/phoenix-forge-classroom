package com.phoenixforge.classroom.teacher.domain.sage

import com.phoenixforge.classroom.teacher.data.network.NetworkGate
import com.phoenixforge.classroom.teacher.data.security.SecureCredentialStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

data class SageChatMessage(val role: String, val content: String)

sealed class SageChatResult {
    data class Success(val reply: String) : SageChatResult()
    data class Error(val message: String) : SageChatResult()
}

@Singleton
class SageChatService @Inject constructor(
    private val credentials: SecureCredentialStore,
    private val networkGate: NetworkGate,
    private val knowledgeBase: CurriculumKnowledgeBase
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .build()

    suspend fun send(
        history: List<SageChatMessage>,
        userMessage: String
    ): SageChatResult = withContext(Dispatchers.IO) {
        if (!networkGate.isOnline()) {
            return@withContext SageChatResult.Error("Connect to the internet for a Sage session.")
        }
        val apiKey = credentials.getApiKey()
        if (apiKey.isNullOrBlank()) {
            return@withContext SageChatResult.Error("Add an API key in Sage settings first.")
        }

        val context = knowledgeBase.buildContext()
        val system = SagePersona.systemPrompt(context)
        val messages = JSONArray().apply {
            put(jsonMessage("system", system))
            history.forEach { put(jsonMessage(it.role, it.content)) }
            put(jsonMessage("user", userMessage))
        }

        val body = JSONObject()
            .put("model", credentials.getModelId())
            .put("messages", messages)
            .toString()
            .toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(credentials.getProviderUrl())
            .addHeader("Authorization", "Bearer $apiKey")
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        runCatching {
            client.newCall(request).execute().use { response ->
                val raw = response.body?.string().orEmpty()
                if (!response.isSuccessful) {
                    return@withContext SageChatResult.Error("API ${response.code}: ${raw.take(200)}")
                }
                val json = JSONObject(raw)
                val content = json.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                SageChatResult.Success(content.trim())
            }
        }.getOrElse { e ->
            SageChatResult.Error(e.message ?: "Sage request failed")
        }
    }

    private fun jsonMessage(role: String, content: String): JSONObject =
        JSONObject().put("role", role).put("content", content)
}
