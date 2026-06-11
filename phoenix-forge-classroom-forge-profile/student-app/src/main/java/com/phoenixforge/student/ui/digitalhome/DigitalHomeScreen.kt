package com.phoenixforge.student.ui.digitalhome

import com.phoenixforge.student.ui.components.kidBounceClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.student.ui.components.StudentHearthBackground
import com.phoenixforge.student.ui.navigation.DigitalHomeNavigation
import com.phoenixforge.student.ui.navigation.StudentRoutes
import com.phoenixforge.student.ui.theme.StudentKidCopy
import com.phoenixforge.student.ui.theme.StudentRoomVisuals

@Composable
fun DigitalHomeScreen(
    onNavigate: (String) -> Unit,
    viewModel: DigitalHomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    StudentHearthBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Text("🏡", fontSize = 48.sp)
                Text(StudentKidCopy.digitalHomeTitle(), style = MaterialTheme.typography.headlineLarge)
                Text(
                    StudentKidCopy.digitalHomeSubtitle(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    StudentKidCopy.digitalHomeProgress(state.level, state.xp, state.tokens),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }

            item {
                Text(
                    StudentKidCopy.zoneSectionTitle(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            items(state.zones, key = { it.room.name }) { zone ->
                ZoneCard(
                    zone = zone,
                    onClick = {
                        DigitalHomeNavigation.routeForRoom(zone.room, zone.isUnlocked)?.let(onNavigate)
                    },
                )
            }

            item {
                Text(
                    StudentKidCopy.friendsSectionTitle(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("✨", fontSize = 36.sp, modifier = Modifier.padding(end = 10.dp))
                                Column {
                                    Text("Spark", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    Text(
                                        if (state.companionUnlocked) state.companionName ?: "Your buddy" else "Almost here…",
                                        style = MaterialTheme.typography.bodyLarge,
                                    )
                                }
                            }
                            TextButton(onClick = { onNavigate(StudentRoutes.DIGITAL_HOME_COMPANIONS) }) {
                                Text(StudentKidCopy.companionVisit())
                            }
                        }
                        Text(
                            StudentKidCopy.whispsLine(state.whispsUnlocked, state.whispsTotal),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Text(
                            if (state.petSpaceUnlocked) StudentKidCopy.petSpaceOpen() else StudentKidCopy.petSpaceLocked(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ZoneCard(zone: DigitalZoneCard, onClick: () -> Unit) {
    val containerColor = if (zone.isUnlocked) {
        StudentRoomVisuals.accent(zone.room)
    } else {
        StudentRoomVisuals.lockedAccent()
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (zone.isUnlocked) 1f else 0.88f)
            .then(
                if (zone.isUnlocked) Modifier.kidBounceClickable(onClick = onClick)
                else Modifier,
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (zone.isUnlocked) 5.dp else 2.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .padding(end = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(StudentRoomVisuals.emoji(zone.room), fontSize = 48.sp)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    StudentKidCopy.kidRoomName(zone.room),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    if (zone.isUnlocked) StudentKidCopy.roomOpen() else StudentKidCopy.roomAlmostReady(),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
            Text(
                if (zone.isUnlocked) "→" else "✨",
                fontSize = 32.sp,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
    }
}
