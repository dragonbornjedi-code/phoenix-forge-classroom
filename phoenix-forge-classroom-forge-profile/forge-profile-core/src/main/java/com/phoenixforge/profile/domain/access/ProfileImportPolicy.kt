package com.phoenixforge.profile.domain.access

import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.ProfileRole

/**
 * Which Forge Profile rows Student Edition may import.
 * Teacher/steward adult profiles never cross into the child app.
 */
object ProfileImportPolicy {

    fun isImportableRole(role: String?): Boolean =
        ProfileRole.fromStorageKey(role) == ProfileRole.STUDENT_SELF

    fun selectImportableProfile(profiles: List<ForgeProfile>): ForgeProfile? =
        profiles.firstOrNull { it.profileRole == ProfileRole.STUDENT_SELF.storageKey }

    fun rejectReason(role: String?): String = when (ProfileRole.fromStorageKey(role)) {
        ProfileRole.STEWARD_FOR_STUDENT ->
            "This is your adult Forge Profile. Switch to the child profile in Forge Profile, then pull again in Student Edition."
        ProfileRole.TEACHER_SELF ->
            "This Forge Profile is a legacy teacher account. Open the child profile, then pull again in Student Edition."
        ProfileRole.STUDENT_SELF -> "Profile is importable."
        null -> "Forge Profile has no role set. Recreate the child profile in Forge Profile."
    }
}
