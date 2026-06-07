package com.phoenixforge.student.ui.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.phoenixforge.student.domain.avatar.ImportedHeroLookParser
import com.phoenixforge.student.domain.avatar.ParsedHeroLook

@Composable
fun HearthWelcomeCard(
    forgeName: String,
    heroLook: ParsedHeroLook?,
    modifier: Modifier = Modifier,
) {
    val accent = heroAccentColor(heroLook?.color ?: "blue")
    val style = heroLook?.style ?: "explorer"

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(accent, accent.copy(alpha = 0.55f), MaterialTheme.colorScheme.surface),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    ImportedHeroLookParser.styleGlyph(style),
                    style = MaterialTheme.typography.displaySmall,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    forgeName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    "${ImportedHeroLookParser.displayStyle(style)} · ${ImportedHeroLookParser.displayColor(heroLook?.color ?: "blue")}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    ImportedHeroLookParser.sparkWelcome(forgeName, style),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
        }
    }
}

private fun heroAccentColor(colorName: String): Color =
    when (ImportedHeroLookParser.normalizeColor(colorName)) {
        "green" -> Color(0xFF33CC59)
        "gold" -> Color(0xFFCCB326)
        "pink" -> Color(0xFFFF73CC)
        "purple" -> Color(0xFFA673FF)
        else -> Color(0xFF3399FF)
    }
