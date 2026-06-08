package com.phoenixforge.profile.ui.studio

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.profile.domain.avatar.AvatarHeroCatalog
import com.phoenixforge.profile.domain.avatar.AvatarShardCatalog
import com.phoenixforge.profile.domain.copy.AppBoundaryCopy

@Composable
fun AvatarStudioScreen(
    viewModel: AvatarViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val preview = viewModel.previewAvatar()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Avatar Studio", style = MaterialTheme.typography.headlineLarge)
        Text(
            "KayKit hero · shard ${state.draftShardLevel} · v${state.lastSavedVersion}",
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(
            "Same look in Forge World when you cross from the tablet.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(24.dp))

        AvatarPreview(
            avatar = preview,
            modifier = Modifier.size(220.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))

        HeroChipSection(
            title = "Hero style",
            options = AvatarHeroCatalog.heroStyles,
            selected = state.draftStyle,
            onSelect = viewModel::selectStyle,
            label = AvatarHeroCatalog::displayStyle,
        )

        HeroColorSection(
            selected = state.draftColor,
            onSelect = viewModel::selectColor,
        )

        HeroChipSection(
            title = "Skin tone",
            options = AvatarHeroCatalog.skinTones,
            selected = state.draftSkinTone,
            onSelect = viewModel::selectSkinTone,
            label = AvatarHeroCatalog::displaySkinTone,
        )

        ShardLevelSection(
            selected = state.draftShardLevel,
            suggested = state.suggestedShardLevel,
            onSelect = viewModel::selectShardLevel,
            onApplySuggested = viewModel::applySuggestedShard,
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedButton(
                onClick = viewModel::randomize,
                modifier = Modifier.weight(1f),
            ) {
                Text("Randomize")
            }
            Button(
                onClick = {
                    viewModel.pushToTablet(context) { message ->
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                },
                modifier = Modifier.weight(1f),
            ) {
                Text("Push to tablet")
            }
        }

        Text(
            AppBoundaryCopy.pushAvatarHint(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp),
        )
    }
}

@Composable
private fun HeroChipSection(
    title: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    label: (String) -> String,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(options) { option ->
                val isSelected = AvatarHeroCatalog.normalizeStyle(option) ==
                    AvatarHeroCatalog.normalizeStyle(selected)
                Card(
                    modifier = Modifier
                        .clickable { onSelect(option) }
                        .then(
                            if (isSelected) {
                                Modifier.border(2.dp, MaterialTheme.colorScheme.primary, MaterialTheme.shapes.medium)
                            } else {
                                Modifier
                            },
                        ),
                ) {
                    Text(
                        label(option),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun ShardLevelSection(
    selected: Int,
    suggested: Int,
    onSelect: (Int) -> Unit,
    onApplySuggested: () -> Unit,
) {
    val tier = AvatarShardCatalog.tier(selected)
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text("Shard tier (0–6)", style = MaterialTheme.typography.titleMedium)
        Text(
            "${tier.title} — ${tier.hint}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (selected != suggested) {
            Text(
                "Suggested for age: $suggested (${AvatarShardCatalog.tier(suggested).title})",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clickable { onApplySuggested() },
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 8.dp),
        ) {
            items(AvatarShardCatalog.tiers) { shard ->
                val isSelected = shard.level == selected
                Card(
                    modifier = Modifier
                        .clickable { onSelect(shard.level) }
                        .then(
                            if (isSelected) {
                                Modifier.border(
                                    2.dp,
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.shapes.medium,
                                )
                            } else {
                                Modifier
                            },
                        ),
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        Text("${shard.level}", style = MaterialTheme.typography.titleMedium)
                        Text(shard.title, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroColorSection(
    selected: String,
    onSelect: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text("Hero color", style = MaterialTheme.typography.titleMedium)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(AvatarHeroCatalog.heroColors) { colorName ->
                val color = when (colorName) {
                    "green" -> Color(0xFF33CC59)
                    "gold" -> Color(0xFFCCB326)
                    "pink" -> Color(0xFFFF73CC)
                    "purple" -> Color(0xFFA673FF)
                    else -> Color(0xFF3399FF)
                }
                val isSelected = AvatarHeroCatalog.normalizeColor(selected) == colorName
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(0.5f),
                            shape = CircleShape,
                        )
                        .clickable { onSelect(colorName) },
                    contentAlignment = Alignment.Center,
                ) {
                    if (isSelected) {
                        Text("✓", color = Color.White)
                    }
                }
            }
        }
    }
}
