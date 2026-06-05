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
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurriculumDomainScreen(
    onBack: () -> Unit,
    onOpenLesson: (String) -> Unit,
    viewModel: CurriculumDomainViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val domain = uiState.domain ?: return
    val lessons = uiState.lessons

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${domain.id.emoji} ${domain.id.displayName}") },
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
            item { SectionBlock("Teacher framing", domain.teacherFraming) }
            item { SectionBlock("Student framing", domain.studentFraming) }
            item { BulletSection("Lesson patterns", domain.lessonPatterns) }
            item { BulletSection("Progress metrics", domain.progressMetrics) }
            item { BulletSection("Teacher cues", domain.teacherCues) }
            item { BulletSection("Support methods", domain.supportMethods) }

            if (lessons.isNotEmpty()) {
                item { Text("Pack 01 lessons", style = MaterialTheme.typography.titleMedium) }
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

@Composable
private fun SectionBlock(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(body, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun BulletSection(title: String, items: List<String>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            items.forEach { item ->
                ListItem(headlineContent = { Text(item, style = MaterialTheme.typography.bodySmall) })
            }
        }
    }
}
