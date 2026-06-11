package com.phoenixforge.profile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = ForgePrimaryAction,
    onPrimary = ForgePrimaryOnAction,
    secondary = ForgeSecondaryAction,
    onSecondary = ForgeSecondaryOnAction,
    tertiary = ForgeNavTile,
    onTertiary = ForgeNavTileOn,
    surfaceVariant = ForgeLockedSurface,
    onSurfaceVariant = ForgeLockedOnSurface,
    outline = Color(0xFFB0B8C4),
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6BC49A),
    onPrimary = Color(0xFF0D2818),
    secondary = Color(0xFF8EB4E8),
    onSecondary = Color(0xFF0F1E33),
    tertiary = Color(0xFF2A3F55),
    onTertiary = Color(0xFFDCE8F4),
)

@Composable
fun ForgeProfileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content
    )
}
