package com.phoenixforge.classroom.teacher.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.classroom.teacher.ui.identity.TeacherIdentityViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgeProfileViewerScreen(
    onBack: () -> Unit,
    viewModel: TeacherIdentityViewModel = hiltViewModel()
) {
    val snapshot by viewModel.snapshot.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Forge Profile — Who am I") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val profile = snapshot
            if (profile == null) {
                Text("Loading teacher identity…")
            } else if (!profile.isLinked) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Not linked yet", style = MaterialTheme.typography.titleMedium)
                        Text(
                            profile.errorMessage
                                ?: "Open Forge Profile on this device, sign in with “I am a teacher”, then return here.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Button(onClick = viewModel::refresh) {
                            Text("Refresh link")
                        }
                    }
                }
            } else {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(profile.forgeName, style = MaterialTheme.typography.headlineSmall)
                        Text("Teacher profile", style = MaterialTheme.typography.labelLarge)
                        Text("Stage: ${profile.currentStage}", style = MaterialTheme.typography.bodyMedium)
                        profile.currentTitle?.let {
                            Text("Title: $it", style = MaterialTheme.typography.bodyMedium)
                        }
                        Text("Profile ID: ${profile.uid}", style = MaterialTheme.typography.bodySmall)
                    }
                }
                Text(
                    "Teacher Edition reads your identity from Forge Profile on this device. Steward student links live in Forge Profile → Students.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Button(onClick = viewModel::refresh) {
                    Text("Refresh")
                }
            }
        }
    }
}
