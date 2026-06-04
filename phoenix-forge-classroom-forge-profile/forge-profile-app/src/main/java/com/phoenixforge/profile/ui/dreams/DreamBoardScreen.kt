package com.phoenixforge.profile.ui.dreams

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DreamBoardScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Dream Board", style = MaterialTheme.typography.headlineLarge)
            Text("What do you want to learn or build someday?", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item { DreamCategory("My Big Dreams", listOf("Build a real robot", "Visit a volcano")) }
        item { DreamCategory("Things to Learn", listOf("How to bake bread", "Play the drums")) }
        item { DreamCategory("Places to Go", listOf("The library", "The big creek", "Grandma's house")) }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { /* Add new dream */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Add a New Dream")
            }
        }
    }
}

@Composable
fun DreamCategory(title: String, items: List<String>) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge)
        items.forEach { item ->
            ListItem(
                headlineContent = { Text(item) },
                leadingContent = { Text("✨") }
            )
        }
    }
}
