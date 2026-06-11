package com.phoenixforge.student.ui.quests

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.student.sync.QuestRoutineCategory
import com.phoenixforge.student.ui.components.StudentBackHeader
import com.phoenixforge.student.ui.components.StudentSparkLoading
import com.phoenixforge.student.ui.theme.StudentKidCopy

@Composable
fun QuestsScreen(
    initialCategory: QuestRoutineCategory? = null,
    onBack: (() -> Unit)? = null,
    viewModel: QuestsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(initialCategory) {
        initialCategory?.let { viewModel.ensureCategory(it) }
    }

    if (state.isLoading) {
        StudentSparkLoading(message = StudentKidCopy.questLoading())
        return
    }

    if (onBack != null) {
        Column(modifier = Modifier.fillMaxSize()) {
            StudentBackHeader(onBack = onBack, modifier = Modifier.padding(start = 4.dp))
            QuestHubScaffold(
                state = state,
                onSelectCategory = viewModel::openCategory,
                onBackToHub = viewModel::backToHub,
                onSelectItem = viewModel::selectItem,
                onSelectSideQuest = viewModel::selectSideQuest,
                onRefresh = viewModel::refresh,
                modifier = Modifier.weight(1f),
            )
        }
    } else {
        QuestHubScaffold(
            state = state,
            onSelectCategory = viewModel::openCategory,
            onBackToHub = viewModel::backToHub,
            onSelectItem = viewModel::selectItem,
            onSelectSideQuest = viewModel::selectSideQuest,
            onRefresh = viewModel::refresh,
        )
    }

    state.selectedItem?.let { item ->
        QuestMissionSheet(
            item = item,
            eventWriting = state.eventWriting,
            eventFeedback = state.eventFeedback,
            onDismiss = viewModel::dismissDetail,
            onStart = viewModel::startSelectedQuest,
            onComplete = viewModel::completeSelectedQuest,
        )
    }

    state.selectedSideQuest?.let { quest ->
        SideQuestMissionSheet(
            quest = quest,
            eventWriting = state.eventWriting,
            eventFeedback = state.eventFeedback,
            onDismiss = viewModel::dismissDetail,
            onStart = viewModel::startSelectedSideQuest,
            onComplete = viewModel::completeSelectedSideQuest,
        )
    }
}
