package com.phoenixforge.student.ui.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.student.domain.model.PhotoTag

@Composable
fun GalleryScreen(viewModel: GalleryViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Photo Gallery", style = MaterialTheme.typography.headlineLarge)
            Text("Import device photos into your Memory Vault.", style = MaterialTheme.typography.bodyMedium)
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(PhotoTag.entries) { tag ->
                    FilterChip(
                        selected = state.selectedTag == tag,
                        onClick = { viewModel.selectTag(tag) },
                        label = { Text(tag.displayName) }
                    )
                }
            }
        }

        state.message?.let { message ->
            item { Text(message, color = MaterialTheme.colorScheme.primary) }
        }

        if (state.isLoading) {
            item { Text("Loading photos…") }
        } else if (state.photos.isEmpty()) {
            item { Text("No photos found. Grant gallery permission in Settings.") }
        }

        items(state.photos) { photo ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(photo.displayName ?: "Photo ${photo.id}", style = MaterialTheme.typography.titleSmall)
                    Text(photo.uri, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                    Button(onClick = { viewModel.importPhoto(photo) }) {
                        Text("Import as ${state.selectedTag.displayName}")
                    }
                }
            }
        }
    }
}
