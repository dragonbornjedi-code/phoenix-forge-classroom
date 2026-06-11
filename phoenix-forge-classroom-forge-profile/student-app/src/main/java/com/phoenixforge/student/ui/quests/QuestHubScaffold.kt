package com.phoenixforge.student.ui.quests

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phoenixforge.student.ui.components.kidBounceClickable
import com.phoenixforge.student.domain.model.Quest
import com.phoenixforge.student.sync.QuestRoutineCategory
import com.phoenixforge.student.sync.TodayManifestItem
import com.phoenixforge.student.ui.components.StudentHearthBackground
import com.phoenixforge.student.ui.components.StudentSecondaryButton
import com.phoenixforge.student.ui.theme.StudentKidCopy
import com.phoenixforge.student.ui.theme.StudentNavTile
import com.phoenixforge.student.ui.theme.StudentPrimaryAction
import com.phoenixforge.student.ui.theme.StudentSecondaryAction

@Composable
fun QuestHubScaffold(
    state: QuestsUiState,
    onSelectCategory: (QuestRoutineCategory) -> Unit,
    onBackToHub: () -> Unit,
    onSelectItem: (TodayManifestItem) -> Unit,
    onSelectSideQuest: (Quest) -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
    val category = state.selectedCategory
    if (category == null) {
        QuestCategoryHub(
            sideQuests = state.sideQuests,
            onSelectCategory = onSelectCategory,
            onSelectSideQuest = onSelectSideQuest,
        )
    } else {
        QuestCategoryList(
            category = category,
            items = state.items,
            questRunStatuses = state.questRunStatuses,
            statusMessage = state.statusMessage,
            onBackToHub = onBackToHub,
            onSelectItem = onSelectItem,
            onRefresh = onRefresh,
        )
    }
    }
}

@Composable
private fun QuestCategoryHub(
    sideQuests: List<Quest>,
    onSelectCategory: (QuestRoutineCategory) -> Unit,
    onSelectSideQuest: (Quest) -> Unit,
) {
    StudentHearthBackground {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("⭐", style = MaterialTheme.typography.displaySmall)
        Text(StudentKidCopy.questsHubTitle(), style = MaterialTheme.typography.headlineLarge)
        Text(
            StudentKidCopy.questsHubSubtitle(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
        SideQuestStrip(
            quests = sideQuests,
            onSelectQuest = onSelectSideQuest,
        )
        QuestCategoryCard(
            emoji = "🌅",
            label = StudentKidCopy.questCategoryMorning(),
            subtitle = StudentKidCopy.questCategoryMorningHint(),
            accent = StudentPrimaryAction,
            onClick = { onSelectCategory(QuestRoutineCategory.MORNING) },
        )
        QuestCategoryCard(
            emoji = "💌",
            label = StudentKidCopy.questCategoryDaily(),
            subtitle = StudentKidCopy.questCategoryDailyHint(),
            accent = StudentSecondaryAction,
            onClick = { onSelectCategory(QuestRoutineCategory.DAILY) },
        )
        QuestCategoryCard(
            emoji = "🌙",
            label = StudentKidCopy.questCategoryNight(),
            subtitle = StudentKidCopy.questCategoryNightHint(),
            accent = MaterialTheme.colorScheme.tertiary,
            onClick = { onSelectCategory(QuestRoutineCategory.NIGHT) },
        )
    }
    }
}

@Composable
private fun QuestCategoryList(
    category: QuestRoutineCategory,
    items: List<TodayManifestItem>,
    questRunStatuses: Map<String, QuestRunStatus>,
    statusMessage: String?,
    onBackToHub: () -> Unit,
    onSelectItem: (TodayManifestItem) -> Unit,
    onRefresh: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            IconButton(onClick = onBackToHub) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to quest hub")
            }
            Text(categoryEmoji(category), fontSize = 40.sp)
            Text(categoryKidLabel(category), style = MaterialTheme.typography.headlineLarge)
            Text(
                categoryKidHint(category),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        statusMessage?.let { message ->
            item {
                Text(
                    message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        items(items, key = { "${it.dayIndex}-${it.questId}" }) { item ->
            val runStatus = questRunStatuses[item.questId] ?: QuestRunStatus.NOT_STARTED
            QuestMissionCard(
                item = item,
                runStatus = runStatus,
                onClick = { onSelectItem(item) },
            )
        }

        item {
            StudentSecondaryButton(
                text = StudentKidCopy.questRefresh(),
                onClick = onRefresh,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun QuestCategoryCard(
    emoji: String,
    label: String,
    subtitle: String,
    accent: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .kidBounceClickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = StudentNavTile.copy(alpha = 0.7f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(emoji, fontSize = 44.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Text("→", fontSize = 28.sp, color = accent)
        }
    }
}

@Composable
private fun QuestMissionCard(
    item: TodayManifestItem,
    runStatus: QuestRunStatus,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .kidBounceClickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("💌", fontSize = 40.sp)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    StudentKidCopy.questLetterFromDad(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    item.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    StudentKidCopy.questFoundSpark(),
                    style = MaterialTheme.typography.bodyLarge,
                )
                QuestRunStatusChip(status = runStatus)
            }
        }
    }
}

private fun categoryEmoji(category: QuestRoutineCategory): String = when (category) {
    QuestRoutineCategory.MORNING -> "🌅"
    QuestRoutineCategory.DAILY -> "💌"
    QuestRoutineCategory.NIGHT -> "🌙"
}

private fun categoryKidLabel(category: QuestRoutineCategory): String = when (category) {
    QuestRoutineCategory.MORNING -> StudentKidCopy.questCategoryMorning()
    QuestRoutineCategory.DAILY -> StudentKidCopy.questCategoryDaily()
    QuestRoutineCategory.NIGHT -> StudentKidCopy.questCategoryNight()
}

private fun categoryKidHint(category: QuestRoutineCategory): String = when (category) {
    QuestRoutineCategory.MORNING -> StudentKidCopy.questCategoryMorningHint()
    QuestRoutineCategory.DAILY -> StudentKidCopy.questCategoryDailyHint()
    QuestRoutineCategory.NIGHT -> StudentKidCopy.questCategoryNightHint()
}
