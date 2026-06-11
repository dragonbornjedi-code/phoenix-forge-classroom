package com.phoenixforge.student.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val KidLightColorScheme = lightColorScheme(
    primary = StudentPrimaryAction,
    onPrimary = StudentPrimaryOnAction,
    primaryContainer = StudentPrimaryContainer,
    onPrimaryContainer = Color(0xFF1B4332),
    secondary = StudentSecondaryAction,
    onSecondary = StudentSecondaryOnAction,
    secondaryContainer = StudentSecondaryContainer,
    onSecondaryContainer = Color(0xFF1A365D),
    tertiary = StudentNavTile,
    onTertiary = StudentNavTileOn,
    background = StudentHearthCream,
    surface = StudentHearthCream,
    surfaceVariant = StudentLockedSurface,
    onSurfaceVariant = StudentLockedOnSurface,
)

private val KidTypography = Typography(
    headlineLarge = TextStyle(
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 38.sp,
    ),
    headlineSmall = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 30.sp,
    ),
    titleMedium = TextStyle(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 26.sp,
    ),
    bodyLarge = TextStyle(
        fontSize = 18.sp,
        lineHeight = 26.sp,
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    labelLarge = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 22.sp,
    ),
)

@Composable
fun StudentHouseTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = KidLightColorScheme,
        typography = KidTypography,
        content = content,
    )
}
