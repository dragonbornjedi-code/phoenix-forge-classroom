package com.phoenixforge.profile.ui.teacher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.phoenixforge.profile.domain.copy.AppBoundaryCopy
import com.phoenixforge.profile.ui.interop.ExternalApps

@Composable
fun TeacherGateScreen(
    onSignOut: () -> Unit = {},
    viewModel: TeacherViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(AppBoundaryCopy.PARENT_GATE_TITLE, style = MaterialTheme.typography.headlineMedium)
        Text(
            AppBoundaryCopy.PARENT_GATE_SUBTITLE,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        BoundarySummaryCard()

        Spacer(modifier = Modifier.height(24.dp))

        if (!state.isAuthorized) {
            Text(
                AppBoundaryCopy.PARENT_GATE_LOCKED_BODY,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.requestStewardAccess() }, modifier = Modifier.fillMaxWidth()) {
                Text(AppBoundaryCopy.REQUEST_PARENT_ACCESS)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { viewModel.enableStewardOnDevice() }, modifier = Modifier.fillMaxWidth()) {
                Text(AppBoundaryCopy.ENABLE_PARENT_ON_DEVICE)
            }
            state.gateMessage?.let { message ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
            }
        } else {
            Text(
                AppBoundaryCopy.PARENT_GATE_UNLOCKED_BODY,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { ExternalApps.launchTeacherEdition(context) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(AppBoundaryCopy.OPEN_TEACHER_EDITION)
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { viewModel.signOut() }, modifier = Modifier.fillMaxWidth()) {
                Text(AppBoundaryCopy.DISABLE_PARENT_ACCESS)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(onClick = onSignOut, modifier = Modifier.fillMaxWidth()) {
            Text("Switch profile")
        }
    }
}

@Composable
private fun BoundarySummaryCard() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Three apps, one childhood", style = MaterialTheme.typography.titleSmall)
            Text(AppBoundaryCopy.FORGE_PROFILE_OWNS, style = MaterialTheme.typography.bodySmall)
            Text(AppBoundaryCopy.STUDENT_EDITION_OWNS, style = MaterialTheme.typography.bodySmall)
            Text(AppBoundaryCopy.TEACHER_EDITION_OWNS, style = MaterialTheme.typography.bodySmall)
            Text(AppBoundaryCopy.MANUAL_SYNC, style = MaterialTheme.typography.bodySmall)
            Text(
                AppBoundaryCopy.MEMORY_IMMUTABLE,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}
