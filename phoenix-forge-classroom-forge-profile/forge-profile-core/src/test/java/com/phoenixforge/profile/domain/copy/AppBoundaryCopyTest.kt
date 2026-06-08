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
        assertTrue(summary.contains("Nothing syncs"))
    }

    @Test
    fun userFacingCopyHasNoStewardTerminology() {
        val userFacing = listOf(
            AppBoundaryCopy.PARENT_GATE_TITLE,
            AppBoundaryCopy.PARENT_GATE_SUBTITLE,
            AppBoundaryCopy.PARENT_GATE_LOCKED_BODY,
            AppBoundaryCopy.PARENT_GATE_UNLOCKED_BODY,
            AppBoundaryCopy.REQUEST_PARENT_ACCESS,
            AppBoundaryCopy.ENABLE_PARENT_ON_DEVICE,
            AppBoundaryCopy.DISABLE_PARENT_ACCESS,
            AppBoundaryCopy.GATE_DENIED,
            AppBoundaryCopy.signInBoundaryHint(ProfileRole.STEWARD_FOR_STUDENT),
            AppBoundaryCopy.dashboardBoundaryLine(ProfileRole.STEWARD_FOR_STUDENT),
        ).joinToString("\n").lowercase()
        assertFalse(userFacing.contains("steward"))
        assertFalse(userFacing.contains("stewardship"))
    }

    @Test
    fun parentGateAvailableForAdultRolesOnly() {
        assertTrue(AppBoundaryCopy.canAccessParentGate(ProfileRole.STEWARD_FOR_STUDENT))
        assertTrue(AppBoundaryCopy.canAccessParentGate(ProfileRole.TEACHER_SELF))
        assertFalse(AppBoundaryCopy.canAccessParentGate(ProfileRole.STUDENT_SELF))
        assertFalse(AppBoundaryCopy.canAccessParentGate(null))
    }

    @Test
    fun memoryImmutableBoundaryPresent() {
        assertTrue(AppBoundaryCopy.MEMORY_IMMUTABLE.contains("cannot replace"))
    }
}
