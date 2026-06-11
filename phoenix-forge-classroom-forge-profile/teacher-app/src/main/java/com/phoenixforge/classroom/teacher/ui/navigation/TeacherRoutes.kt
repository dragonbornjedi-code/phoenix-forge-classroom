package com.phoenixforge.classroom.teacher.ui.navigation

object TeacherRoutes {
    const val EXPEDITION = "expedition_board"
    const val CURRICULUM = "curriculum_home"
    const val STUDENT = "student_snapshot"
    const val SAGE = "sage_advisor"
    const val PROFILE = "forge_profile"
    const val CURRICULUM_DOMAIN = "curriculum_domain/{domainId}"
    const val STARTER_LESSON = "starter_lesson/{lessonId}"
    const val WEEKLY_AUDIT = "weekly_audit"
    const val TILE = "tile_detail/{tileId}"
    const val CURRICULUM_SUBDOMAIN = "curriculum_subdomain/{domainId}/{subdomainId}"
    const val LESSON_PLANNER = "lesson_planner"

    val topLevel = setOf(EXPEDITION, CURRICULUM, STUDENT, SAGE)

    fun curriculumDomain(domainId: String) = "curriculum_domain/$domainId"
    fun starterLesson(lessonId: String) = "starter_lesson/$lessonId"
    fun curriculumSubdomain(domainId: String, subdomainId: String) =
        "curriculum_subdomain/$domainId/$subdomainId"
    fun tile(id: String) = "tile_detail/$id"
}
