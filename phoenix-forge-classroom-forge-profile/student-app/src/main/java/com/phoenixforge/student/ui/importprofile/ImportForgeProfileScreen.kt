package com.phoenixforge.student.ui.importprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
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
fun ImportForgeProfileScreen(viewModel: ImportForgeProfileViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val pastImports by viewModel.pastImports.collectAsState()
    val preview = state.preview

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Import Forge Profile", style = MaterialTheme.typography.headlineLarge)
            Text(
                "Optional one-time copy. Student Edition works without Forge Profile.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    if (preview == null) {
                        Text("Checking for Forge Profile…")
                    } else if (!preview.isAvailable) {
                        Text(preview.errorMessage ?: "Forge Profile unavailable", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        Text("Found: ${preview.forgeName}", style = MaterialTheme.typography.titleMedium)
                        Text("Stage: ${preview.currentStage ?: "—"}", style = MaterialTheme.typography.bodySmall)
                        Text("Timeline events: ${preview.timelineEventCount}", style = MaterialTheme.typography.bodySmall)
                        Text("Avatar: ${preview.avatarSummary ?: "—"}", style = MaterialTheme.typography.bodySmall)
                        Button(
                            onClick = { viewModel.importSelectedProfile() },
                            enabled = !state.isImporting,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(if (state.isImporting) "Importing…" else "Import Snapshot")
                        }
                    }
                    Button(onClick = { viewModel.refreshPreview() }, modifier = Modifier.padding(top = 8.dp)) {
                        Text("Refresh")
                    }
                }
            }
        }

        state.lastReward?.let { reward ->
            item { Text(reward.story.narrative, color = MaterialTheme.colorScheme.primary) }
        }

        if (pastImports.isNotEmpty()) {
            item { Text("Past Imports", style = MaterialTheme.typography.titleMedium) }
            items(pastImports) { snapshot ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(snapshot.forgeName, style = MaterialTheme.typography.titleSmall)
                        Text("Imported ${snapshot.importedAtEpochMillis}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
