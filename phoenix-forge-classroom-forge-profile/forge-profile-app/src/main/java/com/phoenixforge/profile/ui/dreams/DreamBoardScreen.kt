package com.phoenixforge.profile.ui.dreams

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.profile.domain.model.DreamEntry
import com.phoenixforge.profile.domain.spark.DreamBoardCatalog

@Composable
fun DreamBoardScreen(
    viewModel: DreamBoardViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Dream Board", style = MaterialTheme.typography.headlineLarge)
            Text(
                "Short-term and long-term dreams you choose yourself. The board starts empty.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        DreamBoardCatalog.horizons.forEach { horizon ->
            val saved = state.dreamsByType[horizon.type].orEmpty()
            item {
                DreamHorizonSection(
                    title = horizon.title,
                    savedEntries = saved,
                    addButtonLabel = horizon.addButtonLabel,
                    onAddDream = { viewModel.startAddDream(horizon.type) }
                )
            }
        }
    }

    when (val step = state.addStep) {
        is AddDreamStep.AskSuggestion -> WantSuggestionDialog(
            horizonTitle = DreamBoardCatalog.horizonTitle(step.horizon),
            onYes = { viewModel.onWantSuggestion(true) },
            onNo = { viewModel.onWantSuggestion(false) },
            onDismiss = viewModel::dismissAddFlow
        )
        is AddDreamStep.PickCategory -> CategoryPickerDialog(
            horizonTitle = DreamBoardCatalog.horizonTitle(step.horizon),
            onCategory = viewModel::selectCategory,
            onDismiss = viewModel::dismissAddFlow
        )
        is AddDreamStep.PickExample -> ExamplePickerDialog(
            horizonTitle = DreamBoardCatalog.horizonTitle(step.horizon),
            category = DreamBoardCatalog.categoryById(step.categoryId),
            onExample = viewModel::saveExample,
            onBack = viewModel::backToCategoryPicker,
            onDismiss = viewModel::dismissAddFlow
        )
        is AddDreamStep.CustomEntry -> CustomDreamDialog(
            horizonTitle = DreamBoardCatalog.horizonTitle(step.horizon),
            text = step.text,
            onTextChange = viewModel::updateCustomDreamText,
            onSave = viewModel::saveCustomDream,
            onDismiss = viewModel::dismissAddFlow
        )
        AddDreamStep.Idle -> Unit
    }
}

@Composable
private fun DreamHorizonSection(
    title: String,
    savedEntries: List<DreamEntry>,
    addButtonLabel: String,
    onAddDream: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        if (savedEntries.isEmpty()) {
            Text(
                "Nothing here yet.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            savedEntries.forEach { entry ->
                ListItem(
                    headlineContent = { Text(entry.content) },
                    leadingContent = { Text("✨") }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onAddDream, modifier = Modifier.fillMaxWidth()) {
            Text(addButtonLabel)
        }
    }
}

@Composable
private fun WantSuggestionDialog(
    horizonTitle: String,
    onYes: () -> Unit,
    onNo: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add to $horizonTitle") },
        text = { Text("Help me think — want a suggestion?") },
        confirmButton = {
            Button(onClick = onYes) { Text("Yes") }
        },
        dismissButton = {
            OutlinedButton(onClick = onNo) { Text("No, I'll write my own") }
        }
    )
}

@Composable
private fun CategoryPickerDialog(
    horizonTitle: String,
    onCategory: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pick a spark for $horizonTitle") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DreamBoardCatalog.suggestionCategories.forEach { category ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onCategory(category.id) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(category.title, style = MaterialTheme.typography.titleMedium)
                            Text(category.subtitle, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
private fun ExamplePickerDialog(
    horizonTitle: String,
    category: DreamBoardCatalog.SuggestionCategory?,
    onExample: (String) -> Unit,
    onBack: () -> Unit,
    onDismiss: () -> Unit
) {
    if (category == null) {
        onDismiss()
        return
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("${category.title} — $horizonTitle") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(category.subtitle, style = MaterialTheme.typography.bodySmall)
                category.examples.forEach { example ->
                    OutlinedButton(
                        onClick = { onExample(example) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(example)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onBack) { Text("Back") }
        }
    )
}

@Composable
private fun CustomDreamDialog(
    horizonTitle: String,
    text: String,
    onTextChange: (String) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Your $horizonTitle dream") },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                label = { Text("Write it in your own words") },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(onClick = onSave, enabled = text.isNotBlank()) {
                Text("Add to board")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
