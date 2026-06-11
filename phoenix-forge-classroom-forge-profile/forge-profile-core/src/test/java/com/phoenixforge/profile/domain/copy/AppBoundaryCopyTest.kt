package com.phoenixforge.profile.domain.copy

import com.phoenixforge.profile.domain.model.ProfileRole
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppBoundaryCopyTest {

    @Test
    fun threeAppSummaryNamesEachEdition() {
        val summary = AppBoundaryCopy.threeAppSummary
        assertTrue(summary.contains("Forge Profile"))
        assertTrue(summary.contains("Student Edition"))
        assertTrue(summary.contains("Teacher Edition"))
        assertTrue(summary.contains("Phoenix Forge World"))
        assertTrue(summary.contains("Nothing syncs"))
    }

    @Test
    fun userFacingCopyHasNoStewardTerminology() {
        val userFacing = listOf(
            AppBoundaryCopy.OPEN_TEACHER_EDITION,
            AppBoundaryCopy.signInBoundaryHint(ProfileRole.STEWARD_FOR_STUDENT),
            AppBoundaryCopy.dashboardBoundaryLine(ProfileRole.STEWARD_FOR_STUDENT),
            AppBoundaryCopy.adultTeacherHint(),
        ).joinToString("\n").lowercase()
        assertFalse(userFacing.contains("steward"))
        assertFalse(userFacing.contains("stewardship"))
    }

    @Test
    fun teacherEditionAvailableForAdultRolesOnly() {
        assertTrue(AppBoundaryCopy.canOpenTeacherEdition(ProfileRole.STEWARD_FOR_STUDENT))
        assertTrue(AppBoundaryCopy.canOpenTeacherEdition(ProfileRole.TEACHER_SELF))
        assertFalse(AppBoundaryCopy.canOpenTeacherEdition(ProfileRole.STUDENT_SELF))
    }

    @Test
    fun snapshotPushOnlyOnChildProfile() {
        assertTrue(AppBoundaryCopy.canPushPlaySnapshot(ProfileRole.STUDENT_SELF))
        assertFalse(AppBoundaryCopy.canPushPlaySnapshot(ProfileRole.STEWARD_FOR_STUDENT))
    }

    @Test
    fun memoryImmutableBoundaryPresent() {
        assertTrue(AppBoundaryCopy.MEMORY_IMMUTABLE.contains("cannot replace"))
    }
}
