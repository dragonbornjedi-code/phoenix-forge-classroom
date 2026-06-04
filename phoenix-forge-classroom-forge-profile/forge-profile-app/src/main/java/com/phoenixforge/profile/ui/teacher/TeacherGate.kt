package com.phoenixforge.profile.ui.teacher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.profile.domain.model.TeacherMetadata

@Composable
fun TeacherGateScreen(
    viewModel: TeacherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    if (!state.isAuthorized) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Parental Gate", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Steward access requires secure parent setup on this device.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.requestStewardAccess() }) {
                Text("Request Steward Access")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { viewModel.enableStewardOnDevice() }) {
                Text("Set Up Parent Access on This Device")
            }
            state.gateMessage?.let { message ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(message, style = MaterialTheme.typography.bodySmall)
            }
        }
    } else {
        TeacherDashboard(
            metadata = state.metadata,
            onSignOut = { viewModel.signOut() }
        )
    }
}

@Composable
fun TeacherDashboard(
    metadata: List<TeacherMetadata>,
    onSignOut: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Steward Dashboard", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Academic Records & Observations", style = MaterialTheme.typography.titleMedium)
        Text("${metadata.size} steward records on file", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onSignOut) {
            Text("Sign Out")
        }
    }
}
