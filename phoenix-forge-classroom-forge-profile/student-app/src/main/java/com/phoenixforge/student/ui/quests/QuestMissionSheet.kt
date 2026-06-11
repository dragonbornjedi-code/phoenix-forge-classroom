package com.phoenixforge.student.ui.quests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.phoenixforge.student.domain.model.Quest
import com.phoenixforge.student.sync.TodayManifestItem
import com.phoenixforge.student.ui.components.StudentPrimaryButton
import com.phoenixforge.student.ui.components.StudentSecondaryButton
import com.phoenixforge.student.ui.theme.StudentKidCopy
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestMissionSheet(
    item: TodayManifestItem,
    eventWriting: Boolean,
    eventFeedback: String?,
    onDismiss: () -> Unit,
    onStart: () -> Unit,
    onComplete: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("💌", fontSize = 44.sp)
            Text(StudentKidCopy.questLetterFromDad(), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Text(item.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                StudentKidCopy.questFoundSpark(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            if (item.detailText.isNotBlank()) {
                Text(item.detailText, style = MaterialTheme.typography.bodyLarge)
            }
            eventFeedback?.let { feedback ->
                Text(
                    feedback,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            StudentPrimaryButton(
                text = if (eventWriting) StudentKidCopy.questWriting() else StudentKidCopy.questLetsGo(),
                onClick = onStart,
                enabled = !eventWriting,
                modifier = Modifier.fillMaxWidth(),
            )
            StudentSecondaryButton(
                text = StudentKidCopy.questDone(),
                onClick = onComplete,
                enabled = !eventWriting,
                modifier = Modifier.fillMaxWidth(),
            )
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text(StudentKidCopy.questClose())
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SideQuestMissionSheet(
    quest: Quest,
    eventWriting: Boolean,
    eventFeedback: String?,
    onDismiss: () -> Unit,
    onStart: () -> Unit,
    onComplete: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("🎁", fontSize = 44.sp)
            Text(quest.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(
                StudentKidCopy.questRewardStars(quest.xpReward),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(StudentKidCopy.questFoundSpark(), style = MaterialTheme.typography.bodyLarge)
            if (quest.description.isNotBlank()) {
                Text(quest.description, style = MaterialTheme.typography.bodyMedium)
            }
            eventFeedback?.let { feedback ->
                Text(
                    feedback,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            StudentPrimaryButton(
                text = if (eventWriting) StudentKidCopy.questWriting() else StudentKidCopy.questLetsGo(),
                onClick = onStart,
                enabled = !eventWriting,
                modifier = Modifier.fillMaxWidth(),
            )
            StudentSecondaryButton(
                text = StudentKidCopy.questDone(),
                onClick = onComplete,
                enabled = !eventWriting,
                modifier = Modifier.fillMaxWidth(),
            )
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text(StudentKidCopy.questClose())
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
