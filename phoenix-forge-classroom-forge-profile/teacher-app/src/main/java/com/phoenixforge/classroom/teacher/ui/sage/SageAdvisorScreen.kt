package com.phoenixforge.classroom.teacher.ui.sage

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.clickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.classroom.teacher.domain.sage.AiProviderCatalog
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SageAdvisorScreen(
    onBack: (() -> Unit)? = null,
    onOpenExpedition: () -> Unit = {},
    onOpenTile: (String) -> Unit = {},
    viewModel: SageAdvisorViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size, state.isSending) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.lastIndex)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Sage Advisor")
                        Text(
                            if (state.isOnline) {
                                "Online · long-press Sage replies for actions"
                            } else {
                                "Offline — connect for chat"
                            },
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                },
                navigationIcon = if (onBack != null) {
                    {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                } else {
                    {}
                },
                actions = {
                    IconButton(onClick = viewModel::toggleSettings) {
                        Icon(Icons.Outlined.Settings, contentDescription = "API settings")
                    }
                },
            )
        },
        bottomBar = {
            SageInputBar(
                input = state.input,
                isSending = state.isSending,
                onInputChange = viewModel::updateInput,
                onSend = viewModel::send,
            )
        },
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (!state.hasCredentials || state.showSettings) {
                item {
                    SageSettingsCard(state = state, viewModel = viewModel)
                }
            }

            state.error?.let { err ->
                item {
                    Text(err, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
            }

            items(state.messages, key = { it.id }) { msg ->
                SageMessageCard(
                    message = msg,
                    isApplying = state.isApplying,
                    onApply = { viewModel.applyActions(msg.id) },
                    onOpenExpedition = onOpenExpedition,
                    onOpenTile = onOpenTile,
                )
            }

            if (state.isSending) {
                item {
                    Text("Sage is thinking…", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
private fun SageInputBar(
    input: String,
    isSending: Boolean,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
) {
    Surface(tonalElevation = 4.dp) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                label = { Text("Ask Sage to draft or update expedition tiles") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 96.dp),
                minLines = 1,
                maxLines = 3,
                enabled = !isSending,
            )
            Button(
                onClick = onSend,
                enabled = !isSending && input.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (isSending) "Thinking…" else "Send to Sage")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun SageMessageCard(
    message: SageUiMessage,
    isApplying: Boolean,
    onApply: () -> Unit,
    onOpenExpedition: () -> Unit,
    onOpenTile: (String) -> Unit,
) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    val isUser = message.role == "user"
    var showMenu by remember { mutableStateOf(false) }
    var showSelectSheet by remember { mutableStateOf(false) }
    val selectSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val tts = remember { TextToSpeech(context, null) }
    LaunchedEffect(tts) {
        tts.language = Locale.US
    }
    DisposableEffect(Unit) {
        onDispose { tts.shutdown() }
    }

    val canCreateTile = !isUser &&
        message.pendingActions.isNotEmpty() &&
        !message.actionsApplied

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {},
                    onLongClick = { showMenu = true },
                ),
            colors = if (isUser) {
                CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            } else {
                CardDefaults.cardColors()
            },
        ) {
            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                if (!isUser) {
                    Text(
                        "Long-press for copy, read aloud, or expedition tile",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                SelectionContainer {
                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 360.dp)
                            .verticalScroll(rememberScrollState()),
                    )
                }

                if (canCreateTile) {
                    val summary = message.pendingActions.joinToString(" · ") { action ->
                        when (action.op.lowercase()) {
                            "update" -> "Update ${action.matchTitle ?: action.title ?: "tile"}"
                            else -> "Add ${action.title ?: "tile"}"
                        }
                    }
                    Text(
                        "Draft ready: $summary",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Button(
                        onClick = onApply,
                        enabled = !isApplying,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(if (isApplying) "Creating tile…" else "Add to Expedition Board")
                    }
                }

                message.applyMessage?.let { applied ->
                    Text(applied, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                }

                if (message.actionsApplied && message.createdTileIds.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        OutlinedButton(
                            onClick = onOpenExpedition,
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Review board")
                        }
                        if (message.createdTileIds.size == 1) {
                            Button(
                                onClick = { onOpenTile(message.createdTileIds.first()) },
                                modifier = Modifier.weight(1f),
                            ) {
                                Text("Open tile")
                            }
                        }
                    }
                }
            }
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false },
        ) {
            DropdownMenuItem(
                text = { Text("Select all") },
                onClick = {
                    showMenu = false
                    showSelectSheet = true
                },
            )
            DropdownMenuItem(
                text = { Text("Copy") },
                onClick = {
                    clipboard.setText(AnnotatedString(message.content))
                    showMenu = false
                },
            )
            if (canCreateTile) {
                DropdownMenuItem(
                    text = { Text(if (isApplying) "Creating tile…" else "Create expedition tile") },
                    enabled = !isApplying,
                    onClick = {
                        showMenu = false
                        onApply()
                    },
                )
            }
            if (!isUser) {
                DropdownMenuItem(
                    text = { Text("Read aloud") },
                    onClick = {
                        showMenu = false
                        val plain = message.content.replace(Regex("\\*+"), "").trim()
                        if (plain.isNotEmpty()) {
                            tts.stop()
                            tts.speak(plain, TextToSpeech.QUEUE_FLUSH, null, message.id)
                        }
                    },
                )
            }
        }
    }

    if (showSelectSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSelectSheet = false },
            sheetState = selectSheetState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text("Select all", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = TextFieldValue(
                        text = message.content,
                        selection = TextRange(0, message.content.length),
                    ),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                )
                TextButton(
                    onClick = { showSelectSheet = false },
                    modifier = Modifier.align(androidx.compose.ui.Alignment.End),
                ) {
                    Text("Done")
                }
            }
        }
    }
}

@Composable
private fun SageSettingsCard(
    state: SageAdvisorUiState,
    viewModel: SageAdvisorViewModel,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text("Secure API credentials", style = MaterialTheme.typography.titleSmall)
            Text(
                "Type your API key only — pick provider and free-tier model below.",
                style = MaterialTheme.typography.bodySmall,
            )
            OutlinedTextField(
                value = state.apiKeyInput,
                onValueChange = viewModel::updateApiKeyInput,
                label = { Text("API key") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            val provider = viewModel.selectedProvider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleProviderExpanded() }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Provider", style = MaterialTheme.typography.labelMedium)
                    Text(provider.displayName, style = MaterialTheme.typography.bodyMedium)
                    Text(provider.signupHint, style = MaterialTheme.typography.bodySmall)
                }
                Icon(
                    if (state.providerExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                )
            }
            if (state.providerExpanded) {
                AiProviderCatalog.providers.forEach { preset ->
                    TextButton(
                        onClick = { viewModel.selectProvider(preset) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            "${preset.displayName} — ${preset.freeTierModels.size} free models",
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleModelExpanded() }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Model (free tier)", style = MaterialTheme.typography.labelMedium)
                    Text(state.selectedModelId, style = MaterialTheme.typography.bodyMedium)
                }
                Icon(
                    if (state.modelExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                )
            }
            if (state.modelExpanded) {
                provider.freeTierModels.forEach { modelId ->
                    TextButton(
                        onClick = { viewModel.selectModel(modelId) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(modelId, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = viewModel::saveCredentials) { Text("Save") }
                if (state.hasCredentials) {
                    OutlinedButton(onClick = viewModel::clearCredentials) { Text("Clear key") }
                }
            }
        }
    }
}
