package com.phoenixforge.profile.domain.access

import com.phoenixforge.profile.domain.model.ForgeProfile
import com.phoenixforge.profile.domain.model.ProfileRole
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileImportPolicyTest {

    @Test
    fun onlyChildRoleIsImportable() {
        assertTrue(ProfileImportPolicy.isImportableRole(ProfileRole.STUDENT_SELF.storageKey))
        assertFalse(ProfileImportPolicy.isImportableRole(ProfileRole.STEWARD_FOR_STUDENT.storageKey))
    }

    @Test
    fun teacherRoleIsBlocked() {
        assertFalse(ProfileImportPolicy.isImportableRole(ProfileRole.TEACHER_SELF.storageKey))
    }

    @Test
    fun selectImportableProfilePrefersChildProfile() {
        val adult = sampleProfile("Josh", ProfileRole.STEWARD_FOR_STUDENT.storageKey)
        val child = sampleProfile("Ezra", ProfileRole.STUDENT_SELF.storageKey)
        assertEquals(child.uid, ProfileImportPolicy.selectImportableProfile(listOf(adult, child))?.uid)
    }

    @Test
    fun selectImportableProfileIgnoresAdultOnly() {
        val adult = sampleProfile("Josh", ProfileRole.STEWARD_FOR_STUDENT.storageKey)
        assertEquals(null, ProfileImportPolicy.selectImportableProfile(listOf(adult)))
    }

    private fun sampleProfile(name: String, role: String) = ForgeProfile(
        uid = "$name-uid",
        forgeName = name,
        realName = null,
        birthDate = null,
        ageYears = 6,
        pronouns = null,
        favoriteColor = null,
        currentTitle = null,
        currentStage = "JUST_BEGINNING",
        sparkMaturationTier = 0,
        profileRole = role
    )
}
