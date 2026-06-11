package com.phoenixforge.profile.data.network

import android.content.Context
import android.net.wifi.WifiManager
import android.util.Log
import com.phoenixforge.profile.data.projection.CLASSROOM_LANE
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import javax.inject.Inject
import javax.inject.Singleton
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Advertises Forge Profile LAN HTTP on the classroom service name for cross-lane discovery.
 */
@Singleton
class ForgeProfileMdnsRegistrar @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var jmdns: JmDNS? = null
    private var multicastLock: WifiManager.MulticastLock? = null

    fun start(port: Int = ForgeProfileSyncServer.PORT) {
        if (jmdns != null) return
        scope.launch {
            val localAddress = resolveLocalIpv4()
            if (localAddress == null) {
                Log.w(TAG, "Stage 3 skipped: no LAN IPv4 address for mDNS")
                return@launch
            }

            acquireMulticastLock()
            runCatching {
                // Explicit hostname avoids reverse-DNS on the main thread (Android StrictMode).
                val instance = JmDNS.create(localAddress, SERVICE_NAME)
                val serviceInfo = ServiceInfo.create(
                    SERVICE_TYPE,
                    SERVICE_NAME,
                    port,
                    0,
                    0,
                    mapOf(
                        "lane" to CLASSROOM_LANE,
                        "path" to "/api/v1/events",
                        "ping" to "/api/v1/ping",
                    ),
                )
                instance.registerService(serviceInfo)
                jmdns = instance
                Log.i(
                    TAG,
                    "Stage 3 ready: mDNS $SERVICE_NAME.$SERVICE_TYPE on ${localAddress.hostAddress}:$port",
                )
            }.onFailure { error ->
                Log.e(TAG, "Stage 3 failed: mDNS registration error", error)
                releaseMulticastLock()
            }
        }
    }

    fun stop() {
        scope.launch {
            runCatching { jmdns?.unregisterAllServices() }
            runCatching { jmdns?.close() }
            jmdns = null
            releaseMulticastLock()
        }
    }

    private fun acquireMulticastLock() {
        val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        multicastLock = wifi.createMulticastLock("forge-profile-mdns").apply {
            setReferenceCounted(true)
            acquire()
        }
    }

    private fun releaseMulticastLock() {
        multicastLock?.let { lock ->
            if (lock.isHeld) lock.release()
        }
        multicastLock = null
    }

    private fun resolveLocalIpv4(): InetAddress? {
        return NetworkInterface.getNetworkInterfaces()?.toList().orEmpty()
            .asSequence()
            .filter { it.isUp && !it.isLoopback }
            .flatMap { it.inetAddresses.toList().asSequence() }
            .firstOrNull { !it.isLoopbackAddress && it is Inet4Address }
    }

    companion object {
        private const val TAG = "ForgeProfileProjection"
        const val SERVICE_NAME = "phoenix-forge-classroom"
        const val SERVICE_TYPE = "_http._tcp.local."
    }
}
