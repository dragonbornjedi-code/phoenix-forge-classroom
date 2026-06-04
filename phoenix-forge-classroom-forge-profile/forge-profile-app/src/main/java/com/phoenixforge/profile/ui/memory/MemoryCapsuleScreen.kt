package com.phoenixforge.profile.ui.memory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.profile.domain.model.MemoryArtifact

@Composable
fun MemoryCapsuleScreen(
    viewModel: MemoryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Memory Capsule", style = MaterialTheme.typography.headlineLarge)
        Text("Your permanent collection of discoveries.", style = MaterialTheme.typography.bodyMedium)
        
        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(state.artifacts) { artifact ->
                ArtifactItem(artifact)
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { /* Capture new artifact */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Capture New Memory")
        }
    }
}

@Composable
fun ArtifactItem(artifact: MemoryArtifact) {
    Card(modifier = Modifier.aspectRatio(1f)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            Text(when(artifact.type.name) {
                "PHOTO" -> "🖼️"
                "AUDIO" -> "🎤"
                "PROJECT" -> "🔨"
                else -> "📦"
            }, style = MaterialTheme.typography.displaySmall)
        }
    }
}
