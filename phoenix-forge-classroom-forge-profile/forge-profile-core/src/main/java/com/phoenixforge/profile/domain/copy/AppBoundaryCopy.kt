package com.phoenixforge.profile.domain.copy

import com.phoenixforge.profile.domain.model.ProfileRole

/**
 * Canonical three-app boundary copy (master step 0.67).
 * Authority: docs/STUDENT_TEACHER_BOUNDARY.md
 *
 * User-facing strings use parent/adult/teacher — never "steward" (purged at step 4.59).
 */
object AppBoundaryCopy {

    const val FORGE_PROFILE_OWNS =
        "Forge Profile holds identity, avatar, photos, and timeline — the childhood record."

    const val STUDENT_EDITION_OWNS =
        "Student Edition is where Ezra explores, quests, and imports school photos into his vault."

    const val TEACHER_EDITION_OWNS =
        "Teacher Edition is where you plan lessons, run the expedition board, and view read-only student snapshots."

    const val MANUAL_SYNC =
        "Nothing syncs in the background. You push avatar and memories when you choose; Ezra pulls on his tablet."

    const val MEMORY_IMMUTABLE =
        "Ezra's original photo or voice stays the evidence. You can add notes — you cannot replace his capture."

    val threeAppSummary: String = listOf(
        FORGE_PROFILE_OWNS,
        STUDENT_EDITION_OWNS,
        TEACHER_EDITION_OWNS,
        MANUAL_SYNC,
    ).joinToString("\n")

    const val PARENT_GATE_TITLE = "Parent gate"
    const val PARENT_GATE_SUBTITLE =
        "Adult-only access on this device. Unlocks parent tools — not Ezra's play world."

    const val PARENT_GATE_LOCKED_BODY =
        "Enable parent access to open Teacher Edition from here and manage adult-only settings. " +
            "Ezra's Student Edition stays separate — he explores; you curate."

    const val PARENT_GATE_UNLOCKED_BODY =
        "Parent access is on for this device. Open Teacher Edition for curriculum and expedition work. " +
            "Keep capturing memories and avatar updates in Forge Profile."

    const val REQUEST_PARENT_ACCESS = "Unlock parent access"
    const val ENABLE_PARENT_ON_DEVICE = "Set up parent access on this device"
    const val OPEN_TEACHER_EDITION = "Open Teacher Edition"
    const val DISABLE_PARENT_ACCESS = "Turn off parent access on this device"

    const val GATE_DENIED = "Parent access was denied."
    const val GATE_NOT_CONFIGURED = "Parent access is not set up on this device yet."
    const val GATE_ENABLE_FAILED = "Could not enable parent access."
    const val GATE_SETUP_UNAVAILABLE = "Parent access setup is unavailable on this device."

    fun signInBoundaryHint(role: ProfileRole): String = when (role) {
        ProfileRole.STUDENT_SELF ->
            "Student profile — for Ezra's device. Exploration lives in Student Edition; identity lives here."
        ProfileRole.STEWARD_FOR_STUDENT ->
            "Parent profile — you manage Ezra's identity and memories here. Planning lives in Teacher Edition."
        ProfileRole.TEACHER_SELF ->
            "Teacher profile — link to curriculum in Teacher Edition. Child identity stays in Forge Profile."
    }

    fun dashboardBoundaryLine(role: ProfileRole?): String = when (role) {
        ProfileRole.STEWARD_FOR_STUDENT ->
            "Parent · Forge Profile = record · Student = play · Teacher = plan"
        ProfileRole.TEACHER_SELF ->
            "Teacher · Forge Profile = identity · Teacher Edition = mentorship board"
        ProfileRole.STUDENT_SELF ->
            "Student · Your avatar and timeline · Quests in Student Edition"
        null -> FORGE_PROFILE_OWNS
    }

    fun pushAvatarHint(childName: String = "Ezra"): String =
        "Manual push only — on $childName's tablet: Student Edition → Import Forge Profile → Pull snapshot."

    fun memorySchoolHint(): String =
        "School photos tagged here also flow to Student Edition → Gallery → Pull school from Profile."

    fun canAccessParentGate(role: ProfileRole?): Boolean =
        role == ProfileRole.STEWARD_FOR_STUDENT || role == ProfileRole.TEACHER_SELF
}
