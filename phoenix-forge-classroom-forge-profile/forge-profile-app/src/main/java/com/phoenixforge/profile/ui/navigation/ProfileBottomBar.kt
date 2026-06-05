package com.phoenixforge.profile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

private data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)

private fun bottomNavLabel(screen: Screen): String = when (screen) {
    Screen.Dashboard -> "Home"
    Screen.Studio -> "Studio"
    Screen.Timeline -> "Timeline"
    Screen.Memory -> "Memories"
    Screen.TeacherGate -> "Steward"
    else -> screen.route
}

private fun bottomNavIcon(screen: Screen): ImageVector = when (screen) {
    Screen.Dashboard -> Icons.Default.Home
    Screen.Studio -> Icons.Default.Face
    Screen.Timeline -> Icons.Default.Timeline
    Screen.Memory -> Icons.Default.PhotoLibrary
    Screen.TeacherGate -> Icons.Default.Settings
    else -> Icons.Default.Home
}

private val bottomItems: List<BottomNavItem> = profileBottomNavScreens.map { screen ->
    BottomNavItem(screen.route, bottomNavLabel(screen), bottomNavIcon(screen))
}

@Composable
fun ProfileBottomBar(navController: NavController) {
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    NavigationBar {
        bottomItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
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
