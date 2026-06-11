package com.phoenixforge.classroom.teacher.ui.students

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.phoenixforge.classroom.teacher.data.students.StudentStoryFragmentSnapshot
import kotlin.math.pow

@Composable
fun StudentSnapshotScreen(
    onBack: (() -> Unit)? = null,
    viewModel: StudentSnapshotViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student") },
                navigationIcon = if (onBack != null) {
                    {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                } else {
                    {}
                },
                actions = {
                    IconButton(
                        onClick = viewModel::openSendDialog,
                        enabled = !state.isPushing && state.profile != null,
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send quests to student")
                    }
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (state.isLoading) {
                Text("Loading student data…")
                return@Column
            }

            state.connectionStatus?.let { connection ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = if (connection.isTrustworthy) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.errorContainer
                        },
                    ),
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(connection.headline, style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(6.dp))
                        Text(connection.detail, style = MaterialTheme.typography.bodySmall)
                        connection.setupSteps.forEach { step ->
                            Spacer(Modifier.height(4.dp))
                            Text("• $step", style = MaterialTheme.typography.bodySmall)
                        }
                        if (!connection.isTrustworthy) {
                            Spacer(Modifier.height(10.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = viewModel::openForgeProfileToLink) {
                                    Text("Open Forge Profile")
                                }
                                if (state.staleRememberedUid != null) {
                                    OutlinedButton(onClick = viewModel::clearStaleCache) {
                                        Text("Clear stale cache")
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            if (state.linkedStudentUids.isNotEmpty()) {
                Text(
                    "Linked IDs: ${state.linkedStudentUids.joinToString { it.take(8) + "…" }}",
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(8.dp))
            }

            val profile = state.profile
            val progress = state.progress
            val behaviorSignals = state.behaviorSignals

            state.uidSourceLabel?.let { source ->
                Text("Data source · $source", style = MaterialTheme.typography.labelMedium)
                Spacer(Modifier.height(4.dp))
            }
            if (state.dataSources.isNotEmpty()) {
                Text(
                    state.dataSources.joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(8.dp))
            }
            if (state.lanPeerCount > 0) {
                Text(
                    "${state.lanPeerCount} Forge Profile peer(s) on Wi‑Fi",
                    style = MaterialTheme.typography.bodySmall,
                )
                Spacer(Modifier.height(8.dp))
            }

            profile?.let {
                Text(it.forgeName, style = MaterialTheme.typography.headlineSmall)
                Text("Child profile ID: ${it.uid}", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.teacherNotes,
                    onValueChange = viewModel::updateTeacherNotes,
                    label = { Text("Private teacher notes") },
                    placeholder = { Text("Observations only you see — not sent to the child") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                )
                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = viewModel::openSendDialog,
                    enabled = !state.isPushing && state.connectionStatus?.isTrustworthy == true,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(if (state.isPushing) "Sending…" else "Send quests to student")
                }
                if (state.connectionStatus?.isTrustworthy != true) {
                    Text(
                        "Link Ezra's tablet profile ID in Forge Profile before pushing quests.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text("Message Ezra", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.messageSubject,
                    onValueChange = viewModel::updateMessageSubject,
                    label = { Text("Subject") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = state.messageBody,
                    onValueChange = viewModel::updateMessageBody,
                    label = { Text("Message") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = viewModel::sendPersonalMessage,
                    enabled = !state.isSendingMessage,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(if (state.isSendingMessage) "Sending…" else "Send message via Forge Profile")
                }
                state.messageStatus?.let { msg ->
                    Spacer(Modifier.height(8.dp))
                    Text(msg, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                }
                state.pushMessage?.let { msg ->
                    Spacer(Modifier.height(8.dp))
                    Text(msg, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                }
                Spacer(Modifier.height(12.dp))
            }

            if (state.forgeEvents.isNotEmpty()) {
                Text("Forge events (ingested)", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                state.forgeEvents.take(6).forEach { event ->
                    Text(
                        "• ${event.eventType} · ${event.actorApp} · clock ${event.logicalClock}",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
                Spacer(Modifier.height(12.dp))
            }

            if (state.timelineRows.isNotEmpty()) {
                Text("Childhood timeline", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                state.timelineRows.take(4).forEach { row ->
                    Text("• ${row.title}", style = MaterialTheme.typography.bodySmall)
                }
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

    if (state.showSendDialog) {
        AlertDialog(
            onDismissRequest = viewModel::dismissSendDialog,
            title = { Text("Send to ${state.profile?.forgeName ?: "student"}") },
            text = {
                Text(
                    "Push today's expedition stack from the board. " +
                        "Quests appear on Student Edition → Quests tab.",
                )
            },
            confirmButton = {
                Column {
                    Button(
                        onClick = viewModel::pushQuestsAndStory,
                        enabled = !state.isPushing,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Quests + story adventure")
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = viewModel::pushQuestsOnly,
                        enabled = !state.isPushing,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Quests only")
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissSendDialog) {
                    Text("Cancel")
                }
            },
        )
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

