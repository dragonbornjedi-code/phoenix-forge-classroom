package com.phoenixforge.student.ui.quests

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.phoenixforge.student.domain.model.Quest
import com.phoenixforge.student.domain.model.QuestStatus
import com.phoenixforge.student.ui.theme.StudentKidCopy

enum class QuestRunStatus {
    NOT_STARTED,
    IN_PROGRESS,
    DONE,
}

fun QuestRunStatus.kidLabel(): String = when (this) {
    QuestRunStatus.NOT_STARTED -> StudentKidCopy.questStatusNotStarted()
    QuestRunStatus.IN_PROGRESS -> StudentKidCopy.questStatusInProgress()
    QuestRunStatus.DONE -> StudentKidCopy.questStatusDone()
}

@Composable
fun QuestRunStatusChip(
    status: QuestRunStatus,
    modifier: Modifier = Modifier,
) {
    val containerColor = when (status) {
        QuestRunStatus.NOT_STARTED -> MaterialTheme.colorScheme.surfaceVariant
        QuestRunStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primaryContainer
        QuestRunStatus.DONE -> MaterialTheme.colorScheme.secondaryContainer
    }
    AssistChip(
        onClick = {},
        enabled = false,
        label = { Text(status.kidLabel(), style = MaterialTheme.typography.labelLarge) },
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            disabledContainerColor = containerColor,
        ),
    )
}

fun QuestStatus.toRunStatus(): QuestRunStatus? = when (this) {
    QuestStatus.AVAILABLE -> QuestRunStatus.NOT_STARTED
    QuestStatus.IN_PROGRESS -> QuestRunStatus.IN_PROGRESS
    QuestStatus.COMPLETED, QuestStatus.CLAIMED -> QuestRunStatus.DONE
}

fun sideQuestRunStatuses(quests: List<Quest>): Map<String, QuestRunStatus> =
    quests.mapNotNull { quest ->
        quest.status.toRunStatus()?.let { quest.id to it }
    }.toMap()
