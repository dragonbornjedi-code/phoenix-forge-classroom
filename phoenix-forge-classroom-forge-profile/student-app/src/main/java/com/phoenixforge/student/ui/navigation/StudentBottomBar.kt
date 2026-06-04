package com.phoenixforge.student.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)

private val bottomItems = listOf(
    BottomNavItem(StudentRoutes.HOME, "House", Icons.Default.Home),
    BottomNavItem(StudentRoutes.GALLERY, "Gallery", Icons.Default.PhotoLibrary),
    BottomNavItem(StudentRoutes.VAULT, "Vault", Icons.Default.Star),
    BottomNavItem(StudentRoutes.QUESTS, "Quests", Icons.Default.Star),
    BottomNavItem(StudentRoutes.SETTINGS, "More", Icons.Default.Settings)
)

@Composable
fun StudentBottomBar(navController: NavController) {
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    NavigationBar {
        bottomItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(StudentRoutes.HOME) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}
