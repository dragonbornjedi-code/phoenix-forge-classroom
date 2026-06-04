package com.phoenixforge.student.ui.npc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NpcRoomScreen(viewModel: NpcRoomViewModel = hiltViewModel()) {
    val npcs by viewModel.npcs.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("NPC Room", style = MaterialTheme.typography.headlineLarge)
            Text("Companions, whisps, and pets react to your journey.", style = MaterialTheme.typography.bodyMedium)
        }

        items(npcs) { npc ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("${npc.type.name}: ${npc.name}", style = MaterialTheme.typography.titleMedium)
                    Text(
                        buildString {
                            if (npc.isUnlocked) append("Stage ${npc.evolutionStage} · mood: ${npc.mood} · ")
                            append("Traits: ${npc.personalityTraits.joinToString()} · ${npc.memoryGraph.size} memories")
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(npc.lastReaction ?: "…", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
