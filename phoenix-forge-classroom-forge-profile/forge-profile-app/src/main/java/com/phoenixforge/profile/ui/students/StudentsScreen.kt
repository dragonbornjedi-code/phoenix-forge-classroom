package com.phoenixforge.profile.ui.students

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun StudentsScreen(
    viewModel: StudentsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text("Students", style = MaterialTheme.typography.headlineLarge)
            Text(
                "Link student Forge Profiles you manage. Each student keeps their own profile on their device — you reference them here by profile ID.",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        if (state.linkedStudents.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("No students linked yet", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "When a student creates their Forge Profile, copy their profile ID from their device and link it here.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        } else {
            items(state.linkedStudents, key = { it.profileUid }) { student ->
                ListItem(
                    headlineContent = { Text(student.displayName) },
                    supportingContent = {
                        Column {
                            Text("ID: ${student.profileUid}", style = MaterialTheme.typography.bodySmall)
                            student.notes?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                        }
                    },
                    trailingContent = {
                        TextButton(onClick = { viewModel.unlink(student.profileUid) }) {
                            Text("Unlink")
                        }
                    }
                )
            }
        }

        item {
            Button(
                onClick = viewModel::openLinkDialog,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Link a student profile")
            }
        }
    }

    if (state.showLinkDialog) {
        AlertDialog(
            onDismissRequest = viewModel::dismissLinkDialog,
            title = { Text("Link student profile") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = state.linkName,
                        onValueChange = viewModel::updateLinkName,
                        label = { Text("Student name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = state.linkUid,
                        onValueChange = viewModel::updateLinkUid,
                        label = { Text("Profile ID (from student's Forge Profile)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = state.linkNotes,
                        onValueChange = viewModel::updateLinkNotes,
                        label = { Text("Notes (optional)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    state.errorMessage?.let {
                        Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                Button(onClick = viewModel::saveLink) { Text("Link") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::dismissLinkDialog) { Text("Cancel") }
            }
        )
    }
}
