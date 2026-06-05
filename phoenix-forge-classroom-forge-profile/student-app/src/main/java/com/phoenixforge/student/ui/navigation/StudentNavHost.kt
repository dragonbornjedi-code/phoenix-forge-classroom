package com.phoenixforge.student.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.phoenixforge.student.ui.gallery.GalleryScreen
import com.phoenixforge.student.ui.home.HomeScreen
import com.phoenixforge.student.ui.importprofile.ImportForgeProfileScreen
import com.phoenixforge.student.ui.npc.CompanionsHubScreen
import com.phoenixforge.student.ui.quests.QuestsScreen
import com.phoenixforge.student.ui.settings.SettingsScreen
import com.phoenixforge.student.ui.story.StoryArchiveScreen
import com.phoenixforge.student.ui.vault.MemoryVaultScreen

@Composable
fun StudentNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = StudentRoutes.HOME,
        modifier = modifier
    ) {
        composable(StudentRoutes.HOME) { HomeScreen(onNavigate = navController::navigate) }
        composable(StudentRoutes.GALLERY) { GalleryScreen() }
        composable(StudentRoutes.VAULT) { MemoryVaultScreen() }
        composable(StudentRoutes.QUESTS) { QuestsScreen() }
        composable(StudentRoutes.NPC) { CompanionsHubScreen() }
        composable(StudentRoutes.IMPORT) { ImportForgeProfileScreen() }
        composable(StudentRoutes.SETTINGS) { SettingsScreen(onNavigate = navController::navigate) }
        composable(StudentRoutes.STORY_ARCHIVE) { StoryArchiveScreen() }
    }
}
