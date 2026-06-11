package com.phoenixforge.student.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.phoenixforge.student.ui.theme.StudentHearthCream
import com.phoenixforge.student.ui.theme.StudentSkyMist
import com.phoenixforge.student.ui.theme.StudentSunrisePeach

@Composable
fun StudentHearthBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        StudentSunrisePeach.copy(alpha = 0.35f),
                        StudentHearthCream,
                        StudentSkyMist.copy(alpha = 0.45f),
                    ),
                ),
            ),
        content = content,
    )
}
