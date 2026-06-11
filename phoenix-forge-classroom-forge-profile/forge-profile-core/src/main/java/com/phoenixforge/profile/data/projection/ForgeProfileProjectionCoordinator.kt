package com.phoenixforge.profile.data.projection

import android.util.Log
import com.phoenixforge.profile.data.network.ForgeProfileLanIdentity
import com.phoenixforge.profile.data.network.ForgeProfileMdnsRegistrar
import com.phoenixforge.profile.data.network.ForgeProfileSyncServer
import com.phoenixforge.profile.data.serialization.ForgeProfileJson
import com.phoenixforge.profile.data.sync.MessageDiskWriter
import com.phoenixforge.profile.data.sync.MessageIngester
import com.phoenixforge.profile.domain.session.ProfileSessionStore
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stage-1 lifecycle owner for the external projection layer.
 * HTTP (stage 2) and mDNS (stage 3) register here without touching ingest.
 */
@Singleton
class ForgeProfileProjectionCoordinator @Inject constructor(
    private val stateProjector: StateProjector,
    private val messageProjector: MessageProjector,
    private val messageIngester: MessageIngester,
    private val messageDiskWriter: MessageDiskWriter,
    private val forgeProfileJson: ForgeProfileJson,
    private val lanIdentity: ForgeProfileLanIdentity,
    private val sessionStore: ProfileSessionStore,
    private val mdnsRegistrar: ForgeProfileMdnsRegistrar,
) {
    private var started = false
    private var syncServer: ForgeProfileSyncServer? = null

    fun start() {
        if (started) return
        started = true
        Log.i(
            TAG,
            "Stage 1 ready: StateProjector bound lane=$CLASSROOM_LANE schema=$TURN_STATE_SCHEMA_VERSION",
        )
        startHttpServer()
    }

    fun stop() {
        stopHttpServer()
        if (!started) return
        started = false
        Log.i(TAG, "ForgeProfileProjectionCoordinator stopped")
    }

    fun stateProjector(): StateProjector = stateProjector

    private fun startHttpServer() {
        if (syncServer != null) return
        val server = ForgeProfileSyncServer(
            port = ForgeProfileSyncServer.PORT,
            stateProjector = stateProjector,
            messageProjector = messageProjector,
            messageIngester = messageIngester,
            messageDiskWriter = messageDiskWriter,
            forgeProfileJson = forgeProfileJson,
            lanIdentity = lanIdentity,
            sessionStore = sessionStore,
        )
        try {
            server.start(SOCKET_READ_TIMEOUT, false)
            syncServer = server
            Log.i(
                TAG,
                "Stage 2 ready: NanoHTTPD listening on :${ForgeProfileSyncServer.PORT} " +
                    "routes=/api/v1/ping,/api/v1/events/{studentUid},/api/v1/messages/{studentUid}",
            )
            mdnsRegistrar.start(ForgeProfileSyncServer.PORT)
        } catch (error: IOException) {
            Log.e(TAG, "Stage 2 failed: NanoHTTPD could not bind :${ForgeProfileSyncServer.PORT}", error)
        }
    }

    private fun stopHttpServer() {
        mdnsRegistrar.stop()
        syncServer?.stop()
        syncServer = null
    }

    private companion object {
        const val TAG = "ForgeProfileProjection"
        const val SOCKET_READ_TIMEOUT = 5000
    }
}
