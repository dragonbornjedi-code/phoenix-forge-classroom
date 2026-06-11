package com.phoenixforge.student.ui.garden

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.phoenixforge.student.ui.components.StudentBackHeader

@Composable
fun GardenMerchantScreen(
    onBack: (() -> Unit)? = null,
    viewModel: GardenMerchantViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        onBack?.let { back ->
            item { StudentBackHeader(onBack = back) }
        }
        item {
            Text("Garden merchant", style = MaterialTheme.typography.headlineLarge)
            Text(
                "Spend forge tokens on plants and tools. Purchases sync to Forge World.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(
                "${state.tokens} forge tokens",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp),
            )
            state.statusMessage?.let { msg ->
                Text(msg, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
        }

        items(viewModel.catalog, key = { it.itemId }) { item ->
            val owned = item.itemId in state.ownedItemIds
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(item.displayName, style = MaterialTheme.typography.titleSmall)
                    Text("${item.cost} tokens", style = MaterialTheme.typography.bodySmall)
                    Button(
                        onClick = { viewModel.purchase(item.itemId) },
                        enabled = !owned && !state.purchasing && state.tokens >= item.cost,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(
                            when {
                                owned -> "Already yours"
                                state.purchasing -> "Buying…"
                                state.tokens < item.cost -> "Need more tokens"
                                else -> "Buy"
                            },
                        )
                    }
                }
            }
        }
    }
}
