package com.phoenixforge.profile.data.sync

import android.util.Log
import com.phoenixforge.profile.data.local.dao.ProfileDao
import com.phoenixforge.profile.domain.model.ProfileRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Boots FileObservers for child profile event folders and ingests PUBLIC EVT_*.json into Room.
 */
@Singleton
class EventIngestCoordinator @Inject constructor(
    private val profileDao: ProfileDao,
    private val eventIngester: EventIngester,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val refreshMutex = Mutex()
    private data class ActiveWatch(
        val watcher: EventFileWatcher,
        val studentUid: String,
        val eventsDir: File,
    )

    private val watchers = mutableMapOf<String, ActiveWatch>()
    private var started = false

    fun start() {
        if (started) return
        started = true
        scope.launch { refreshWatchTargets() }
        scope.launch {
            profileDao.listProfiles()
                .map { profiles ->
                    profiles
                        .filter { ProfileRole.fromStorageKey(it.profileRole)?.isStudentProfile == true }
                        .map { it.uid }
                        .toSet()
                }
                .distinctUntilChanged()
                .collect { refreshWatchTargets(it) }
        }
    }

    fun stop() {
        started = false
        scope.launch {
            refreshMutex.withLock {
                watchers.values.forEach { it.watcher.stop() }
                watchers.clear()
            }
        }
    }

    /** Re-scan sync folders (FileObserver can miss files; call on app foreground). */
    fun rescanAll() {
        if (!started) return
        scope.launch {
            refreshMutex.withLock {
                watchers.values.forEach { active ->
                    eventIngester.ingestDirectory(active.eventsDir, active.studentUid)
                }
            }
        }
    }

    suspend fun refreshWatchTargets(studentUids: Set<String>? = null) {
        refreshMutex.withLock {
            val dbUids = studentUids ?: loadStudentProfileUids()
            val targets = dbUids + SyncPaths.scanStudentUidsOnDisk()
            if (targets.isNotEmpty()) {
                Log.i(TAG, "watching event dirs for ${targets.size} student uid(s)")
            }
            val desiredKeys = mutableSetOf<String>()

            targets.forEach { studentUid ->
                SyncPaths.resolveEventsDirs(studentUid).forEach eventsDirLoop@{ eventsDir ->
                    val key = eventsDir.absolutePath
                    desiredKeys += key
                    if (watchers.containsKey(key)) return@eventsDirLoop

                    eventIngester.ingestDirectory(eventsDir, studentUid)
                    val watcher = EventFileWatcher(eventsDir) { file ->
                        scope.launch {
                            delay(FILE_SETTLE_MS)
                            eventIngester.ingestFile(file, studentUid)
                        }
                    }
                    watcher.start()
                    watchers[key] = ActiveWatch(watcher, studentUid, eventsDir)
                }
            }

            val stale = watchers.keys - desiredKeys
            stale.forEach { key ->
                watchers.remove(key)?.watcher?.stop()
            }
        }
    }

    private companion object {
        const val FILE_SETTLE_MS = 150L
        const val TAG = "EventIngestCoordinator"
    }

    private suspend fun loadStudentProfileUids(): Set<String> {
        val profiles = profileDao.listProfiles().first()
        return profiles
            .filter { ProfileRole.fromStorageKey(it.profileRole)?.isStudentProfile == true }
            .map { it.uid }
            .toSet()
    }
}
