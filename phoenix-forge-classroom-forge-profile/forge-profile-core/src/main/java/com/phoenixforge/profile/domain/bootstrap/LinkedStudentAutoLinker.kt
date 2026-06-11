package com.phoenixforge.profile.domain.bootstrap

import com.phoenixforge.profile.domain.model.ProfileRole
import com.phoenixforge.profile.domain.repository.ProfileRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.firstOrNull

/** Ensures child profiles on this device appear in linked_students for Teacher Edition. */
@Singleton
class LinkedStudentAutoLinker @Inject constructor(
    private val repository: ProfileRepository,
) {
    suspend fun linkAllChildrenOnDevice() {
        val children = repository.listProfiles().firstOrNull().orEmpty()
            .filter { ProfileRole.fromStorageKey(it.profileRole)?.isStudentProfile == true }
        if (children.isEmpty()) return
        val linked = repository.getLinkedStudents().firstOrNull().orEmpty()
            .map { it.profileUid }
            .toSet()
        children.filter { it.uid !in linked }.forEach { child ->
            repository.linkStudentProfile(
                displayName = child.forgeName,
                profileUid = child.uid,
                notes = "Linked from child profile on this device",
            )
        }
    }
}
