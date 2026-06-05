package com.phoenixforge.profile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.phoenixforge.profile.domain.access.ProfileAccessPolicy
import com.phoenixforge.profile.ui.session.ActiveProfileViewModel

private data class BottomNavItem(val route: String, val label: String, val icon: ImageVector)

private fun iconFor(surface: ProfileAccessPolicy.Surface): ImageVector = when (surface) {
    ProfileAccessPolicy.Surface.DASHBOARD -> Icons.Default.Home
    ProfileAccessPolicy.Surface.STUDIO -> Icons.Default.Face
    ProfileAccessPolicy.Surface.TIMELINE -> Icons.Default.Timeline
    ProfileAccessPolicy.Surface.MEMORY -> Icons.Default.PhotoLibrary
}

@Composable
fun ProfileBottomBar(
    navController: NavController,
    activeProfileViewModel: ActiveProfileViewModel = hiltViewModel()
) {
    val visibleSurfaces by activeProfileViewModel.visibleSurfaces.collectAsState()
    val bottomItems = visibleSurfaces.map { surface ->
        BottomNavItem(surface.route, surface.label, iconFor(surface))
    }

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
