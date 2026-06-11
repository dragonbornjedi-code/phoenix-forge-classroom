package com.phoenixforge.student.sync

import android.os.Environment
import java.io.File

/**
 * Writes JSON artifacts to the shared Syncthing tree on Android 14+ (targetSdk 34).
 * Requires All files access (MANAGE_EXTERNAL_STORAGE) or adb appops grant for public paths.
 */
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

    fun candidateRoots(): List<String> {
        val roots = ManifestSyncPaths.PUBLIC_SYNC_ROOTS.toMutableList()
        runCatching {
            val docs = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            roots += File(docs, ManifestSyncPaths.SYNC_ROOT_FOLDER).absolutePath
        }
        runCatching {
            val root = Environment.getExternalStorageDirectory()
            roots += File(root, ManifestSyncPaths.SYNC_ROOT_FOLDER).absolutePath
        }
        return roots.distinct()
    }

    fun hasPublicSyncAccess(): Boolean =
        Environment.isExternalStorageManager() || probeWritable()

    private fun probeWritable(): Boolean {
        val probePath = ".pf_probe/${System.currentTimeMillis()}.txt"
        val written = writeRelative(probePath, "ok")
        written.forEach { path -> runCatching { File(path).delete() } }
        return written.isNotEmpty()
    }
}
