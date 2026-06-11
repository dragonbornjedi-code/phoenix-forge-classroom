package com.phoenixforge.student.domain.importpolicy

/**
 * Mirror of forge-profile-core ProfileImportPolicy — student-app has no core dependency.
 */
object StudentProfileImportPolicy {
    fun isImportableRole(role: String?): Boolean = role == "student_self"

    fun rejectReason(role: String?): String = when (role) {
        "steward_for_student" ->
            "This is an adult Forge Profile. Switch to the child profile in Forge Profile, then pull again here."
        "teacher_self" ->
            "This Forge Profile is a legacy teacher account. Open the child profile in Forge Profile, then pull again here."
        "student_self" -> "Profile is importable."
        null -> "Forge Profile has no role set. Recreate the child profile in Forge Profile."
        else -> "This profile cannot be imported into Student Edition."
    }
}
