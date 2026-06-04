package com.phoenixforge.student.ui.quests

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
import com.phoenixforge.student.domain.model.QuestStatus

@Composable
fun QuestsScreen(viewModel: QuestsViewModel = hiltViewModel()) {
    val quests by viewModel.quests.collectAsState()
    val result by viewModel.lastResult.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Quest Board", style = MaterialTheme.typography.headlineLarge)
            result?.story?.narrative?.let { narrative ->
                Text(narrative, color = MaterialTheme.colorScheme.primary)
            }
        }

        items(quests) { quest ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("${quest.type.name} · ${quest.status.name}", style = MaterialTheme.typography.labelMedium)
                    Text(quest.title, style = MaterialTheme.typography.titleMedium)
                    Text(quest.description, style = MaterialTheme.typography.bodyMedium)
                    Text("Reward: +${quest.xpReward} XP", style = MaterialTheme.typography.bodySmall)
                    if (quest.status == QuestStatus.AVAILABLE) {
                        Button(onClick = { viewModel.completeQuest(quest) }) {
                            Text("Complete Quest")
                        }
                    }
                }
            }
        }
    }
}
