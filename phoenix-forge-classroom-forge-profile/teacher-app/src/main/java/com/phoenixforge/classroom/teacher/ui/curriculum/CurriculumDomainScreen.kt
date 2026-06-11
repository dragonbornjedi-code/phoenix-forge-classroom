package com.phoenixforge.classroom.teacher.ui.curriculum

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumSubdomain

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CurriculumDomainScreen(
    onBack: () -> Unit,
    onOpenLesson: (String) -> Unit,
    onOpenSubdomain: (String) -> Unit,
    viewModel: CurriculumDomainViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val domain = uiState.domain ?: return
    val lessons = uiState.lessons

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("${domain.id.emoji} ${domain.id.displayName}")
                        Text(
                            "Domain ${domain.id.sectionNumber} of 7",
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
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(domain.focusLine, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text(domain.teacherFraming, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            "Student Edition: ${domain.studentFraming}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                Text(
                    "Subdomains (${domain.subdomains.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Skill groups within this domain — reading, money, breathwork, telling time, and similar topics.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(domain.subdomains) { subdomain ->
                SubdomainCard(
                    subdomain = subdomain,
                    onClick = { onOpenSubdomain(subdomain.id) },
                )
            }

            item { BulletSection("Lesson patterns", domain.lessonPatterns) }
            item { BulletSection("Progress metrics", domain.progressMetrics) }
            item { BulletSection("Teacher cues", domain.teacherCues) }
            item { BulletSection("Support methods", domain.supportMethods) }

            if (lessons.isNotEmpty()) {
                item {
                    Text("Pack 01 lessons", style = MaterialTheme.typography.titleMedium)
                }
                items(lessons) { lesson ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenLesson(lesson.id) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(lesson.title, style = MaterialTheme.typography.titleSmall)
                            Text(lesson.studentMission, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SubdomainCard(
    subdomain: CurriculumSubdomain,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(subdomain.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(subdomain.summary, style = MaterialTheme.typography.bodyMedium)
            if (subdomain.topics.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    subdomain.topics.forEach { topic ->
                        AssistChip(
                            onClick = onClick,
                            label = { Text(topic, style = MaterialTheme.typography.labelSmall) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BulletSection(title: String, items: List<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            items.forEach { item ->
                Text("• $item", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}
