package com.phoenixforge.student.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.student.domain.avatar.ImportedHeroLookParser
import com.phoenixforge.student.ui.avatar.HearthWelcomeCard
import com.phoenixforge.student.ui.navigation.StudentRoutes

@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.worldState.collectAsState()

    val heroLook = ImportedHeroLookParser.parse(state.importedHeroSummary)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            state.importedForgeName?.let { forgeName ->
                HearthWelcomeCard(
                    forgeName = forgeName,
                    heroLook = heroLook,
                )
            } ?: run {
                Text("Your Hearth", style = MaterialTheme.typography.headlineLarge)
                Text(
                    "Welcome, explorer. Import your Forge Profile when your steward is ready.",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Level ${state.progress.level} · ${state.progress.xp} XP · ${state.progress.streakDays}-day streak",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "World: ${state.world.environmentTheme.replace('_', ' ')}",
                style = MaterialTheme.typography.labelMedium
            )
        }

        item {
            WorldDriftPanel(drift = state.world.drift)
        }

        state.latestStory?.let { story ->
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Life Story", style = MaterialTheme.typography.titleMedium)
                        story.npcSpeaker?.let {
                            Text("— $it", style = MaterialTheme.typography.labelSmall)
                        }
                        story.callbackLine?.let {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(story.narrative, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        item {
            Text("Rooms in your hearth", style = MaterialTheme.typography.titleMedium)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(state.house.rooms) { room ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (room.isUnlocked) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.surfaceVariant
                            }
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(room.type.displayName, style = MaterialTheme.typography.labelLarge)
                            Text(
                                if (room.isUnlocked) "Unlocked" else "Lv ${room.type.unlockLevel}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Companion", style = MaterialTheme.typography.titleMedium)
                    val companion = state.activeCompanion
                    Text(companion?.name ?: "—", style = MaterialTheme.typography.headlineSmall)
                    Text(companion?.lastReaction ?: "Explore to meet your companion.", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { onNavigate(StudentRoutes.NPC) }) { Text("Visit Companions") }
                }
            }
        }

        item {
            Text("Active Quests", style = MaterialTheme.typography.titleMedium)
        }
        items(state.activeQuests) { quest ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(quest.title, style = MaterialTheme.typography.titleSmall)
                    Text(quest.description, style = MaterialTheme.typography.bodySmall)
                    Text("+${quest.xpReward} XP", style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        item {
            RowActions(onNavigate)
        }
    }
}

@Composable
private fun RowActions(onNavigate: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = { onNavigate(StudentRoutes.GALLERY) }, modifier = Modifier.fillMaxWidth()) {
            Text("Open Gallery")
        }
        Button(onClick = { onNavigate(StudentRoutes.VAULT) }, modifier = Modifier.fillMaxWidth()) {
            Text("Memory Vault")
        }
        Button(onClick = { onNavigate(StudentRoutes.QUESTS) }, modifier = Modifier.fillMaxWidth()) {
            Text("Quest Board")
        }
        Button(onClick = { onNavigate(StudentRoutes.STORY_ARCHIVE) }, modifier = Modifier.fillMaxWidth()) {
            Text("Story Archive")
        }
    }
}
