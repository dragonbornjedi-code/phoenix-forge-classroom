package com.phoenixforge.profile.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.profile.ui.navigation.Screen
import com.phoenixforge.profile.ui.interop.ExternalApps

@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Forge Identity", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.size(80.dp), contentAlignment = Alignment.Center) {
                    Text("👤", style = MaterialTheme.typography.displayMedium)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        state.profile?.forgeName ?: "Unknown Forger",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        formatProfileSubtitle(state.profile),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val stats = listOf(
            StatItem("Timeline", state.timelineEventCount.toString(), Screen.Timeline.route),
            StatItem("Memories", state.memoryCount.toString(), Screen.Memory.route),
            StatItem("Avatars", state.avatarCount.toString(), Screen.Studio.route),
            StatItem("Identity", state.profile?.forgeName ?: "—", Screen.Identity.route)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(stats) { stat ->
                StatCard(stat, onClick = { onNavigate(stat.route) })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (state.profile?.profileRole == "student_self" || state.profile?.profileRole == "steward_for_student") {
            state.profile?.uid?.let { uid ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Profile ID", style = MaterialTheme.typography.labelMedium)
                        Text(uid, style = MaterialTheme.typography.bodySmall)
                        Text(
                            "Share this ID with your teacher to link profiles.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onNavigate(Screen.Dreams.route) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dream Board")
        }

        if (state.profile?.profileRole == "teacher_self") {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { ExternalApps.launchTeacherEdition(context) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Open Teacher Edition")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Recent Activity",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigate(Screen.Timeline.route) }
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Text("📊")
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    if (state.timelineEventCount > 0) {
                        "${state.timelineEventCount} childhood moments recorded — tap to open timeline"
                    } else {
                        "Tap to open timeline — moments appear as you use Forge Profile"
                    }
                )
            }
        }
    }
}

private fun formatProfileSubtitle(profile: com.phoenixforge.profile.domain.model.ForgeProfile?): String {
    if (profile == null) return "Just getting started"
    val age = profile.ageYears?.let { "Age $it" }
    val role = when (profile.profileRole) {
        "steward_for_student" -> "Profile for a student"
        "student_self" -> "Student profile"
        "teacher_self" -> "Teacher profile"
        else -> null
    }
    return listOfNotNull(age, role, formatStageLabel(profile.currentStage))
        .joinToString(" · ")
}

private fun formatStageLabel(stage: String?): String = when (stage) {
    null, "" -> "Just getting started"
    "JUST_BEGINNING" -> "Just getting started"
    else -> stage.replace('_', ' ').lowercase().replaceFirstChar { it.titlecase() }
}

data class StatItem(val label: String, val value: String, val route: String)

@Composable
fun StatCard(stat: StatItem, onClick: () -> Unit) {
    Card(modifier = Modifier.clickable(onClick = onClick)) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stat.value, style = MaterialTheme.typography.headlineMedium)
            Text(stat.label, style = MaterialTheme.typography.labelSmall)
        }
    }
}
