package com.phoenixforge.profile.ui.identity

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun IdentityCardScreen(
    viewModel: IdentityViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item { Text("Identity Card", style = MaterialTheme.typography.headlineLarge) }
        
        item {
            IdentityField(
                label = "Forge Name",
                fieldKey = "forgeName",
                value = state.profile?.forgeName ?: "",
                onValueChange = viewModel::updateField
            )
        }
        item {
            IdentityField(
                label = "Favorite Color",
                fieldKey = "favoriteColor",
                value = state.profile?.favoriteColor ?: "",
                onValueChange = viewModel::updateField
            )
        }
        item {
            IdentityField(
                label = "Current Title",
                fieldKey = "currentTitle",
                value = state.profile?.currentTitle ?: "",
                onValueChange = viewModel::updateField
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text("About Me", style = MaterialTheme.typography.headlineSmall)
        }
        
        item { AboutMePrompt("What makes you happy?") }
        item { AboutMePrompt("What are you proud of?") }
    }
}

@Composable
fun IdentityField(
    label: String,
    fieldKey: String,
    value: String,
    onValueChange: (String, String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(fieldKey, it) },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun AboutMePrompt(prompt: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(prompt, style = MaterialTheme.typography.titleMedium)
            TextField(
                value = "",
                onValueChange = { /* ... */ },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Tap to answer...") }
            )
        }
    }
}
