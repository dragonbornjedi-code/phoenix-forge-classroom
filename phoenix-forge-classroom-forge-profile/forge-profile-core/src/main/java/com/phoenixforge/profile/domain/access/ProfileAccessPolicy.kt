package com.phoenixforge.profile.domain.access

import com.phoenixforge.profile.domain.model.ProfileRole

/**
 * Which Forge Profile surfaces each role may see.
 * Forge Profile stays identity/chronicle-only. Teacher Edition owns student data views.
 */
object ProfileAccessPolicy {

    enum class Surface(val route: String, val label: String) {
        DASHBOARD("dashboard", "Home"),
        STUDIO("studio", "Studio"),
        TIMELINE("timeline", "Timeline"),
        MEMORY("memory", "Memories")
    }

    fun visibleSurfaces(role: ProfileRole?): List<Surface> = when (role) {
        ProfileRole.TEACHER_SELF -> listOf(
            Surface.DASHBOARD,
            Surface.STUDIO,
            Surface.TIMELINE,
            Surface.MEMORY
        )
        ProfileRole.STUDENT_SELF -> listOf(
            Surface.DASHBOARD,
            Surface.STUDIO,
            Surface.TIMELINE,
            Surface.MEMORY
        )
        ProfileRole.STEWARD_FOR_STUDENT -> listOf(
            Surface.DASHBOARD,
            Surface.STUDIO,
            Surface.TIMELINE,
            Surface.MEMORY
        )
        null -> emptyList()
    }

    fun canManageLinkedStudents(role: ProfileRole?): Boolean =
        false
}
