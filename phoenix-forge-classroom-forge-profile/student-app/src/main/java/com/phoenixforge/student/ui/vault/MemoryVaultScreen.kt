package com.phoenixforge.student.ui.vault

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
import com.phoenixforge.student.ui.components.StudentBackHeader

@Composable
fun MemoryVaultScreen(
    onBack: (() -> Unit)? = null,
    viewModel: MemoryVaultViewModel = hiltViewModel(),
) {
    val chapters by viewModel.chapters.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        onBack?.let { back ->
            item { StudentBackHeader(onBack = back) }
        }
        item {
            Text("Memory Vault", style = MaterialTheme.typography.headlineLarge)
            Text("Life chapters and time capsules", style = MaterialTheme.typography.bodyMedium)
        }

        items(chapters) { chapter ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(chapter.chapter.displayName, style = MaterialTheme.typography.titleMedium)
                    Text(
                        "${chapter.memories.size} memories · ${chapter.sealedCount} sealed",
                        style = MaterialTheme.typography.bodySmall
                    )
                    chapter.memories.take(5).forEach { memory ->
                        Text(
                            "• [${memory.tag.displayName}] ${memory.note ?: memory.mediaUri.substringAfterLast('/')}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
