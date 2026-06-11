package com.phoenixforge.profile.domain.access

import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.ProfileRole

/**
 * Child tablets: create/view child profiles only.
 * Adult devices (parent phone with Teacher Edition): adult + child profiles.
 */
object DeviceProfilePolicy {

    fun creatableProfileRoles(
        savedProfiles: List<ForgeProfile>,
        teacherEditionInstalled: Boolean,
    ): List<ProfileRole> {
        val hasAdultProfile = savedProfiles.any { profile ->
            ProfileRole.fromStorageKey(profile.profileRole)?.isAdultProfile == true
        }
        val adultCapableDevice = hasAdultProfile || teacherEditionInstalled
        return if (adultCapableDevice) {
            listOf(ProfileRole.STEWARD_FOR_STUDENT, ProfileRole.STUDENT_SELF)
        } else {
            listOf(ProfileRole.STUDENT_SELF)
        }
    }

    fun isAdultCapableDevice(
        savedProfiles: List<ForgeProfile>,
        teacherEditionInstalled: Boolean,
    ): Boolean = creatableProfileRoles(savedProfiles, teacherEditionInstalled).size > 1
}
