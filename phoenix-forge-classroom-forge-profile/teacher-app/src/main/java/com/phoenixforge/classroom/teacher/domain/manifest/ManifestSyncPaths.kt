package com.phoenixforge.classroom.teacher.domain.manifest

object ManifestSyncPaths {
    const val SYNC_ROOT_FOLDER = "PhoenixForge/sync/profiles"

    val PUBLIC_SYNC_ROOTS = listOf(
        "/sdcard/$SYNC_ROOT_FOLDER",
        "/storage/emulated/0/$SYNC_ROOT_FOLDER",
        "/storage/emulated/0/Documents/$SYNC_ROOT_FOLDER",
    )

    fun manifestFileName(dateIso: String): String = "lesson_manifest_$dateIso.json"

    fun manifestRelativePath(studentUid: String, dateIso: String): String =
        "$studentUid/manifests/${manifestFileName(dateIso)}"
}
