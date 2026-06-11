package com.phoenixforge.profile.data.network

import com.phoenixforge.profile.data.projection.CLASSROOM_LANE
import com.phoenixforge.profile.data.projection.EventsProjectionResponse
import com.phoenixforge.profile.data.projection.MessageProjector
import com.phoenixforge.profile.data.projection.StateProjector
import com.phoenixforge.profile.data.projection.TURN_STATE_SCHEMA_VERSION
import com.phoenixforge.profile.data.serialization.ForgeProfileJson
import com.phoenixforge.profile.data.sync.ForgeMessageParser
import com.phoenixforge.profile.data.sync.ForgeMessageRecord
import com.phoenixforge.profile.data.sync.MessageDiskWriter
import com.phoenixforge.profile.data.sync.MessageIngester
import com.phoenixforge.profile.domain.session.ProfileSessionStore
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import java.util.HashMap

class ForgeProfileSyncServer(
    port: Int,
    private val stateProjector: StateProjector,
    private val messageProjector: MessageProjector,
    private val messageIngester: MessageIngester,
    private val messageDiskWriter: MessageDiskWriter,
    private val forgeProfileJson: ForgeProfileJson,
    private val lanIdentity: ForgeProfileLanIdentity,
    private val sessionStore: ProfileSessionStore,
) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri.trimEnd('/')
        return when (session.method) {
            Method.GET -> when {
                uri == PING_PATH -> jsonResponse(Response.Status.OK, buildPing())
                uri.startsWith(EVENTS_PREFIX) -> eventsResponse(uri.removePrefix(EVENTS_PREFIX))
                uri.startsWith(MESSAGES_PREFIX) -> messagesGetResponse(uri.removePrefix(MESSAGES_PREFIX), session)
                else -> textResponse(Response.Status.NOT_FOUND, "not found")
            }
            Method.POST -> when {
                uri.startsWith(MESSAGES_PREFIX) -> messagesPostResponse(uri.removePrefix(MESSAGES_PREFIX), session)
                else -> textResponse(Response.Status.NOT_FOUND, "not found")
            }
            else -> textResponse(Response.Status.METHOD_NOT_ALLOWED, "method not allowed")
        }
    }

    private fun eventsResponse(rawStudentUid: String): Response {
        val studentUid = rawStudentUid.trim()
        if (!STUDENT_UID_PATTERN.matches(studentUid)) {
            return textResponse(Response.Status.BAD_REQUEST, "invalid studentUid")
        }
        return runCatching {
            val projection = stateProjector.projectEventsBlocking(studentUid)
            jsonResponse(Response.Status.OK, projection)
        }.getOrElse {
            textResponse(Response.Status.INTERNAL_ERROR, "projection failed")
        }
    }

    private fun messagesGetResponse(rawStudentUid: String, session: IHTTPSession): Response {
        val studentUid = rawStudentUid.substringBefore('?').trim()
        if (!STUDENT_UID_PATTERN.matches(studentUid)) {
            return textResponse(Response.Status.BAD_REQUEST, "invalid studentUid")
        }
        val targetApp = session.parameters["targetApp"]?.firstOrNull()
        return runCatching {
            val projection = messageProjector.projectMessagesBlocking(studentUid, targetApp)
            jsonResponse(Response.Status.OK, projection)
        }.getOrElse {
            textResponse(Response.Status.INTERNAL_ERROR, "message projection failed")
        }
    }

    private fun messagesPostResponse(rawStudentUid: String, session: IHTTPSession): Response {
        val studentUid = rawStudentUid.trim()
        if (!STUDENT_UID_PATTERN.matches(studentUid)) {
            return textResponse(Response.Status.BAD_REQUEST, "invalid studentUid")
        }
        val body = readBody(session)
        if (body.isBlank()) {
            return textResponse(Response.Status.BAD_REQUEST, "empty body")
        }
        return runBlocking {
            val record = ForgeMessageParser.parse(body).getOrElse {
                return@runBlocking textResponse(Response.Status.BAD_REQUEST, "invalid message json")
            }
            if (record.toStudentUid != studentUid) {
                return@runBlocking textResponse(Response.Status.BAD_REQUEST, "studentUid path/body mismatch")
            }
            messageDiskWriter.write(record)
            val outcome = messageIngester.ingestRecord(record, sourcePath = "lan:${session.remoteIpAddress}")
            val status = when (outcome.status.name) {
                "INGESTED", "ALREADY_PRESENT" -> Response.Status.CREATED
                else -> Response.Status.BAD_REQUEST
            }
            jsonResponse(
                status,
                MessagePostResponse(
                    messageId = record.messageId,
                    status = outcome.status.name,
                    detail = outcome.detail,
                ),
            )
        }
    }

    private fun readBody(session: IHTTPSession): String {
        val files = HashMap<String, String>()
        return runCatching {
            session.parseBody(files)
            files["postData"] ?: ""
        }.getOrDefault("")
    }

    private fun buildPing(): PingResponse =
        PingResponse(
            schemaVersion = TURN_STATE_SCHEMA_VERSION,
            lane = CLASSROOM_LANE,
            deviceId = lanIdentity.deviceId(),
            activeProfileUid = sessionStore.getActiveProfileUid(),
            serverTimeMs = System.currentTimeMillis(),
            port = PORT,
        )

    private inline fun <reified T> jsonResponse(status: Response.Status, body: T): Response =
        newFixedLengthResponse(
            status,
            MIME_JSON,
            forgeProfileJson.json.encodeToString(body),
        )

    private fun textResponse(status: Response.Status, message: String): Response =
        newFixedLengthResponse(status, MIME_PLAINTEXT, message)

    @Serializable
    data class PingResponse(
        @SerialName("schema_version") val schemaVersion: String,
        val lane: String,
        @SerialName("deviceId") val deviceId: String,
        @SerialName("activeProfileUid") val activeProfileUid: String? = null,
        @SerialName("serverTimeMs") val serverTimeMs: Long,
        val port: Int,
    )

    @Serializable
    data class MessagePostResponse(
        @SerialName("messageId") val messageId: String,
        val status: String,
        val detail: String,
    )

    companion object {
        const val PORT = 7433
        private const val MIME_JSON = "application/json"
        private const val MIME_PLAINTEXT = "text/plain"
        private const val PING_PATH = "/api/v1/ping"
        private const val EVENTS_PREFIX = "/api/v1/events/"
        private const val MESSAGES_PREFIX = "/api/v1/messages/"
        private val STUDENT_UID_PATTERN = Regex("^[0-9a-fA-F-]{36}$")
    }
}
