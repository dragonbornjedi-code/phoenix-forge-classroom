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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyAuditScreen(
    onBack: () -> Unit,
    viewModel: WeeklyAuditViewModel = hiltViewModel()
) {
    val draft by viewModel.draft.collectAsState()
    val saved by viewModel.saved.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weekly Teacher Audit") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text(if (saved) "Saved" else "Save audit draft")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "For each domain: one win, one friction, one method that worked, one to try next, one metric.",
                style = MaterialTheme.typography.bodyMedium
            )

            CurriculumDomainId.entries.forEach { domain ->
                val entry = draft.sections[domain]
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("${domain.emoji} ${domain.displayName}", style = MaterialTheme.typography.titleSmall)
                        AuditField("Win observed", entry?.winObserved.orEmpty()) {
                            viewModel.updateSection(domain, WeeklyAuditViewModel.SectionField.WIN, it)
                        }
                        AuditField("Friction point", entry?.frictionPoint.orEmpty()) {
                            viewModel.updateSection(domain, WeeklyAuditViewModel.SectionField.FRICTION, it)
                        }
                        AuditField("Method that worked", entry?.methodWorked.orEmpty()) {
                            viewModel.updateSection(domain, WeeklyAuditViewModel.SectionField.METHOD_WORKED, it)
                        }
                        AuditField("Method to try next", entry?.methodToTryNext.orEmpty()) {
                            viewModel.updateSection(domain, WeeklyAuditViewModel.SectionField.METHOD_TRY_NEXT, it)
                        }
                        AuditField("Metric to track", entry?.metricToTrack.orEmpty()) {
                            viewModel.updateSection(domain, WeeklyAuditViewModel.SectionField.METRIC, it)
                        }
                    }
                }
            }

            Text("Overall", style = MaterialTheme.typography.titleMedium)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AuditField("Best energy window", draft.overall.bestEnergyWindow) {
                        viewModel.updateOverall(WeeklyAuditViewModel.OverallField.ENERGY, it)
                    }
                    AuditField("Hardest transition", draft.overall.hardestTransition) {
                        viewModel.updateOverall(WeeklyAuditViewModel.OverallField.TRANSITION, it)
                    }
                    AuditField("Strongest motivator", draft.overall.strongestMotivator) {
                        viewModel.updateOverall(WeeklyAuditViewModel.OverallField.MOTIVATOR, it)
                    }
                    AuditField("Current overload signs", draft.overall.overloadSigns) {
                        viewModel.updateOverall(WeeklyAuditViewModel.OverallField.OVERLOAD, it)
                    }
                    AuditField("Next tiny V1.1 adjustment", draft.overall.nextAdjustment) {
                        viewModel.updateOverall(WeeklyAuditViewModel.OverallField.ADJUSTMENT, it)
                    }
                }
            }
        }
    }
}

@Composable
private fun AuditField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        minLines = 1,
        maxLines = 3
    )
}
