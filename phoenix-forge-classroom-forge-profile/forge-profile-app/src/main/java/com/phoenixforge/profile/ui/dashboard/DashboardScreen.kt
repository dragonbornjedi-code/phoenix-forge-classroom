package com.phoenixforge.profile.ui.dashboard

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.profile.domain.avatar.AvatarHeroCatalog
import com.phoenixforge.profile.domain.copy.AppBoundaryCopy
import com.phoenixforge.profile.domain.model.ProfileRole
import com.phoenixforge.profile.ui.components.ForgeNavTile
import com.phoenixforge.profile.ui.components.ForgePrimaryButton
import com.phoenixforge.profile.ui.components.ForgeSecondaryButton
import com.phoenixforge.profile.ui.interop.ExternalApps
import com.phoenixforge.profile.ui.navigation.Screen
import com.phoenixforge.profile.ui.studio.AvatarPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit = {},
    onSignOut: () -> Unit = {},
    onSwitchProfile: () -> Unit = {},
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val role = ProfileRole.fromStorageKey(state.profile?.profileRole)

    val stats = listOf(
        StatItem("Timeline", state.timelineEventCount.toString(), Screen.Timeline.route),
        StatItem("Memories", state.memoryCount.toString(), Screen.Memory.route),
        StatItem("Avatars", state.avatarCount.toString(), Screen.Studio.route),
        StatItem("Identity", state.profile?.forgeName ?: "—", Screen.Identity.route)
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                if (role == ProfileRole.STUDENT_SELF) "Your Forge" else "Forge Identity",
                style = MaterialTheme.typography.headlineLarge,
            )
            if (role != ProfileRole.STUDENT_SELF) {
                Text(
                    AppBoundaryCopy.dashboardBoundaryLine(role),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AvatarPreview(
                        avatar = state.latestAvatar,
                        modifier = Modifier.size(80.dp),
                        size = 80.dp,
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            state.profile?.forgeName ?: "Unknown Forger",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(formatProfileSubtitle(state.profile), style = MaterialTheme.typography.labelMedium)
                        state.latestAvatar?.let { avatar ->
                            Text(
                                "${AvatarHeroCatalog.displayStyle(avatar.hairType)} · ${AvatarHeroCatalog.displayColor(avatar.eyeColor)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }

        if (AppBoundaryCopy.canOpenTeacherEdition(role)) {
            item {
                ForgePrimaryButton(
                    text = AppBoundaryCopy.OPEN_TEACHER_EDITION,
                    onClick = {
                        when (val result = ExternalApps.launchTeacherEdition(context)) {
                            ExternalApps.LaunchResult.Launched -> Unit
                            is ExternalApps.LaunchResult.NotInstalled ->
                                Toast.makeText(
                                    context,
                                    "Teacher Edition is not installed on this device.",
                                    Toast.LENGTH_LONG,
                                ).show()
                            is ExternalApps.LaunchResult.Failed ->
                                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                        }
                    },
                )
                Text(
                    AppBoundaryCopy.adultTeacherHint(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }

        if (AppBoundaryCopy.canPushPlaySnapshot(role)) {
            item {
                Text(
                    AppBoundaryCopy.childDashboardTagline(state.profile?.forgeName),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            item {
                ForgePrimaryButton(
                    text = AppBoundaryCopy.PUSH_CHILD_SNAPSHOT,
                    onClick = {
                        viewModel.pushToTablet(context) { message ->
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        }
                    },
                )
                Text(
                    AppBoundaryCopy.pushAvatarHint(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            item {
                ForgeSecondaryButton(
                    text = AppBoundaryCopy.OPEN_STUDENT_EDITION,
                    onClick = {
                        when (val result = ExternalApps.launchStudentEdition(context)) {
                            ExternalApps.LaunchResult.Launched -> Unit
                            is ExternalApps.LaunchResult.NotInstalled ->
                                Toast.makeText(
                                    context,
                                    "Student Edition is not installed on this device.",
                                    Toast.LENGTH_LONG,
                                ).show()
                            is ExternalApps.LaunchResult.Failed ->
                                Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                        }
                    },
                )
            }
        }

        item {
            Text("Your record", style = MaterialTheme.typography.titleMedium)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                userScrollEnabled = false,
            ) {
                items(stats) { stat ->
                    ForgeNavTile(
                        label = stat.label,
                        value = stat.value,
                        onClick = { onNavigate(stat.route) },
                    )
                }
            }
        }

        item {
            Text("Session", style = MaterialTheme.typography.titleMedium)
            ForgeSecondaryButton(text = "Switch profile", onClick = onSwitchProfile)
            Spacer(modifier = Modifier.height(8.dp))
            ForgePrimaryButton(
                text = "Sign out",
                onClick = onSignOut,
            )
            Text(
                "Sign out returns to the profile picker. Clearing app cache does not sign you out — use this button.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp),
            )
        }

        item {
            Text("Recent Activity", style = MaterialTheme.typography.titleMedium)
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onNavigate(Screen.Timeline.route) },
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
}

private fun formatProfileSubtitle(profile: com.phoenixforge.profile.domain.model.ForgeProfile?): String {
    if (profile == null) return "Just getting started"
    val age = profile.ageYears?.let { "Age $it" }
    val role = when (profile.profileRole) {
        "steward_for_student" -> "Adult profile"
        "student_self" -> "Child profile"
        "teacher_self" -> "Teacher profile (legacy)"
        else -> null
    }
    return listOfNotNull(age, role, formatStageLabel(profile.currentStage)).joinToString(" · ")
}

private fun formatStageLabel(stage: String?): String = when (stage) {
    null, "" -> "Just getting started"
    "JUST_BEGINNING" -> "Just getting started"
    else -> stage.replace('_', ' ').lowercase().replaceFirstChar { it.titlecase() }
}

data class StatItem(val label: String, val value: String, val route: String)
