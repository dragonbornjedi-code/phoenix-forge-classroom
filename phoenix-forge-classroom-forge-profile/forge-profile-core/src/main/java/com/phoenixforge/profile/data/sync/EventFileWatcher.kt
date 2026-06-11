package com.phoenixforge.profile.data.sync

import android.os.FileObserver
import java.io.File

/**
 * Watches one student's events directory for append-only EVT_*.json files.
 */
class EventFileWatcher(
    private val eventsDir: File,
    private val onEventFile: (File) -> Unit,
) {
    private var observer: FileObserver? = null

    val watchKey: String = eventsDir.absolutePath

    fun start() {
        stop()
        if (!eventsDir.exists()) {
            eventsDir.mkdirs()
        }
        observer = object : FileObserver(
            eventsDir,
            CREATE or CLOSE_WRITE or MOVED_TO,
        ) {
            override fun onEvent(event: Int, path: String?) {
                if (path == null) return
                if (!path.startsWith("EVT_") || !path.endsWith(".json")) return
                onEventFile(File(eventsDir, path))
            }
        }
        observer?.startWatching()
    }

    fun stop() {
        observer?.stopWatching()
        observer = null
    }
}
