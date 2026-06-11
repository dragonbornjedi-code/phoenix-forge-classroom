package com.phoenixforge.classroom.teacher.ui.tile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.classroom.teacher.domain.curriculum.CurriculumDomainId
import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.StewardReflection
import com.phoenixforge.classroom.teacher.domain.model.StewardReflectionAxis
import com.phoenixforge.classroom.teacher.domain.model.StewardReflectionCatalog
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

            if (tile.routineKind.isNotBlank()) {
                Text(
                    "Routine: ${tile.routineKind.replace('_', ' ')} — not a daily quest swap slot.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Car quest export", style = MaterialTheme.typography.titleSmall)
                    Text(
                        "Optional: include this tile in quest-stack.json for your Termux car setup (~port 8000). " +
                            "Not the old Pi Zero dashboard — export only until you wire the car listener.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(
                    checked = tile.carFriendly,
                    onCheckedChange = viewModel::setCarFriendly,
                )
            }

            Text("Field guide", style = MaterialTheme.typography.titleSmall)
            Text(
                "Six boxes — edit any time; pre-filled from starter lessons.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = state.materials,
                    onValueChange = viewModel::updateMaterials,
                    label = { Text("Materials") },
                    modifier = Modifier.weight(1f),
                    minLines = 4,
                )
                OutlinedTextField(
                    value = state.coachingCues,
                    onValueChange = viewModel::updateCoaching,
                    label = { Text("Coaching cues") },
                    modifier = Modifier.weight(1f),
                    minLines = 4,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = state.fieldGuideExamples,
                    onValueChange = viewModel::updateExamples,
                    label = { Text("Examples") },
                    modifier = Modifier.weight(1f),
                    minLines = 4,
                )
                OutlinedTextField(
                    value = state.evidenceNotes,
                    onValueChange = viewModel::updateEvidence,
                    label = { Text("Notes") },
                    modifier = Modifier.weight(1f),
                    minLines = 4,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = state.fieldGuideSupports,
                    onValueChange = viewModel::updateSupports,
                    label = { Text("Supports") },
                    modifier = Modifier.weight(1f),
                    minLines = 4,
                )
                OutlinedTextField(
                    value = state.fieldGuideRecovery,
                    onValueChange = viewModel::updateRecovery,
                    label = { Text("Recovery") },
                    modifier = Modifier.weight(1f),
                    minLines = 4,
                )
            }

            if (status != TileStatus.COMPLETED) {
                StewardReflectionSection(
                    reflection = state.reflection,
                    onAxisChange = viewModel::updateReflectionAxis
                )
            } else {
                CompletedReflectionSummary(reflection = state.reflection)
            }

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

@Composable
private fun StewardReflectionSection(
    reflection: StewardReflection,
    onAxisChange: (StewardReflectionAxis, Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Deep reflection (L3)", style = MaterialTheme.typography.titleSmall)
        Text(
            "Optional check-in across five axes before you mark complete.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        StewardReflectionCatalog.axes.forEach { axis ->
            val value = StewardReflectionCatalog.valueFor(reflection, axis)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(axis.label, style = MaterialTheme.typography.labelLarge)
                    Text(
                        StewardReflectionCatalog.poleLabel(axis, value),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(axis.lowLabel, style = MaterialTheme.typography.labelSmall)
                    Text(axis.highLabel, style = MaterialTheme.typography.labelSmall)
                }
                Slider(
                    value = value.toFloat(),
                    onValueChange = { onAxisChange(axis, it.toInt()) },
                    valueRange = 0f..100f,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun CompletedReflectionSummary(
    reflection: StewardReflection,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Reflection recorded", style = MaterialTheme.typography.titleSmall)
        StewardReflectionCatalog.axes.forEach { axis ->
            val stored = StewardReflectionCatalog.valueFor(reflection, axis)
            if (reflectionHasStoredValue(reflection, axis)) {
                Text(
                    "${axis.label}: ${StewardReflectionCatalog.poleLabel(axis, stored)}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

private fun reflectionHasStoredValue(
    reflection: StewardReflection,
    axis: StewardReflectionAxis,
): Boolean =
    when (axis.id) {
        StewardReflectionAxis.AxisId.MENTAL -> reflection.mental != null
        StewardReflectionAxis.AxisId.EMOTIONAL -> reflection.emotional != null
        StewardReflectionAxis.AxisId.PHYSICAL -> reflection.physical != null
        StewardReflectionAxis.AxisId.EDUCATIONAL -> reflection.educational != null
        StewardReflectionAxis.AxisId.BEHAVIORAL -> reflection.behavioral != null
    }
