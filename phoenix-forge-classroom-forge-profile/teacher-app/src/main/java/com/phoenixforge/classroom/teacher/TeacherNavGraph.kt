package com.phoenixforge.classroom.teacher

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.phoenixforge.classroom.teacher.ui.curriculum.CurriculumDomainScreen
import com.phoenixforge.classroom.teacher.ui.curriculum.CurriculumHomeScreen
import com.phoenixforge.classroom.teacher.ui.curriculum.StarterLessonDetailScreen
import com.phoenixforge.classroom.teacher.ui.curriculum.WeeklyAuditScreen
import com.phoenixforge.classroom.teacher.ui.expedition.ExpeditionBoardScreen
import com.phoenixforge.classroom.teacher.ui.profile.ForgeProfileViewerScreen
import com.phoenixforge.classroom.teacher.ui.tile.TileDetailScreen

private object Routes {
    const val BOARD = "expedition_board"
    const val PROFILE = "forge_profile"
    const val CURRICULUM = "curriculum_home"
    const val CURRICULUM_DOMAIN = "curriculum_domain/{domainId}"
    const val STARTER_LESSON = "starter_lesson/{lessonId}"
    const val WEEKLY_AUDIT = "weekly_audit"
    const val TILE = "tile_detail/{tileId}"

    fun curriculumDomain(domainId: String) = "curriculum_domain/$domainId"
    fun starterLesson(lessonId: String) = "starter_lesson/$lessonId"
    fun tile(id: String) = "tile_detail/$id"
}

@Composable
fun TeacherNavGraph() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.BOARD) {
        composable(Routes.BOARD) {
            ExpeditionBoardScreen(
                onViewProfile = { nav.navigate(Routes.PROFILE) },
                onOpenCurriculum = { nav.navigate(Routes.CURRICULUM) },
                onTileClick = { id -> nav.navigate(Routes.tile(id)) }
            )
        }
        composable(Routes.PROFILE) {
            ForgeProfileViewerScreen(onBack = { nav.popBackStack() })
        }
        composable(Routes.CURRICULUM) {
            CurriculumHomeScreen(
                onBack = { nav.popBackStack() },
                onOpenDomain = { domainId -> nav.navigate(Routes.curriculumDomain(domainId.name)) },
                onOpenLesson = { lessonId -> nav.navigate(Routes.starterLesson(lessonId)) },
                onOpenWeeklyAudit = { nav.navigate(Routes.WEEKLY_AUDIT) }
            )
        }
        composable(
            route = Routes.CURRICULUM_DOMAIN,
            arguments = listOf(navArgument("domainId") { type = NavType.StringType })
        ) {
            CurriculumDomainScreen(
                onBack = { nav.popBackStack() },
                onOpenLesson = { lessonId -> nav.navigate(Routes.starterLesson(lessonId)) }
            )
        }
        composable(
            route = Routes.STARTER_LESSON,
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType })
        ) {
            StarterLessonDetailScreen(onBack = { nav.popBackStack() })
        }
        composable(Routes.WEEKLY_AUDIT) {
            WeeklyAuditScreen(onBack = { nav.popBackStack() })
        }
        composable(
            route = Routes.TILE,
            arguments = listOf(navArgument("tileId") { type = NavType.StringType })
        ) {
            TileDetailScreen(onBack = { nav.popBackStack() })
        }
    }
}
