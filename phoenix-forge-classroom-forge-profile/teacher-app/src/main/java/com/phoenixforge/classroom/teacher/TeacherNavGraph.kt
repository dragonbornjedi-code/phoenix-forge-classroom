package com.phoenixforge.classroom.teacher

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.phoenixforge.classroom.teacher.ui.curriculum.CurriculumDomainScreen
import com.phoenixforge.classroom.teacher.ui.curriculum.CurriculumHomeScreen
import com.phoenixforge.classroom.teacher.ui.curriculum.CurriculumSubdomainQuestsScreen
import com.phoenixforge.classroom.teacher.ui.curriculum.StarterLessonDetailScreen
import com.phoenixforge.classroom.teacher.ui.curriculum.WeeklyAuditScreen
import com.phoenixforge.classroom.teacher.ui.expedition.ExpeditionBoardScreen
import com.phoenixforge.classroom.teacher.ui.lesson.LessonPlannerScreen
import com.phoenixforge.classroom.teacher.ui.navigation.TeacherRoutes
import com.phoenixforge.classroom.teacher.ui.profile.ForgeProfileViewerScreen
import com.phoenixforge.classroom.teacher.ui.sage.SageAdvisorScreen
import com.phoenixforge.classroom.teacher.ui.students.StudentSnapshotScreen
import com.phoenixforge.classroom.teacher.ui.tile.TileDetailScreen

@Composable
fun TeacherNavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = TeacherRoutes.EXPEDITION,
        modifier = modifier,
    ) {
        composable(TeacherRoutes.EXPEDITION) {
            ExpeditionBoardScreen(
                onBack = null,
                onViewProfile = { navController.navigate(TeacherRoutes.PROFILE) },
                onViewStudentSnapshot = { navController.navigate(TeacherRoutes.STUDENT) },
                onOpenCurriculum = { navController.navigate(TeacherRoutes.CURRICULUM) },
                onTileClick = { id -> navController.navigate(TeacherRoutes.tile(id)) },
            )
        }
        composable(TeacherRoutes.CURRICULUM) {
            CurriculumHomeScreen(
                onOpenExpedition = { navController.navigate(TeacherRoutes.EXPEDITION) },
                onViewProfile = { navController.navigate(TeacherRoutes.PROFILE) },
                onViewStudentSnapshot = { navController.navigate(TeacherRoutes.STUDENT) },
                onOpenDomain = { domainId -> navController.navigate(TeacherRoutes.curriculumDomain(domainId.name)) },
                onOpenLesson = { lessonId -> navController.navigate(TeacherRoutes.starterLesson(lessonId)) },
                onOpenWeeklyAudit = { navController.navigate(TeacherRoutes.WEEKLY_AUDIT) },
                onOpenLessonPlanner = { navController.navigate(TeacherRoutes.LESSON_PLANNER) },
                onOpenSageAdvisor = { navController.navigate(TeacherRoutes.SAGE) },
            )
        }
        composable(TeacherRoutes.STUDENT) {
            StudentSnapshotScreen(onBack = null)
        }
        composable(TeacherRoutes.SAGE) {
            SageAdvisorScreen(
                onBack = null,
                onOpenExpedition = {
                    navController.navigate(TeacherRoutes.EXPEDITION) {
                        popUpTo(TeacherRoutes.EXPEDITION) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onOpenTile = { tileId ->
                    navController.navigate(TeacherRoutes.tile(tileId)) {
                        popUpTo(TeacherRoutes.EXPEDITION) { inclusive = false }
                    }
                },
            )
        }
        composable(TeacherRoutes.PROFILE) {
            ForgeProfileViewerScreen(onBack = { navController.popBackStack() })
        }
        composable(
            route = TeacherRoutes.CURRICULUM_DOMAIN,
            arguments = listOf(navArgument("domainId") { type = NavType.StringType }),
        ) {
            CurriculumDomainScreen(
                onBack = { navController.popBackStack() },
                onOpenLesson = { lessonId -> navController.navigate(TeacherRoutes.starterLesson(lessonId)) },
                onOpenSubdomain = { subdomainId ->
                    navController.navigate(
                        TeacherRoutes.curriculumSubdomain(
                            domainId = it.arguments?.getString("domainId").orEmpty(),
                            subdomainId = subdomainId,
                        ),
                    )
                },
            )
        }
        composable(
            route = TeacherRoutes.CURRICULUM_SUBDOMAIN,
            arguments = listOf(
                navArgument("domainId") { type = NavType.StringType },
                navArgument("subdomainId") { type = NavType.StringType },
            ),
        ) {
            CurriculumSubdomainQuestsScreen(
                onBack = { navController.popBackStack() },
                onOpenLesson = { lessonId -> navController.navigate(TeacherRoutes.starterLesson(lessonId)) },
            )
        }
        composable(
            route = TeacherRoutes.STARTER_LESSON,
            arguments = listOf(navArgument("lessonId") { type = NavType.StringType }),
        ) {
            StarterLessonDetailScreen(onBack = { navController.popBackStack() })
        }
        composable(TeacherRoutes.WEEKLY_AUDIT) {
            WeeklyAuditScreen(onBack = { navController.popBackStack() })
        }
        composable(TeacherRoutes.LESSON_PLANNER) {
            LessonPlannerScreen(
                onBack = { navController.popBackStack() },
                onTileCreated = { tileId ->
                    navController.navigate(TeacherRoutes.tile(tileId)) {
                        popUpTo(TeacherRoutes.CURRICULUM) { saveState = true }
                    }
                },
            )
        }
        composable(
            route = TeacherRoutes.TILE,
            arguments = listOf(navArgument("tileId") { type = NavType.StringType }),
        ) {
            TileDetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
