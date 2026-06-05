package com.phoenixforge.classroom.teacher.ui.curriculum

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomain
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurriculumHomeScreen(
    onOpenExpedition: () -> Unit,
    onViewProfile: () -> Unit,
    onViewStudentSnapshot: () -> Unit,
    onOpenDomain: (CurriculumDomainId) -> Unit,
    onOpenLesson: (String) -> Unit,
    onOpenWeeklyAudit: () -> Unit,
    onOpenLessonPlanner: () -> Unit,
    onOpenSageAdvisor: () -> Unit,
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
                            "Ezra · age 5+ · seven domains for growing humans",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onViewProfile) {
                        Icon(Icons.Outlined.Person, contentDescription = "Forge Profile")
                    }
                    IconButton(onClick = onViewStudentSnapshot) {
                        Icon(Icons.Outlined.Star, contentDescription = "Student Snapshot")
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
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "The whole homeschool framework",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "These seven top-level domains together cover everything worth teaching " +
                                "a growing human — from reading and telling time to breathwork, money, " +
                                "emotional regulation, and civic duty. Tap a domain to see its subdomains " +
                                "(reading, budgeting, conflict resolution, and more).",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        OutlinedButton(
                            onClick = onOpenExpedition,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Outlined.Explore, contentDescription = null)
                            Text("Expedition Board — intent tiles", modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }

            item {
                Text(
                    "Seven domains",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            items(state.domains) { domain ->
                DomainCard(domain = domain, onClick = { onOpenDomain(domain.id) })
            }

            item {
                Button(onClick = onOpenLessonPlanner, modifier = Modifier.fillMaxWidth()) {
                    Text("Lesson Planner — subdomain → quest draft")
                }
            }

            item {
                OutlinedButton(onClick = onOpenSageAdvisor, modifier = Modifier.fillMaxWidth()) {
                    Text("Sage Advisor — monthly eval (online)")
                }
            }

            item {
                OutlinedButton(onClick = onOpenWeeklyAudit, modifier = Modifier.fillMaxWidth()) {
                    Text("Weekly Teacher Audit")
                }
            }

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Starter Lessons Pack 01", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "One detailed lesson per domain — import to Expedition Board or preview first.",
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
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${domain.id.sectionNumber}. ${domain.id.emoji} ${domain.id.displayName}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Open domain")
            }
            Text(
                domain.focusLine,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                "${domain.subdomains.size} subdomains · ${domain.lessonPatterns.size} lesson patterns",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
