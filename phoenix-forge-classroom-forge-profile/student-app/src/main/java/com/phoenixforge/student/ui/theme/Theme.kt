package com.phoenixforge.student.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ForestDark = darkColorScheme(
    primary = Color(0xFF95D5B2),
    secondary = Color(0xFF52B788),
    tertiary = Color(0xFFB7E4C7),
    background = Color(0xFF081C15),
    surface = Color(0xFF1B4332),
    onPrimary = Color(0xFF081C15),
    onBackground = Color(0xFFD8F3DC),
    onSurface = Color(0xFFD8F3DC)
)

@Composable
fun StudentHouseTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ForestDark, content = content)
}
