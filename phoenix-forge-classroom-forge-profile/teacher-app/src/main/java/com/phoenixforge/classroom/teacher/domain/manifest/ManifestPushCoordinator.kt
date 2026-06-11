package com.phoenixforge.classroom.teacher.domain.manifest

import android.content.Context
import com.phoenixforge.classroom.teacher.data.repository.TileRepository
import com.phoenixforge.classroom.teacher.data.sync.ManifestPushTargetResolver
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManifestPushCoordinator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tileRepository: TileRepository,
    private val manifestPushTargetResolver: ManifestPushTargetResolver,
) {
    suspend fun pushTodayStack(): ManifestWriteResult? {
        val studentUid = manifestPushTargetResolver.resolveStudentUid()
        if (studentUid.isNullOrBlank()) {
            return null
        }

        val stackTiles = tileRepository.observeAll().first()
        val deviceId = TeacherDeviceIdStore.getOrCreate(context)
        val manifest = ManifestWriter.build(
            tiles = stackTiles,
            studentUid = studentUid,
            deviceId = deviceId,
        )
        val result = ManifestWriter.writeToDisk(context, manifest)
        manifestPushTargetResolver.rememberStudentUid(studentUid)
        return result
    }
}
