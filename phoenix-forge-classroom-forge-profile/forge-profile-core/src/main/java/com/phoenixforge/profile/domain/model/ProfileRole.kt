package com.phoenixforge.profile.domain.model

enum class ProfileRole(val storageKey: String, val label: String) {
    STUDENT_SELF("student_self", "I am the student"),
    TEACHER_SELF("teacher_self", "I am a teacher"),
    STEWARD_FOR_STUDENT("steward_for_student", "Creating a profile for a student");

    val isTeacherCapable: Boolean
        get() = this == TEACHER_SELF

    val isStudentProfile: Boolean
        get() = this == STUDENT_SELF || this == STEWARD_FOR_STUDENT

    companion object {
        fun fromStorageKey(key: String?): ProfileRole? =
            entries.firstOrNull { it.storageKey == key }
    }
}
