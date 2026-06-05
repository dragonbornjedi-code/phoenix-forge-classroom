package com.phoenixforge.student.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.phoenixforge.student.ui.navigation.StudentRoutes

@Composable
fun SettingsScreen(onNavigate: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Settings & Extras", style = MaterialTheme.typography.headlineLarge)
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onNavigate(StudentRoutes.NPC) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Companions")
                }
                Button(onClick = { onNavigate(StudentRoutes.IMPORT) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Import Forge Profile (Optional)")
                }
            }
        }
        item {
            Text(
                "Student Edition runs fully offline. Forge Profile is an optional import only.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
