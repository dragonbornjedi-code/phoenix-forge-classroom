package com.phoenixforge.student.ui.story

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun StoryArchiveScreen(viewModel: StoryArchiveViewModel = hiltViewModel()) {
    val nodes by viewModel.nodes.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Story Archive", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Fragments link across time — causes, callbacks, and threads.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (nodes.isEmpty()) {
            item {
                Text("No story yet. Explore the house to begin the ledger.", style = MaterialTheme.typography.bodyLarge)
            }
        }

        items(nodes, key = { it.fragment.id }) { node ->
            StoryGraphCard(node)
        }
    }
}

@Composable
private fun StoryGraphCard(node: com.phoenixforge.student.domain.model.StoryGraphNode) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            node.fragment.continuityThread?.let {
                Text(it, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            }
            node.fragment.npcSpeaker?.let {
                Text("— $it", style = MaterialTheme.typography.labelMedium)
            }
            Spacer(modifier = Modifier.height(6.dp))
            node.fragment.callbackLine?.let { callback ->
                Text(callback, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(node.fragment.narrative, style = MaterialTheme.typography.bodyMedium)
            node.causeFragment?.let { cause ->
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "↳ because: ${cause.worldEventType.name.lowercase().replace('_', ' ')}",
                    style = MaterialTheme.typography.labelSmall
                )
            }
            if (node.fragment.emotionalImpact > 0f) {
                Text(
                    "Impact ${"%.0f".format(node.fragment.emotionalImpact * 100)}%",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}
