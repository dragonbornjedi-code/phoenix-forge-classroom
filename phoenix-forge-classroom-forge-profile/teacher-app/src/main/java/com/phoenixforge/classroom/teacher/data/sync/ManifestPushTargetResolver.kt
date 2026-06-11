package com.phoenixforge.classroom.teacher.data.sync

import com.phoenixforge.classroom.teacher.data.bridge.ChildStudentUidResolver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManifestPushTargetResolver @Inject constructor(
    private val childStudentUidResolver: ChildStudentUidResolver,
) {
    suspend fun resolveStudentUid(): String? =
        childStudentUidResolver.resolve()?.studentUid

    fun rememberStudentUid(uid: String) {
        childStudentUidResolver.rememberStudentUid(uid)
    }
}
