package com.phoenixforge.classroom.teacher.ui.curriculum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.classroom.teacher.domain.curriculum.StarterLesson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StarterLessonDetailScreen(
    onBack: () -> Unit,
    viewModel: StarterLessonViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val lesson = state.lesson

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(lesson?.title ?: "Lesson") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            if (lesson != null) {
                Button(
                    onClick = viewModel::addToExpeditionBoard,
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text("Add to Expedition Board")
                }
            }
        }
    ) { padding ->
        if (lesson == null) {
            Text("Lesson not found", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            LessonBlock("Student mission", lesson.studentMission)
            LessonBlock("Objective", lesson.objective)
            LessonBlock("Why it matters", lesson.whyItMatters)
            LessonBlock("Duration", "${lesson.durationStandard} · low-demand: ${lesson.durationLowDemand}")
            LessonBlock("Materials", lesson.materials)
            LessonBlock("Setup", lesson.setup)
            LessonBlock("Steps", lesson.steps.joinToString("\n") { "• $it" })
            LessonBlock("Questions", lesson.questions.joinToString("\n") { "• $it" })
            LessonBlock("Metric", lesson.metric)
            LessonBlock("Evidence", lesson.evidence)
            LessonBlock("Supports", lesson.supports)
            LessonBlock("Make easier", lesson.makeEasier)
            LessonBlock("Make harder", lesson.makeHarder)
            LessonBlock("Recovery", lesson.recovery)
            LessonBlock("Student Edition notes", lesson.studentEditionNotes)
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

@Composable
private fun LessonBlock(label: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.titleSmall)
        Text(body, style = MaterialTheme.typography.bodyMedium)
    }
}
