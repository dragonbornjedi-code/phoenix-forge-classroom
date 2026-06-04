package com.phoenixforge.profile.ui.studio

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AvatarStudioScreen(
    viewModel: AvatarViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Avatar Studio", style = MaterialTheme.typography.headlineLarge)
        Text(
            "Version ${state.currentAvatar?.version ?: 0}",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Avatar Preview Placeholder
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("👤", style = MaterialTheme.typography.displayLarge)
        }

        Spacer(modifier = Modifier.height(32.dp))

        CustomizationSection("Hair", listOf("Short", "Long", "Curly", "Bald"))
        CustomizationSection("Eyes", listOf("Blue", "Green", "Brown", "Grey"))
        CustomizationSection("Clothing", listOf("Tunic", "Armor", "Robe", "Shirt"))

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { /* viewModel.updateAvatar(...) */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save My Look")
        }
    }
}

@Composable
fun CustomizationSection(title: String, options: List<String>) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        LazyRow {
            items(options) { option ->
                SuggestionChip(
                    onClick = { /* Select option */ },
                    label = { Text(option) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
