package com.phoenixforge.student.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.student.ui.components.StudentHearthBackground
import com.phoenixforge.student.ui.components.StudentPrimaryButton
import com.phoenixforge.student.ui.components.StudentSecondaryButton
import com.phoenixforge.student.ui.navigation.StudentRoutes
import com.phoenixforge.student.ui.theme.StudentKidCopy

@Composable
fun SettingsScreen(
    onNavigate: (String) -> Unit,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsState()
    val pastImports by viewModel.pastImports.collectAsState()

    StudentHearthBackground {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Text("✨", fontSize = 40.sp)
                Text("More", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        StudentPrimaryButton(
                            text = StudentKidCopy.settingsSyncHero(),
                            onClick = { onNavigate(StudentRoutes.IMPORT) },
                        )
                        StudentSecondaryButton(
                            text = "Visit friends",
                            onClick = { onNavigate(StudentRoutes.DIGITAL_HOME_COMPANIONS) },
                        )
                    }
                }
            }

            item {
                Text(
                    StudentKidCopy.settingsParentSection(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                if (uiState.isSignedIn) {
                    StudentSecondaryButton(
                        text = StudentKidCopy.settingsSignOut(),
                        onClick = viewModel::signOut,
                    )
                }
                uiState.signOutMessage?.let { message ->
                    Text(message, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
                }
            }

            if (uiState.showDeveloperSection) {
                item {
                    Text("Forge World memories", style = MaterialTheme.typography.titleMedium)
                    StudentPrimaryButton(
                        text = if (uiState.isImporting) "Importing…" else "Import memories (${uiState.draftCount})",
                        onClick = viewModel::importForgeWorldMemories,
                        enabled = !uiState.isImporting,
                    )
                    if (uiState.isImporting) {
                        Text("✨ Importing…", modifier = Modifier.padding(top = 8.dp))
                    }
                    uiState.importMessage?.let { message ->
                        Text(message, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }

            item {
                StudentSecondaryButton(
                    text = if (uiState.showDeveloperSection) "Hide grown-up tools" else "Show grown-up tools",
                    onClick = viewModel::toggleDeveloperSection,
                )
            }

            if (uiState.showDeveloperSection && pastImports.isNotEmpty()) {
                items(pastImports) { snapshot ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(snapshot.forgeName, style = MaterialTheme.typography.titleSmall)
                            Text("uid: ${snapshot.uid}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
