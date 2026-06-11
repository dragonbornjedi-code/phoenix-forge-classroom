package com.phoenixforge.student.ui.npc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.student.domain.model.NpcState
import com.phoenixforge.student.domain.model.NpcType
import com.phoenixforge.student.ui.components.StudentBackHeader

@Composable
fun CompanionsHubScreen(
    onBack: (() -> Unit)? = null,
    viewModel: NpcRoomViewModel = hiltViewModel(),
) {
    val state by viewModel.hubState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        onBack?.let { back ->
            item { StudentBackHeader(onBack = back) }
        }
        item {
            Text("Companions", style = MaterialTheme.typography.headlineLarge)
            Text(
                "Your companion walks with you. Whisps wake when you explore. Pets live in Pet Space.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            SectionLabel("★ Companion")
            state.companion?.let { CompanionHeroCard(it, state.sparkPhaseLabel) }
                ?: Text("No companion yet — explore the house to meet Spark.", style = MaterialTheme.typography.bodyMedium)
        }

        item {
            SectionLabel("✧ Whisps")
            if (state.whisps.isEmpty()) {
                Text("Whisps appear after photos, quests, and surprises.", style = MaterialTheme.typography.bodySmall)
            } else {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.whisps, key = { it.id }) { whisp ->
                        WhispCard(whisp)
                    }
                }
            }
        }

        item {
            SectionLabel("🐾 Pet Space")
            if (!state.petSpaceUnlocked) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Pet Space unlocks at Level 4", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "Complete quests and grow your level to invite pets.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text("Current level: ${state.level}", style = MaterialTheme.typography.labelMedium)
                    }
                }
            } else if (state.pets.isEmpty()) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Pet Space is open!", style = MaterialTheme.typography.titleSmall)
                        Text(
                            "Finish special quests to invite a pet.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.pets.forEach { pet -> PetCard(pet) }
                }
            }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun CompanionHeroCard(companion: NpcState, phaseLabel: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(companion.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(
                "Stage ${companion.evolutionStage} · mood: ${companion.mood}",
                style = MaterialTheme.typography.labelLarge
            )
            Text(phaseLabel, style = MaterialTheme.typography.labelMedium)
            companion.lastReaction?.let {
                Text("\"$it\"", style = MaterialTheme.typography.bodyLarge)
            }
            if (companion.personalityTraits.isNotEmpty()) {
                Text(
                    "Traits: ${companion.personalityTraits.joinToString()}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun WhispCard(whisp: NpcState) {
    Card(modifier = Modifier.fillMaxWidth(0.72f)) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                if (whisp.isUnlocked) whisp.name else "???",
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                if (whisp.isUnlocked) "Stage ${whisp.evolutionStage} · ${whisp.mood}" else "Sleeping — explore to wake",
                style = MaterialTheme.typography.labelSmall
            )
            if (whisp.isUnlocked) {
                whisp.lastReaction?.let { Text(it, style = MaterialTheme.typography.bodySmall, maxLines = 3) }
            }
        }
    }
}

@Composable
private fun PetCard(pet: NpcState) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("${pet.name} · Stage ${pet.evolutionStage}", style = MaterialTheme.typography.titleSmall)
            Text("Mood: ${pet.mood}", style = MaterialTheme.typography.labelSmall)
            pet.lastReaction?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
        }
    }
}
