package com.phoenixforge.student.sync

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PublicStateWriter @Inject constructor(
    private val snapshotBuilder: PublicStateSnapshotBuilder,
) {
    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    suspend fun writeFromLocal(eventCount: Int = 0): Result<List<String>> = withContext(Dispatchers.IO) {
        val snapshot = snapshotBuilder.build(eventCount)
            ?: return@withContext Result.failure(IllegalStateException("Not signed in — cannot write public_state.json"))

        val encoded = json.encodeToString(snapshot)
        val relative = ManifestSyncPaths.publicStateRelativePath(snapshot.studentUid)
        val written = PublicSyncWriter.writeRelative(relative, encoded)
        if (written.isEmpty()) {
            Result.failure(IllegalStateException("Could not write public_state.json"))
        } else {
            Result.success(written)
        }
    }
}
