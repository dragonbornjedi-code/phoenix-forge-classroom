package com.phoenixforge.profile.data.sync

import android.os.Environment
import java.io.File

object SyncPaths {
    const val SYNC_ROOT_FOLDER = "PhoenixForge/sync/profiles"

    val PUBLIC_SYNC_ROOTS: List<String> = buildList {
        add("/sdcard/$SYNC_ROOT_FOLDER")
        add("/storage/emulated/0/$SYNC_ROOT_FOLDER")
        add("/storage/emulated/0/Documents/$SYNC_ROOT_FOLDER")
        runCatching {
            val docs = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            add(File(docs, SYNC_ROOT_FOLDER).absolutePath)
        }
    }.distinct()

    fun eventsRelativePath(studentUid: String): String = "$studentUid/events"

    fun messagesRelativePath(studentUid: String): String = "$studentUid/messages"

    fun publicStateRelativePath(studentUid: String): String = "$studentUid/public_state.json"

    fun eventFileName(eventId: String): String = "$eventId.json"

    fun resolveEventsDirs(studentUid: String): List<File> =
        PUBLIC_SYNC_ROOTS.map { root -> File(root, eventsRelativePath(studentUid)) }

    fun resolveMessagesDirs(studentUid: String): List<File> =
        PUBLIC_SYNC_ROOTS.map { root -> File(root, messagesRelativePath(studentUid)) }

    fun messageFileName(messageId: String): String = "$messageId.json"

    /** UUID folders under the public sync tree (Syncthing / Teacher push). */
    fun scanStudentUidsOnDisk(): Set<String> {
        val uidPattern = Regex(
            "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
            RegexOption.IGNORE_CASE,
        )
        val found = linkedSetOf<String>()
        PUBLIC_SYNC_ROOTS.forEach { root ->
            runCatching {
                File(root).listFiles()?.forEach { dir ->
                    if (dir.isDirectory && uidPattern.matches(dir.name)) {
                        found += dir.name
                    }
                }
            }
        }
        return found
    }
}
