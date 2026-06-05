package com.phoenixforge.classroom.teacher.ui.tile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainId
import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.TileStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TileDetailScreen(
    onBack: () -> Unit,
    viewModel: TileDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val tile = state.tile

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tile?.title ?: "Intent Tile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (tile == null) {
            Text("Tile not found", modifier = Modifier.padding(padding).padding(16.dp))
            return@Scaffold
        }

        val domain = ForgeDomain.fromName(tile.domain)
        val status = TileStatus.fromName(tile.status)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("${domain.emoji} ${domain.displayName}", style = MaterialTheme.typography.labelLarge)
            Text(status.displayName, style = MaterialTheme.typography.titleMedium)
            if (tile.description.isNotBlank()) {
                Text(tile.description, style = MaterialTheme.typography.bodyLarge)
            }

            CurriculumDomainId.fromId(tile.curriculumDomainId)?.let { curriculum ->
                Text(
                    "${curriculum.emoji} ${curriculum.displayName}",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            if (tile.studentMission.isNotBlank()) {
                Text("Student mission", style = MaterialTheme.typography.titleSmall)
                Text(tile.studentMission, style = MaterialTheme.typography.bodyMedium)
            }
            if (tile.lessonPatternId.isNotBlank()) {
                Text("Lesson pattern: ${tile.lessonPatternId}", style = MaterialTheme.typography.bodySmall)
            }

            Text("Field guide", style = MaterialTheme.typography.titleSmall)
            OutlinedTextField(
                value = state.materials,
                onValueChange = viewModel::updateMaterials,
                label = { Text("Materials") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            OutlinedTextField(
                value = state.coachingCues,
                onValueChange = viewModel::updateCoaching,
                label = { Text("Coaching cues") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            OutlinedTextField(
                value = state.evidenceNotes,
                onValueChange = viewModel::updateEvidence,
                label = { Text("Evidence / steward note") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(onClick = viewModel::saveDetails, modifier = Modifier.fillMaxWidth()) {
                Text("Save details")
            }
            Button(onClick = viewModel::markComplete, modifier = Modifier.fillMaxWidth()) {
                Text("Mark completed")
            }
            Text(
                "P2: Send to Phoenix Forge Classroom Student Edition as quest. P2: Promote to Forge Profile chronicle.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
