package com.phoenixforge.student.ui.navigation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

data class BottomNavItem(
    val route: String,
    val label: String,
    val emoji: String,
)

private val scrollNavItems = listOf(
    BottomNavItem(StudentRoutes.HOME, "Home", "🏠"),
    BottomNavItem(StudentRoutes.DIGITAL_HOME, "House", "🏡"),
    BottomNavItem(StudentRoutes.QUESTS, "Adventures", "⭐"),
    BottomNavItem(StudentRoutes.DREAMS, "Dreams", "🌙"),
    BottomNavItem(StudentRoutes.STORY_ARCHIVE, "Stories", "📖"),
)

@Composable
fun StudentBottomBar(navController: NavController) {
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route
    val scrollState = rememberScrollState()

    Surface(
        tonalElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .horizontalScroll(scrollState)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            scrollNavItems.forEach { item ->
                val selected = when (item.route) {
                    StudentRoutes.DIGITAL_HOME -> StudentRoutes.isDigitalHomeRoute(currentRoute)
                    else -> currentRoute == item.route
                }
                FilterChip(
                    selected = selected,
                    onClick = {
                        val target = when (item.route) {
                            StudentRoutes.DIGITAL_HOME -> StudentRoutes.DIGITAL_HOME_HUB
                            else -> item.route
                        }
                        navController.navigate(target) {
                            popUpTo(StudentRoutes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    label = {
                        Text(
                            "${item.emoji} ${item.label}",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            maxLines = 1,
                        )
                    },
                    modifier = Modifier.heightIn(min = 48.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
            }
        }
    }
}
