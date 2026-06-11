package com.phoenixforge.classroom.teacher.domain.messages

import android.content.Context
import com.phoenixforge.classroom.teacher.data.bridge.ForgeProfileLanDiscovery
import com.phoenixforge.classroom.teacher.data.bridge.ForgeProfileLanMessagesClient
import com.phoenixforge.classroom.teacher.data.bridge.LanMessagePostBody
import com.phoenixforge.classroom.teacher.data.sync.ManifestPushTargetResolver
import com.phoenixforge.classroom.teacher.domain.manifest.TeacherDeviceIdStore
import com.phoenixforge.classroom.teacher.domain.manifest.PublicSyncWriter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class MessageRelayResult(
    val messageId: String,
    val writtenPaths: List<String>,
    val lanDeliveries: Int,
    val message: String,
)

@Singleton
class MessageRelayCoordinator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val manifestPushTargetResolver: ManifestPushTargetResolver,
    private val lanMessagesClient: ForgeProfileLanMessagesClient,
    private val lanDiscovery: ForgeProfileLanDiscovery,
) {
    private val json = Json { prettyPrint = true; encodeDefaults = true }

    suspend fun sendToStudent(
        subject: String,
        body: String,
        fromDisplayName: String = "Dad",
        targetApp: String = TARGET_STUDENT,
    ): MessageRelayResult? {
        val studentUid = manifestPushTargetResolver.resolveStudentUid()?.trim().orEmpty()
        if (studentUid.isBlank()) return null

        val deviceId = TeacherDeviceIdStore.getOrCreate(context)
        val epochMs = System.currentTimeMillis()
        val messageId = "MSG_${deviceId}_${epochMs}_0001"
        val record = LanMessagePostBody(
            messageId = messageId,
            threadId = "steward-$studentUid",
            direction = DIRECTION_TO_STUDENT,
            fromDeviceId = deviceId,
            fromDisplayName = fromDisplayName,
            toStudentUid = studentUid,
            targetApp = targetApp,
            epochMs = epochMs,
            subject = subject.trim(),
            bodyMarkdown = body.trim(),
        )

        val relative = "$studentUid/messages/$messageId.json"
        val payload = json.encodeToString(record)
        val written = PublicSyncWriter.writeRelative(relative, payload)

        var deliveries = 0
        if (lanMessagesClient.ping(LOCAL_HOST)) {
            if (lanMessagesClient.postMessage(LOCAL_HOST, record)) deliveries++
        }
        lanDiscovery.discoverPeers().forEach { peer ->
            if (lanMessagesClient.postMessage(peer.host, record, peer.port)) {
                deliveries++
            }
        }

        manifestPushTargetResolver.rememberStudentUid(studentUid)
        val detail = when {
            deliveries > 0 -> "Delivered on LAN to $deliveries Forge Profile node(s)."
            written.isNotEmpty() -> "Saved to sync folder — will appear when profiles sync."
            else -> "Could not write message. Grant storage on this device."
        }
        return MessageRelayResult(
            messageId = messageId,
            writtenPaths = written,
            lanDeliveries = deliveries,
            message = detail,
        )
    }

    private companion object {
        const val LOCAL_HOST = "127.0.0.1"
        const val TARGET_STUDENT = "student_edition"
        const val DIRECTION_TO_STUDENT = "TO_STUDENT"
    }
}
