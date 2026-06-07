package com.phoenixforge.classroom.teacher.ui.expedition

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpeditionBoardScreen(
    onBack: (() -> Unit)? = null,
    onViewProfile: () -> Unit,
    onViewStudentSnapshot: () -> Unit,
    onOpenCurriculum: () -> Unit = {},
    onTileClick: (String) -> Unit,
    viewModel: ExpeditionBoardViewModel = hiltViewModel()
) {
    val tiles by viewModel.tiles.collectAsState()
    val visibleTiles by viewModel.visibleTiles.collectAsState()
    val boardFilter by viewModel.filter.collectAsState()
    val showSheet by viewModel.showSheet.collectAsState()
    val showStartDayExport by viewModel.showStartDayExport.collectAsState()
    val startDayExportText by viewModel.startDayExportText.collectAsState()
    val showChariotExport by viewModel.showChariotExport.collectAsState()
    val chariotExportText by viewModel.chariotExportText.collectAsState()
    val canReorder = boardFilter == ExpeditionBoardFilter.ALL

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
                navigationIcon = if (onBack != null) {
                    {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back to Curriculum")
                        }
                    }
                } else {
                    {}
                },
                actions = {
                    IconButton(onClick = viewModel::openStartDayExport) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Start day export")
                    }
                    IconButton(onClick = viewModel::openChariotExport) {
                        Icon(Icons.Default.DirectionsCar, contentDescription = "Chariot quest stack")
                    }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ExpeditionBoardFilter.entries.forEach { option ->
                    FilterChip(
                        selected = boardFilter == option,
                        onClick = { viewModel.setFilter(option) },
                        label = { Text(option.label) }
                    )
                }
            }

            if (!canReorder && tiles.isNotEmpty()) {
                Text(
                    "Switch to All to drag-reorder the stack.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else if (canReorder && visibleTiles.size > 1) {
                Text(
                    "Long-press the handle to reorder.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            when {
                visibleTiles.isEmpty() -> {
                    ExpeditionBoardEmptyState(
                        hasAnyTiles = tiles.isNotEmpty(),
                        onCreate = viewModel::openSheet,
                        onShowAll = { viewModel.setFilter(ExpeditionBoardFilter.ALL) }
                    )
                }
                canReorder -> {
                    val lazyListState = rememberLazyListState()
                    val reorderState =
                        rememberReorderableLazyListState(lazyListState) { from, to ->
                            viewModel.moveTile(from.index, to.index)
                        }
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 88.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(visibleTiles, key = { it.id }) { tile ->
                            ReorderableItem(reorderState, key = tile.id) { isDragging ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.DragHandle,
                                        contentDescription = "Drag to reorder",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.draggableHandle()
                                    )
                                    IntentTileCard(
                                        tile = tile,
                                        isDragging = isDragging,
                                        onClick = { onTileClick(tile.id) },
                                        onDelete = { viewModel.deleteTile(tile.id) },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 88.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(visibleTiles, key = { it.id }) { tile ->
                            IntentTileCard(
                                tile = tile,
                                onClick = { onTileClick(tile.id) },
                                onDelete = { viewModel.deleteTile(tile.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showSheet) {
        CreateTileSheet(
            onDismiss = viewModel::closeSheet,
            onCreate = viewModel::createTile
        )
    }

    if (showStartDayExport) {
        AlertDialog(
            onDismissRequest = viewModel::closeStartDayExport,
            title = { Text("Start day") },
            text = {
                Text(
                    startDayExportText,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            confirmButton = {
                TextButton(onClick = viewModel::closeStartDayExport) {
                    Text("Done")
                }
            }
        )
    }

    if (showChariotExport) {
        AlertDialog(
            onDismissRequest = viewModel::closeChariotExport,
            title = { Text("Chariot quest stack") },
            text = {
                Text(
                    chariotExportText,
                    style = MaterialTheme.typography.bodySmall
                )
            },
            confirmButton = {
                TextButton(onClick = viewModel::closeChariotExport) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
private fun ExpeditionBoardEmptyState(
    hasAnyTiles: Boolean,
    onCreate: () -> Unit,
    onShowAll: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (hasAnyTiles) {
            Text("No tiles in this view", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Try another filter or create a new intent tile.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Button(onClick = onShowAll) { Text("Show all tiles") }
        } else {
            Text("No expeditions yet", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Tap + to create the first intent tile for today's stack.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(16.dp))
            Button(onClick = onCreate) { Text("Create first tile") }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun IntentTileCard(
    tile: IntentTile,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    isDragging: Boolean = false,
) {
    val domain = ForgeDomain.fromName(tile.domain)
    val status = TileStatus.fromName(tile.status)
    var confirmDelete by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(onClick = onClick, onLongClick = { confirmDelete = true }),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isDragging) 8.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Text(domain.emoji, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(4.dp))
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
            Spacer(Modifier.height(8.dp))
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
