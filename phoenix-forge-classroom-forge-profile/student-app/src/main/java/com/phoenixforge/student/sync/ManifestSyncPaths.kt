package com.phoenixforge.student.sync

object ManifestSyncPaths {
    const val SYNC_ROOT_FOLDER = "PhoenixForge/sync/profiles"

    val PUBLIC_SYNC_ROOTS = listOf(
        "/sdcard/$SYNC_ROOT_FOLDER",
        "/storage/emulated/0/$SYNC_ROOT_FOLDER",
        "/storage/emulated/0/Documents/$SYNC_ROOT_FOLDER",
    )

    val APP_EXTERNAL_SUFFIX = "PhoenixForge/sync/profiles"

    fun manifestFileName(dateIso: String): String = "lesson_manifest_$dateIso.json"

    fun manifestRelativePath(studentUid: String, dateIso: String): String =
        "$studentUid/manifests/${manifestFileName(dateIso)}"

    fun eventsRelativePath(studentUid: String): String = "$studentUid/events"

    fun publicStateRelativePath(studentUid: String): String = "$studentUid/public_state.json"

    fun eventFileName(eventId: String): String = "$eventId.json"
}
