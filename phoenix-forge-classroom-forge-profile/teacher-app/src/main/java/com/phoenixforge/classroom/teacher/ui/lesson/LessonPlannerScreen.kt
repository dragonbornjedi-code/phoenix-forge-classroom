package com.phoenixforge.classroom.teacher.ui.lesson

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonPlannerScreen(
    onBack: () -> Unit,
    viewModel: LessonPlannerViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Lesson Planner")
                        Text(
                            "7 domains · ${state.subdomains.size} subdomains in view",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Domain", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(CurriculumDomainId.entries) { domainId ->
                        val selected = state.selectedDomainId == domainId
                        FilterChip(
                            selected = selected,
                            onClick = { viewModel.selectDomain(domainId) },
                            label = { Text("${domainId.emoji} ${domainId.displayName}") }
                        )
                    }
                }
            }

            item {
                Text("Subdomain", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(state.subdomains, key = { it.id }) { sub ->
                        FilterChip(
                            selected = state.selectedSubdomainId == sub.id,
                            onClick = { viewModel.selectSubdomain(sub.id) },
                            label = { Text(sub.name) }
                        )
                    }
                }
            }

            state.draft?.let { plan ->
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(plan.title, style = MaterialTheme.typography.titleLarge)
                            Text(plan.objective, style = MaterialTheme.typography.bodyMedium)
                            Text("Student mission", style = MaterialTheme.typography.labelLarge)
                            Text(plan.studentMission, style = MaterialTheme.typography.bodyMedium)
                            Text("Narrative hook", style = MaterialTheme.typography.labelLarge)
                            Text(plan.narrativeHook, style = MaterialTheme.typography.bodyMedium)
                            Text("Quest: ${plan.questTitle}", style = MaterialTheme.typography.titleSmall)
                            Text(plan.questDescription, style = MaterialTheme.typography.bodySmall)
                            Text("Materials", style = MaterialTheme.typography.labelLarge)
                            Text(plan.materials, style = MaterialTheme.typography.bodySmall)
                            Text("Steps", style = MaterialTheme.typography.labelLarge)
                            plan.steps.forEachIndexed { i, step ->
                                Text("${i + 1}. $step", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
                item {
                    Button(
                        onClick = viewModel::addToExpeditionBoard,
                        enabled = !state.isSaving,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (state.isSaving) "Saving…" else "Add plan to Expedition Board")
                    }
                }
            }
        }
    }

    state.tileCreatedMessage?.let { message ->
        AlertDialog(
            onDismissRequest = viewModel::dismissMessage,
            title = { Text("Expedition Board") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = viewModel::dismissMessage) { Text("OK") }
            }
        )
    }
}
