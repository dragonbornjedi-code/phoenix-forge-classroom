package com.phoenixforge.profile.domain.model

enum class ProfileRole(val storageKey: String, val label: String) {
    STUDENT_SELF("student_self", "Child / student profile"),
    TEACHER_SELF("teacher_self", "Teacher profile (legacy)"),
    STEWARD_FOR_STUDENT("steward_for_student", "My adult profile");

    val isTeacherCapable: Boolean
        get() = this == TEACHER_SELF || this == STEWARD_FOR_STUDENT

    /** Only child-owned profiles may import into Student Edition / Forge World. */
    val isStudentProfile: Boolean
        get() = this == STUDENT_SELF

    val isAdultProfile: Boolean
        get() = this == STEWARD_FOR_STUDENT || this == TEACHER_SELF

    companion object {
        fun fromStorageKey(key: String?): ProfileRole? =
            entries.firstOrNull { it.storageKey == key }
    }
}
