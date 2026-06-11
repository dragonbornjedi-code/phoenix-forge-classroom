package com.phoenixforge.profile.domain.access

import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.ProfileRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DeviceProfilePolicyTest {

    @Test
    fun childOnlyDeviceWhenNoAdultProfileAndNoTeacherApp() {
        val roles = DeviceProfilePolicy.creatableProfileRoles(
            savedProfiles = listOf(sample("Test child", ProfileRole.STUDENT_SELF)),
            teacherEditionInstalled = false,
        )
        assertEquals(listOf(ProfileRole.STUDENT_SELF), roles)
    }

    @Test
    fun adultCapableWhenTeacherAppInstalled() {
        val roles = DeviceProfilePolicy.creatableProfileRoles(
            savedProfiles = emptyList(),
            teacherEditionInstalled = true,
        )
        assertTrue(roles.contains(ProfileRole.STEWARD_FOR_STUDENT))
        assertTrue(roles.contains(ProfileRole.STUDENT_SELF))
    }

    @Test
    fun adultCapableWhenAdultProfileExistsEvenWithoutTeacherApp() {
        val roles = DeviceProfilePolicy.creatableProfileRoles(
            savedProfiles = listOf(sample("Josh", ProfileRole.STEWARD_FOR_STUDENT)),
            teacherEditionInstalled = false,
        )
        assertEquals(2, roles.size)
    }

    @Test
    fun isAdultCapableDeviceMatchesCreatableRoleCount() {
        assertFalse(
            DeviceProfilePolicy.isAdultCapableDevice(emptyList(), teacherEditionInstalled = false),
        )
        assertTrue(
            DeviceProfilePolicy.isAdultCapableDevice(emptyList(), teacherEditionInstalled = true),
        )
    }

    private fun sample(name: String, role: ProfileRole) = ForgeProfile(
        uid = "uid-$name",
        forgeName = name,
        realName = null,
        birthDate = null,
        ageYears = 30,
        pronouns = null,
        favoriteColor = null,
        currentTitle = null,
        currentStage = "JUST_BEGINNING",
        sparkMaturationTier = 0,
        profileRole = role.storageKey,
    )
}
