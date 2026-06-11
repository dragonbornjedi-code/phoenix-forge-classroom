package com.phoenixforge.student.ui.avatar

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.phoenixforge.student.domain.avatar.ImportedHeroLookParser
import com.phoenixforge.student.domain.avatar.ParsedHeroLook
import com.phoenixforge.student.ui.theme.StudentKidCopy
import com.phoenixforge.student.ui.theme.StudentSparkGold

@Composable
fun HearthWelcomeCard(
    forgeName: String,
    heroLook: ParsedHeroLook?,
    modifier: Modifier = Modifier,
) {
    val accent = heroAccentColor(heroLook?.color ?: "blue")
    val style = heroLook?.style ?: "explorer"
    val transition = rememberInfiniteTransition(label = "heroPulse")
    val pulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "heroScale",
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .scale(pulse)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                accent,
                                accent.copy(alpha = 0.65f),
                                StudentSparkGold.copy(alpha = 0.4f),
                            ),
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    ImportedHeroLookParser.styleGlyph(style),
                    fontSize = 44.sp,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    StudentKidCopy.homeGreeting(forgeName),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    StudentKidCopy.heroBadge(style, heroLook?.color ?: "blue"),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    ImportedHeroLookParser.sparkWelcome(forgeName, style),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

private fun heroAccentColor(colorName: String): Color =
    when (ImportedHeroLookParser.normalizeColor(colorName)) {
        "green" -> Color(0xFF5CD67A)
        "gold" -> Color(0xFFFFC857)
        "pink" -> Color(0xFFFF8FD0)
        "purple" -> Color(0xFFB88CFF)
        else -> Color(0xFF5CB8FF)
    }
