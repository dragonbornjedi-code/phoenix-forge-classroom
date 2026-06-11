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
        "Teacher Edition is where you plan lessons, run the expedition board, and push the daily stack after you approve it."

    const val FORGE_WORLD_OWNS =
        "Phoenix Forge World is Ezra's 3D house — same avatar and companion progress as Student Edition via forge_profile_push.json."

    const val MANUAL_SYNC =
        "Nothing syncs in the background. Each app writes JSON to the shared sync folder when you choose; Forge Profile ingests events for one timeline."

    const val MEMORY_IMMUTABLE =
        "Ezra's original photo or voice stays the evidence. You can add notes — you cannot replace his capture."

    val threeAppSummary: String = listOf(
        FORGE_PROFILE_OWNS,
        STUDENT_EDITION_OWNS,
        TEACHER_EDITION_OWNS,
        FORGE_WORLD_OWNS,
        MANUAL_SYNC,
    ).joinToString("\n")

    const val OPEN_TEACHER_EDITION = "Open Teacher Edition"
    const val OPEN_STUDENT_EDITION = "Open Student Edition"
    const val PUSH_CHILD_SNAPSHOT = "Push snapshot to Student & Forge World"

    fun signInBoundaryHint(role: ProfileRole): String = when (role) {
        ProfileRole.STUDENT_SELF ->
            "Child profile — identity, avatar, and timeline. Push snapshot from here; quests live in Student Edition."
        ProfileRole.STEWARD_FOR_STUDENT ->
            "Your adult profile — your identity and timeline. Teacher Edition plans the day and pushes the quest stack."
        ProfileRole.TEACHER_SELF ->
            "Legacy teacher profile — use Teacher Edition for planning. Create a separate child profile for Student Edition import."
    }

    fun dashboardBoundaryLine(role: ProfileRole?): String = when (role) {
        ProfileRole.STEWARD_FOR_STUDENT ->
            "Adult · Teacher Edition = plan & push daily stack · Switch to child profile to push avatar snapshot"
        ProfileRole.TEACHER_SELF ->
            "Teacher · Teacher Edition = expedition board · Child profile = play identity"
        ProfileRole.STUDENT_SELF ->
            "Your hero, your look — send it to quests and your 3D world when you're ready."
        null -> FORGE_PROFILE_OWNS
    }

    fun childDashboardTagline(forgeName: String?): String =
        "Hey ${forgeName ?: "Forger"}! Pick your hero look, then jump into adventures."

    fun adultTeacherHint(): String =
        "Daily quests push from Teacher Edition (Expedition Board → Push today's stack) after you approve — not from this adult profile."

    fun pushAvatarHint(childName: String = "the child"): String =
        "Writes forge_profile_push.json for Student Edition import and Forge World. Switch to the child profile before pushing $childName's avatar."

    fun memorySchoolHint(): String =
        "School photos tagged here also flow to Student Edition → Gallery → Pull school from Profile."

    fun canOpenTeacherEdition(role: ProfileRole?): Boolean =
        role == ProfileRole.STEWARD_FOR_STUDENT || role == ProfileRole.TEACHER_SELF

    fun canPushPlaySnapshot(role: ProfileRole?): Boolean =
        role == ProfileRole.STUDENT_SELF

    fun canOpenStudentEdition(role: ProfileRole?): Boolean =
        role == ProfileRole.STUDENT_SELF
}
