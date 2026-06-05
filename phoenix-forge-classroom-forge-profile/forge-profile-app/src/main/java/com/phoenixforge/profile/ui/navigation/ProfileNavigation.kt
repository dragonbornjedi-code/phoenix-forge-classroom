package com.phoenixforge.profile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.phoenixforge.profile.ui.dashboard.DashboardScreen
import com.phoenixforge.profile.ui.dreams.DreamBoardScreen
import com.phoenixforge.profile.ui.identity.IdentityCardScreen
import com.phoenixforge.profile.ui.memory.MemoryCapsuleScreen
import com.phoenixforge.profile.ui.studio.AvatarStudioScreen
import com.phoenixforge.profile.ui.teacher.TeacherGateScreen
import com.phoenixforge.profile.ui.timeline.ChildhoodTimelineScreen

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Studio : Screen("studio")
    object Identity : Screen("identity")
    object Timeline : Screen("timeline")
    object Memory : Screen("memory")
    object Dreams : Screen("dreams")
    object TeacherGate : Screen("teacher_gate")
}

@Composable
fun ProfileNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onSignOut: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(onNavigate = { route -> navController.navigate(route) })
        }
        composable(Screen.Studio.route) {
            AvatarStudioScreen()
        }
        composable(Screen.Identity.route) {
            IdentityCardScreen()
        }
        composable(Screen.Timeline.route) {
            ChildhoodTimelineScreen()
        }
        composable(Screen.Memory.route) {
            MemoryCapsuleScreen()
        }
        composable(Screen.Dreams.route) {
            DreamBoardScreen()
        }
        composable(Screen.TeacherGate.route) {
            TeacherGateScreen(onSignOut = onSignOut)
        }
    }
}
