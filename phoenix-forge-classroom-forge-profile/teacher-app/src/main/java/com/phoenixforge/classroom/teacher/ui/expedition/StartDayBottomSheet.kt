package com.phoenixforge.classroom.teacher.ui.expedition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.phoenixforge.classroom.teacher.domain.model.ForgeDomain
import com.phoenixforge.classroom.teacher.domain.model.IntentTile
import com.phoenixforge.classroom.teacher.domain.model.TileStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartDayBottomSheet(
    tiles: List<IntentTile>,
    isPushing: Boolean,
    pushMessage: String?,
    writtenPaths: List<String>,
    onDismiss: () -> Unit,
    onPush: () -> Unit,
) {
    val today = remember { LocalDate.now() }
    val dateLabel = remember { DateTimeFormatter.ofPattern("EEEE, MMM d").format(today) }
    val stack = remember(tiles) {
        tiles
            .filter {
                when (TileStatus.fromName(it.status)) {
                    TileStatus.PLANNED, TileStatus.ACTIVE, TileStatus.SENT -> true
                    else -> false
                }
            }
            .filter {
                val kind = it.routineKind.trim()
                kind != "morning_routine" && kind != "night_routine"
            }
            .sortedBy { it.sortOrder }
    }
    var showTechnical by remember { mutableStateOf(false) }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Start day", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(dateLabel, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Text(
                "Connection check, then push today's expedition quests to Student Edition.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Today's stack", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                    if (stack.isEmpty()) {
                        Text(
                            "No daily quests queued. Add tiles on the Expedition Board.",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    } else {
                        stack.forEachIndexed { index, tile ->
                            val domain = ForgeDomain.fromName(tile.domain)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top,
                            ) {
                                Text(
                                    "${index + 1}.",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                )
                                Column {
                                    Text(tile.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                                    Text(
                                        domain.displayName,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }
                        }
                    }
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Text(
                        "Morning & night routines stay on Student → Quests (not pushed daily).",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }

            Button(
                onClick = onPush,
                enabled = !isPushing && stack.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(if (isPushing) "Pushing…" else "Push today's stack")
            }

            pushMessage?.let { message ->
                Text(
                    message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            if (writtenPaths.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Sync paths", style = MaterialTheme.typography.labelMedium)
                    IconButton(onClick = { showTechnical = !showTechnical }) {
                        Icon(
                            if (showTechnical) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Toggle paths",
                        )
                    }
                }
                if (showTechnical) {
                    writtenPaths.forEach { path ->
                        Text(
                            path,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            TextButton(onClick = onDismiss, modifier = Modifier.align(Alignment.End)) {
                Text("Done")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
