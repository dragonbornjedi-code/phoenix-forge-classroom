package com.phoenixforge.student.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.phoenixforge.student.sync.DigitalHomeWire
import com.phoenixforge.student.ui.digitalhome.DigitalHomeRoomScreen
import com.phoenixforge.student.ui.digitalhome.DigitalHomeScreen
import com.phoenixforge.student.ui.gallery.GalleryScreen
import com.phoenixforge.student.ui.garden.GardenMerchantScreen
import com.phoenixforge.student.ui.npc.CompanionsHubScreen
import com.phoenixforge.student.ui.quests.QuestsScreen
import com.phoenixforge.student.ui.vault.MemoryVaultScreen

/**
 * Flat digital-home destinations (no nested [navigation] graph).
 * Full route strings like `digital_home/hub` are single composable routes on the root NavHost.
 */
fun NavGraphBuilder.digitalHomeNavGraph(navController: NavHostController) {
    composable(StudentRoutes.DIGITAL_HOME_HUB) {
        DigitalHomeScreen(onNavigate = navController::navigate)
    }
    composable(
        route = StudentRoutes.DIGITAL_HOME_ROOM,
        arguments = listOf(navArgument("roomId") { type = NavType.StringType }),
    ) { entry ->
        DigitalHomeRoomScreen(
            roomId = entry.arguments?.getString("roomId").orEmpty(),
            onBack = { navController.popBackStack() },
        )
    }
    composable(StudentRoutes.DIGITAL_HOME_GARDEN) {
        GardenMerchantScreen(onBack = { navController.popBackStack() })
    }
    composable(StudentRoutes.DIGITAL_HOME_GALLERY) {
        GalleryScreen(onBack = { navController.popBackStack() })
    }
    composable(StudentRoutes.DIGITAL_HOME_VAULT) {
        MemoryVaultScreen(onBack = { navController.popBackStack() })
    }
    composable(StudentRoutes.DIGITAL_HOME_QUEST_BOARD) {
        QuestsScreen(onBack = { navController.popBackStack() })
    }
    composable(StudentRoutes.DIGITAL_HOME_COMPANIONS) {
        CompanionsHubScreen(onBack = { navController.popBackStack() })
    }
    composable(StudentRoutes.DIGITAL_HOME_PET_SPACE) {
        DigitalHomeRoomScreen(
            roomId = DigitalHomeWire.Rooms.PET_SPACE,
            onBack = { navController.popBackStack() },
        )
    }
}
