package com.phoenixforge.student.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.phoenixforge.student.domain.model.WorldDriftState

@Composable
fun WorldDriftPanel(drift: WorldDriftState?, modifier: Modifier = Modifier) {
    if (drift == null) return

    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("World drift", style = MaterialTheme.typography.titleMedium)
            Text(drift.driftLabel, style = MaterialTheme.typography.labelLarge)
            Text(drift.visualCue, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(12.dp))
            DriftBar("Gallery vitality", drift.galleryVitality)
            Spacer(modifier = Modifier.height(6.dp))
            DriftBar("Quietness", drift.worldQuietness)
            Spacer(modifier = Modifier.height(6.dp))
            DriftBar("Time bend", drift.temporalDistortion)
        }
    }
}

@Composable
private fun DriftBar(label: String, value: Float) {
    val fraction = value.coerceIn(0f, 1f)
    Text(label, style = MaterialTheme.typography.labelSmall)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.primary),
        )
    }
}
