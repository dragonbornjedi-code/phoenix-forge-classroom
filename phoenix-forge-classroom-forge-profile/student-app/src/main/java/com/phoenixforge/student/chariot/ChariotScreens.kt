package com.phoenixforge.student.chariot

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChariotNight = Brush.verticalGradient(
    colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E), Color(0xFF0F3460))
)

@Composable
fun ChariotListeningScreen() {
    val transition = rememberInfiniteTransition(label = "mic")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing), RepeatMode.Reverse),
        label = "micScale",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ChariotNight),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎤", fontSize = 96.sp, modifier = Modifier.scale(scale))
            Spacer(Modifier.height(16.dp))
            Text(
                "Listening to Ezra…",
                color = Color(0xFF00FF88),
                fontSize = 28.sp,
            )
            Spacer(Modifier.height(24.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(5) { index ->
                    WaveBar(index = index)
                }
            }
        }
    }
}

@Composable
private fun WaveBar(index: Int) {
    val transition = rememberInfiniteTransition(label = "wave$index")
    val heightScale by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(500, delayMillis = index * 100, easing = LinearEasing),
            RepeatMode.Reverse,
        ),
        label = "waveHeight",
    )
    Box(
        modifier = Modifier
            .size(width = 10.dp, height = (40 * heightScale).dp)
            .background(Color(0xFF00BFFF), RoundedCornerShape(5.dp))
    )
}

@Composable
fun ChariotCelebrationScreen(xp: Int, questTitle: String) {
    val transition = rememberInfiniteTransition(label = "celebrate")
    val bounce by transition.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "bounce",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ChariotNight),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🐉", fontSize = 96.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                "QUEST COMPLETE!",
                color = Color(0xFFFFD700),
                fontSize = 40.sp,
                modifier = Modifier.padding(top = bounce.dp),
                textAlign = TextAlign.Center,
            )
            Text(
                questTitle,
                color = Color.White,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            )
            Text(
                "+$xp XP",
                color = Color(0xFF00FF88),
                fontSize = 36.sp,
            )
        }
    }
}

@Composable
fun ChariotWelcomeScreen(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ChariotNight),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp),
        ) {
            Text("🏎️", fontSize = 72.sp)
            Spacer(Modifier.height(16.dp))
            Text(
                message,
                color = Color(0xFFFFD700),
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChariotDeckScreen(stack: ChariotQuestStack, onCelebrate: (ChariotMission) -> Unit) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ChariotNight)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                stack.welcomeLine,
                color = Color(0xFFFFD700),
                fontSize = 24.sp,
                modifier = Modifier.padding(vertical = 8.dp),
            )
        }
        itemsIndexed(stack.missions) { index, mission ->
            Card(
                onClick = { onCelebrate(mission) },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF16213E)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        "${index + 1}. ${mission.title}",
                        color = Color.White,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        mission.studentMission,
                        color = Color(0xFFB0C4DE),
                        modifier = Modifier.padding(top = 4.dp),
                    )
                    if (mission.missionCards.isNotEmpty()) {
                        Text(
                            mission.missionCards.joinToString(" · "),
                            color = Color(0xFF00FF88),
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                }
            }
        }
    }
}
