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
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.classroom.teacher.domain.curriculum.StarterLesson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurriculumSubdomainQuestsScreen(
    onBack: () -> Unit,
    onOpenLesson: (String) -> Unit,
    viewModel: CurriculumSubdomainQuestsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val subdomain = state.subdomain

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(subdomain?.name ?: "Subdomain quests")
                        Text(
                            "${state.quests.size} quest${if (state.quests.size == 1) "" else "s"} in catalog",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        if (subdomain == null) {
            Text("Subdomain not found", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                Text(subdomain.summary, style = MaterialTheme.typography.bodyMedium)
            }
            items(state.quests, key = { it.id }) { quest ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectQuest(quest) },
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(quest.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                        Text(
                            when (quest) {
                                is SubdomainQuestItem.PackLesson -> "Pack 01 · gold-standard lesson"
                                is SubdomainQuestItem.TopicQuest -> "Topic mission · ${quest.topic}"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }

    state.selectedQuest?.let { quest ->
        QuestDetailDialog(
            quest = quest,
            onDismiss = viewModel::dismissDetail,
            onOpenPackLesson = onOpenLesson,
        )
    }
}

@Composable
private fun QuestDetailDialog(
    quest: SubdomainQuestItem,
    onDismiss: () -> Unit,
    onOpenPackLesson: (String) -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(quest.title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                when (quest) {
                    is SubdomainQuestItem.PackLesson -> PackLessonDetail(quest.lesson)
                    is SubdomainQuestItem.TopicQuest -> {
                        DetailBlock("Narrative hook", quest.draft.narrativeHook)
                        DetailBlock("Student mission", quest.draft.studentMission)
                        DetailBlock("Spark reaction", quest.draft.sparkReactionSeed)
                        DetailBlock("XP reward stub", "${quest.draft.xpReward} XP")
                    }
                }
            }
        },
        confirmButton = {
            when (quest) {
                is SubdomainQuestItem.PackLesson -> {
                    TextButton(onClick = { onOpenPackLesson(quest.lesson.id); onDismiss() }) {
                        Text("Full lesson detail")
                    }
                }
                is SubdomainQuestItem.TopicQuest -> {
                    TextButton(onClick = onDismiss) { Text("Close") }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Dismiss") }
        },
    )
}

@Composable
private fun PackLessonDetail(lesson: StarterLesson) {
    DetailBlock("Student mission", lesson.studentMission)
    DetailBlock("Objective", lesson.objective)
    DetailBlock("Why it matters", lesson.whyItMatters)
    DetailBlock("Materials", lesson.materials)
    DetailBlock("Steps", lesson.steps.joinToString("\n") { "• $it" })
    DetailBlock("Supports (UDL)", lesson.supports)
    DetailBlock("Make easier", lesson.makeEasier)
    DetailBlock("Make harder", lesson.makeHarder)
    DetailBlock("Recovery", lesson.recovery)
}

@Composable
private fun DetailBlock(label: String, body: String) {
    if (body.isBlank()) return
    Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
    Text(body, style = MaterialTheme.typography.bodySmall)
}
