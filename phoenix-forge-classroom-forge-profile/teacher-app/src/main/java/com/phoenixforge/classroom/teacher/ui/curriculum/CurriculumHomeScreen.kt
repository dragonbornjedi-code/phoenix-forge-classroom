package com.phoenixforge.classroom.teacher.ui.curriculum

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomain
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurriculumHomeScreen(
    onBack: () -> Unit,
    onOpenDomain: (CurriculumDomainId) -> Unit,
    onOpenLesson: (String) -> Unit,
    onOpenWeeklyAudit: () -> Unit,
    viewModel: CurriculumHomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Curriculum Of Life")
                        Text(
                            "Ezra · age 5½ · homeschool framework",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
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
                Text(
                    "Seven adult-facing planning categories. Student Edition translates these into missions and games.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Starter Lessons Pack 01", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Seven detailed lessons — one per domain. Import all as intent tiles, or open each lesson first.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Button(
                            onClick = viewModel::importPack01,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(if (state.pack01Imported) "Re-check Pack 01 on board" else "Import Pack 01 to Expedition Board")
                        }
                        state.pack01Lessons.forEach { lesson ->
                            ListItem(
                                headlineContent = { Text(lesson.title) },
                                supportingContent = { Text(lesson.studentMission, maxLines = 2) },
                                modifier = Modifier.clickable { onOpenLesson(lesson.id) }
                            )
                        }
                    }
                }
            }

            item {
                OutlinedButton(onClick = onOpenWeeklyAudit, modifier = Modifier.fillMaxWidth()) {
                    Text("Weekly Teacher Audit")
                }
            }

            item {
                Text("Seven domains", style = MaterialTheme.typography.titleMedium)
            }

            items(state.domains) { domain ->
                DomainCard(domain = domain, onClick = { onOpenDomain(domain.id) })
            }

            item {
                Text("Design rules", style = MaterialTheme.typography.titleSmall)
                state.designRules.forEach { rule ->
                    Text("• $rule", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 2.dp))
                }
            }
        }
    }

    state.importMessage?.let { message ->
        AlertDialog(
            onDismissRequest = viewModel::dismissImportMessage,
            title = { Text("Pack 01") },
            text = { Text(message) },
            confirmButton = {
                TextButton(onClick = viewModel::dismissImportMessage) { Text("OK") }
            }
        )
    }
}

@Composable
private fun DomainCard(domain: CurriculumDomain, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("${domain.id.emoji} ${domain.id.displayName}", style = MaterialTheme.typography.titleMedium)
            Text(domain.teacherFraming, style = MaterialTheme.typography.bodySmall, maxLines = 2)
            Text(
                "${domain.lessonPatterns.size} lesson patterns · ${domain.subcategoryCount} subcategories",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
