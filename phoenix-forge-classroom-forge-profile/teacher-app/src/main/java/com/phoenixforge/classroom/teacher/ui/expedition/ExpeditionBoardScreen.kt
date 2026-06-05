package com.phoenixforge.classroom.teacher.ui.expedition

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.TileStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpeditionBoardScreen(
    onViewProfile: () -> Unit,
    onViewStudentSnapshot: () -> Unit,
    onOpenCurriculum: () -> Unit = {},
    onTileClick: (String) -> Unit,
    viewModel: ExpeditionBoardViewModel = hiltViewModel()
) {
    val tiles by viewModel.tiles.collectAsState()
    val showSheet by viewModel.showSheet.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Expedition Board")
                        Text(
                            "${tiles.size} intent tile${if (tiles.size == 1) "" else "s"}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onOpenCurriculum) {
                        Icon(Icons.AutoMirrored.Outlined.MenuBook, contentDescription = "Curriculum Of Life")
                    }
                    IconButton(onClick = onViewProfile) {
                        Icon(Icons.Outlined.Person, contentDescription = "Forge Profile")
                    }
                    IconButton(onClick = onViewStudentSnapshot) {
                        Icon(Icons.Default.Star, contentDescription = "Student Snapshot")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = viewModel::openSheet,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("New Tile") }
            )
        }
    ) { padding ->
        if (tiles.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("No expeditions yet", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(8.dp))
                Text("Tap + to create Ezra's first intent tile")
                Spacer(Modifier.height(24.dp))
                Button(onClick = viewModel::openSheet) { Text("Create First Tile") }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tiles, key = { it.id }) { tile ->
                    IntentTileCard(
                        tile = tile,
                        onClick = { onTileClick(tile.id) },
                        onDelete = { viewModel.deleteTile(tile.id) }
                    )
                }
                item(span = { GridItemSpan(2) }) { Spacer(Modifier.height(80.dp)) }
            }
        }
    }

    if (showSheet) {
        CreateTileSheet(
            onDismiss = viewModel::closeSheet,
            onCreate = viewModel::createTile
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun IntentTileCard(
    tile: IntentTile,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val domain = ForgeDomain.fromName(tile.domain)
    val status = TileStatus.fromName(tile.status)
    var confirmDelete by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.88f)
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(onClick = onClick, onLongClick = { confirmDelete = true })
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(domain.emoji, style = MaterialTheme.typography.headlineMedium)
                Spacer(Modifier.height(8.dp))
                Text(tile.title, style = MaterialTheme.typography.titleSmall, maxLines = 2, overflow = TextOverflow.Ellipsis)
                if (tile.description.isNotBlank()) {
                    Text(
                        tile.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(status.displayName, style = MaterialTheme.typography.labelSmall)
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Open", modifier = Modifier.size(14.dp))
            }
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete tile?") },
            text = { Text("\"${tile.title}\" will be removed.") },
            confirmButton = {
                TextButton(
                    onClick = { onDelete(); confirmDelete = false },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { confirmDelete = false }) { Text("Cancel") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateTileSheet(
    onDismiss: () -> Unit,
    onCreate: (String, String, ForgeDomain) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var domain by remember { mutableStateOf(ForgeDomain.LANGUAGE) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("New Intent Tile", style = MaterialTheme.typography.titleLarge)
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
            ForgeDomain.entries.chunked(3).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    row.forEach { d ->
                        FilterChip(
                            selected = domain == d,
                            onClick = { domain = d },
                            label = { Text("${d.emoji} ${d.displayName}") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Button(
                onClick = { if (title.isNotBlank()) onCreate(title, description, domain) },
                enabled = title.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Create Tile") }
        }
    }
}
