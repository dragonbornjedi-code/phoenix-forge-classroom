package com.phoenixforge.profile.data.sync

import android.os.Environment
import java.io.File

/** Writes JSON artifacts to the shared PhoenixForge/sync tree (Android 14+). */
object PublicSyncWriter {
    fun writeRelative(relativePath: String, payload: String): List<String> {
        val written = linkedSetOf<String>()
        candidateRoots().forEach { root ->
            runCatching {
                val file = File(root, relativePath)
                file.parentFile?.mkdirs()
                file.writeText(payload)
                if (file.isFile && file.length() > 0L) {
                    written += file.absolutePath
                }
            }
        }
        return written.toList()
    }

    fun candidateRoots(): List<String> = SyncPaths.PUBLIC_SYNC_ROOTS
}
