package com.phoenixforge.profile.ui.teacher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.profile.ui.interop.ExternalApps

@Composable
fun TeacherGateScreen(
    onSignOut: () -> Unit = {},
    viewModel: TeacherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    if (!state.isAuthorized) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Parental Gate", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Mentorship access requires secure parent setup on this device.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.requestStewardAccess() }) {
                Text("Request Mentorship Access")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { viewModel.enableStewardOnDevice() }) {
                Text("Set Up Parent Access on This Device")
            }
            state.gateMessage?.let { message ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(message, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Teacher Edition now owns mentorship. Open it for curriculum planning and daily expedition work.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { ExternalApps.launchTeacherEdition(context) }) {
                Text("Open Teacher Edition")
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onSignOut) {
                Text("Switch profile")
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Mentorship access enabled", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Open Teacher Edition to plan curriculum and view student check-ins in a read-only view.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { ExternalApps.launchTeacherEdition(context) }) {
                Text("Open Teacher Edition")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { viewModel.signOut() }) {
                Text("Disable this device access")
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onSignOut) {
                Text("Switch profile")
            }
        }
    }
}
