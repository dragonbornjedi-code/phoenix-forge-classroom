package com.phoenixforge.classroom.teacher.ui.students

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.Icon
import com.phoenixforge.classroom.teacher.data.students.StudentStoryFragmentSnapshot
import kotlin.math.pow

@Composable
fun StudentSnapshotScreen(
    onBack: () -> Unit,
    viewModel: StudentSnapshotViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student Snapshot (read-only)") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                Text("Loading student data…")
                return@Scaffold
            }
            state.errorMessage?.let { msg ->
                Text(msg, color = MaterialTheme.colorScheme.error)
                Spacer(Modifier.height(12.dp))
            }

            val profile = state.profile
            val progress = state.progress
            val behaviorSignals = state.behaviorSignals

            profile?.let {
                Text(it.forgeName, style = MaterialTheme.typography.headlineSmall)
                Text("Student profile ID: ${it.uid}", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(12.dp))
            }

            progress?.let { p ->
                val percent = xpProgressToNextLevel(p.level, p.xp)
                LinearProgressIndicator(
                    progress = percent.coerceIn(0f, 1f),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Text("Level ${p.level} · XP ${p.xp} · Streak ${p.streakDays} days")
                Spacer(Modifier.height(12.dp))

                if (p.achievements.isNotEmpty()) {
                    Text("Game grades / achievements", style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(8.dp))
                    p.achievements.take(8).forEach { id ->
                        Text("• $id", style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(Modifier.height(12.dp))
                }
            }

            behaviorSignals?.let { s ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Lesson progress (this week)", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(8.dp))
                        Text("Quests completed: ${s.questsCompletedThisWeek}")
                        Text("Photos / memories: ${s.photosThisWeek}")
                        Text("Returns this week: ${s.returnsThisWeek}")
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            Text("Emotional check-ins (recent)", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            state.emotionalCheckIns.take(6).forEach { fragment ->
                EmotionalCheckInRow(fragment = fragment)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun EmotionalCheckInRow(fragment: StudentStoryFragmentSnapshot) {
    val pct = (fragment.emotionalImpact * 100).coerceIn(0f, 100f)
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(fragment.worldEventType.ifBlank { "Event" }, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(6.dp))
            Text("Emotional impact: ${"%.0f".format(pct)}%")
            fragment.narrative?.takeIf { it.isNotBlank() }?.let {
                Spacer(Modifier.height(6.dp))
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

private fun xpProgressToNextLevel(level: Int, xp: Int): Float {
    val currentLevelXpThreshold = xpThresholdForLevel(level)
    val nextLevelXp = xpForNextLevel(level)
    val intoLevel = xp - currentLevelXpThreshold
    return if (nextLevelXp <= 0) 0f else intoLevel.toFloat() / nextLevelXp.toFloat()
}

private fun xpThresholdForLevel(level: Int): Int {
    var threshold = 0
    for (l in 1 until level) threshold += xpForNextLevel(l)
    return threshold
}

private fun xpForNextLevel(level: Int): Int = (50 * level.toDouble().pow(1.2)).toInt()

