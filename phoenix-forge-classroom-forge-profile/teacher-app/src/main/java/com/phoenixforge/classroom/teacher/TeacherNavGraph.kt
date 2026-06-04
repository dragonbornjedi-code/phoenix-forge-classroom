package com.phoenixforge.classroom.teacher

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.phoenixforge.classroom.teacher.ui.expedition.ExpeditionBoardScreen
import com.phoenixforge.classroom.teacher.ui.profile.ForgeProfileViewerScreen
import com.phoenixforge.classroom.teacher.ui.tile.TileDetailScreen

private object Routes {
    const val BOARD = "expedition_board"
    const val PROFILE = "forge_profile"
    const val TILE = "tile_detail/{tileId}"
    fun tile(id: String) = "tile_detail/$id"
}

@Composable
fun TeacherNavGraph() {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.BOARD) {
        composable(Routes.BOARD) {
            ExpeditionBoardScreen(
                onViewProfile = { nav.navigate(Routes.PROFILE) },
                onTileClick = { id -> nav.navigate(Routes.tile(id)) }
            )
        }
        composable(Routes.PROFILE) {
            ForgeProfileViewerScreen(onBack = { nav.popBackStack() })
        }
        composable(
            route = Routes.TILE,
            arguments = listOf(navArgument("tileId") { type = NavType.StringType })
        ) {
            TileDetailScreen(onBack = { nav.popBackStack() })
        }
    }
}
