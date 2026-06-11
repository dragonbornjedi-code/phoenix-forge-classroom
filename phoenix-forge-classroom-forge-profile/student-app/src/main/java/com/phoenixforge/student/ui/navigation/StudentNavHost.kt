package com.phoenixforge.student.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.phoenixforge.student.ui.dreams.DreamBoardScreen
import com.phoenixforge.student.ui.home.HomeScreen
import com.phoenixforge.student.ui.importprofile.ImportForgeProfileScreen
import com.phoenixforge.student.ui.inbox.InboxScreen
import com.phoenixforge.student.ui.quests.QuestsScreen
import com.phoenixforge.student.ui.settings.SettingsScreen
import com.phoenixforge.student.ui.story.StoryArchiveScreen
import com.phoenixforge.student.sync.QuestRoutineCategory

@Composable
fun StudentNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = StudentRoutes.HOME,
        modifier = modifier,
    ) {
        composable(StudentRoutes.HOME) { HomeScreen(onNavigate = navController::navigate) }
        legacyDigitalHomeRedirect(StudentRoutes.DIGITAL_HOME, StudentRoutes.DIGITAL_HOME_HUB, navController)
        digitalHomeNavGraph(navController)
        composable(StudentRoutes.QUESTS) { QuestsScreen() }
        composable(StudentRoutes.TODAY) {
            QuestsScreen(initialCategory = QuestRoutineCategory.DAILY)
        }
        composable(StudentRoutes.IMPORT) { ImportForgeProfileScreen() }
        composable(StudentRoutes.SETTINGS) { SettingsScreen(onNavigate = navController::navigate) }
        composable(StudentRoutes.STORY_ARCHIVE) { StoryArchiveScreen() }
        composable(StudentRoutes.DREAMS) { DreamBoardScreen() }
        composable(StudentRoutes.INBOX) { InboxScreen() }

        legacyDigitalHomeRedirect(StudentRoutes.GARDEN_MERCHANT, StudentRoutes.DIGITAL_HOME_GARDEN, navController)
        legacyDigitalHomeRedirect(StudentRoutes.GALLERY, StudentRoutes.DIGITAL_HOME_GALLERY, navController)
        legacyDigitalHomeRedirect(StudentRoutes.VAULT, StudentRoutes.DIGITAL_HOME_VAULT, navController)
        legacyDigitalHomeRedirect(StudentRoutes.NPC, StudentRoutes.DIGITAL_HOME_COMPANIONS, navController)
    }
}

private fun NavGraphBuilder.legacyDigitalHomeRedirect(
    legacyRoute: String,
    nestedRoute: String,
    navController: NavHostController,
) {
    composable(legacyRoute) {
        LaunchedEffect(legacyRoute) {
            navController.navigate(nestedRoute) {
                popUpTo(legacyRoute) { inclusive = true }
                launchSingleTop = true
            }
        }
    }
}
