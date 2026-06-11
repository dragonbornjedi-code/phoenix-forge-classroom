package com.phoenixforge.profile.data.sync

import android.util.Log
import com.phoenixforge.profile.data.local.dao.ProfileDao
import com.phoenixforge.profile.domain.model.ProfileRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessageIngestCoordinator @Inject constructor(
    private val profileDao: ProfileDao,
    private val messageIngester: MessageIngester,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val refreshMutex = Mutex()
    private data class ActiveWatch(
        val watcher: EventFileWatcher,
        val studentUid: String,
        val messagesDir: File,
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

    fun rescanAll() {
        if (!started) return
        scope.launch {
            refreshMutex.withLock {
                watchers.values.forEach { active ->
                    messageIngester.ingestDirectory(active.messagesDir, active.studentUid)
                }
            }
        }
    }

    suspend fun refreshWatchTargets(studentUids: Set<String>? = null) {
        refreshMutex.withLock {
            val dbUids = studentUids ?: loadStudentProfileUids()
            val targets = dbUids + SyncPaths.scanStudentUidsOnDisk()
            val desiredKeys = mutableSetOf<String>()

            targets.forEach { studentUid ->
                SyncPaths.resolveMessagesDirs(studentUid).forEach messagesDirLoop@{ messagesDir ->
                    val key = messagesDir.absolutePath
                    desiredKeys += key
                    if (watchers.containsKey(key)) return@messagesDirLoop

                    messageIngester.ingestDirectory(messagesDir, studentUid)
                    val watcher = EventFileWatcher(messagesDir) { file ->
                        scope.launch {
                            delay(FILE_SETTLE_MS)
                            messageIngester.ingestFile(file, studentUid)
                        }
                    }
                    watcher.start()
                    watchers[key] = ActiveWatch(watcher, studentUid, messagesDir)
                }
            }

            val stale = watchers.keys - desiredKeys
            stale.forEach { key -> watchers.remove(key)?.watcher?.stop() }
            if (targets.isNotEmpty()) {
                Log.i(TAG, "watching message dirs for ${targets.size} student uid(s)")
            }
        }
    }

    private suspend fun loadStudentProfileUids(): Set<String> =
        profileDao.listProfiles().first()
            .filter { ProfileRole.fromStorageKey(it.profileRole)?.isStudentProfile == true }
            .map { it.uid }
            .toSet()

    private companion object {
        const val FILE_SETTLE_MS = 150L
        const val TAG = "MessageIngestCoordinator"
    }
}
