package com.phoenixforge.classroom.teacher.data.bridge

import android.content.Context
import android.net.wifi.WifiManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import javax.inject.Inject
import javax.inject.Singleton
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

data class LanForgePeer(
    val host: String,
    val port: Int,
    val deviceId: String?,
)

@Singleton
class ForgeProfileLanDiscovery @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun discoverPeers(timeoutMs: Long = 2500L): List<LanForgePeer> = withContext(Dispatchers.IO) {
        val local = resolveLocalIpv4() ?: return@withContext emptyList()
        val found = linkedMapOf<String, LanForgePeer>()
        var lock: WifiManager.MulticastLock? = null
        var jmdns: JmDNS? = null
        try {
            val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            lock = wifi.createMulticastLock("teacher-forge-discovery").apply {
                setReferenceCounted(true)
                acquire()
            }
            jmdns = JmDNS.create(local, "teacher-forge-discovery")
            val listener = object : ServiceListener {
                override fun serviceAdded(event: ServiceEvent) {
                    jmdns?.requestServiceInfo(event.type, event.name, true)
                }

                override fun serviceRemoved(event: ServiceEvent) = Unit

                override fun serviceResolved(event: ServiceEvent) {
                    val info = event.info ?: return
                    val host = info.hostAddresses?.firstOrNull { it.contains('.') }
                        ?: info.inetAddresses?.firstOrNull()?.hostAddress
                        ?: return
                    if (host == local.hostAddress) return
                    found[host] = LanForgePeer(
                        host = host,
                        port = info.port,
                        deviceId = info.getPropertyString("deviceId"),
                    )
                }
            }
            jmdns.addServiceListener(SERVICE_TYPE, listener)
            withTimeoutOrNull(timeoutMs) {
                kotlinx.coroutines.delay(timeoutMs)
            }
            jmdns.removeServiceListener(SERVICE_TYPE, listener)
        } catch (_: Exception) {
            // discovery is best-effort
        } finally {
            runCatching { jmdns?.close() }
            lock?.let { if (it.isHeld) it.release() }
        }
        found.values.toList()
    }

    private fun resolveLocalIpv4(): InetAddress? =
        NetworkInterface.getNetworkInterfaces()?.toList().orEmpty()
            .asSequence()
            .filter { it.isUp && !it.isLoopback }
            .flatMap { it.inetAddresses.toList().asSequence() }
            .firstOrNull { !it.isLoopbackAddress && it is Inet4Address }

    companion object {
        const val SERVICE_TYPE = "_http._tcp.local."
        const val SERVICE_NAME = "phoenix-forge-classroom"
    }
}
