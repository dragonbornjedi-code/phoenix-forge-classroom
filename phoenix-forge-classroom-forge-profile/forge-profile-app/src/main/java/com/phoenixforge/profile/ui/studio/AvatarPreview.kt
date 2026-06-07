package com.phoenixforge.profile.ui.studio

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.phoenixforge.profile.domain.model.Avatar

/**
 * Layered Compose avatar: clothing → head → hair → eyes.
 * Replaces the emoji placeholder (master step 0.56).
 */
@Composable
fun AvatarPreview(
    avatar: Avatar?,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
) {
    val look = AvatarPreviewMapper.fromAvatar(avatar)

    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp,
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = this.size.width
                val h = this.size.height
                val cx = w / 2f
                val headRadius = w * 0.22f
                val headCenterY = h * 0.38f

                drawClothing(look, cx, headCenterY + headRadius * 0.6f, w, h)
                drawHead(look.skinColor, cx, headCenterY, headRadius)
                drawHeroStyleOverlay(look, cx, headCenterY, headRadius, w)
                if (look.hairStyle != HairStyle.BALD && look.heroStyle == HeroStyle.GUARDIAN) {
                    drawHair(look, cx, headCenterY, headRadius)
                }
                drawEyes(look.eyeColor, cx, headCenterY, headRadius)
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawClothing(
    look: AvatarPreviewLook,
    cx: Float,
    headBottom: Float,
    w: Float,
    h: Float,
) {
    val torsoTop = headBottom - w * 0.04f
    val torsoHeight = h * 0.38f
    val torsoWidth = w * 0.52f
    val left = cx - torsoWidth / 2f

    when (look.clothingStyle) {
        ClothingStyle.ARMOR -> {
            drawRoundRect(
                color = look.clothingColor,
                topLeft = Offset(left, torsoTop),
                size = Size(torsoWidth, torsoHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.06f),
            )
            drawRoundRect(
                color = look.clothingColor.copy(alpha = 0.7f),
                topLeft = Offset(left + w * 0.08f, torsoTop + h * 0.04f),
                size = Size(torsoWidth - w * 0.16f, torsoHeight * 0.35f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.03f),
            )
        }
        ClothingStyle.ROBE -> {
            val path = Path().apply {
                moveTo(cx, torsoTop)
                lineTo(left - w * 0.06f, torsoTop + torsoHeight)
                lineTo(cx + torsoWidth / 2f + w * 0.06f, torsoTop + torsoHeight)
                close()
            }
            drawPath(path, look.clothingColor)
        }
        ClothingStyle.SHIRT -> {
            drawRoundRect(
                color = look.clothingColor,
                topLeft = Offset(left, torsoTop),
                size = Size(torsoWidth, torsoHeight * 0.85f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.04f),
            )
        }
        ClothingStyle.TUNIC -> {
            drawRoundRect(
                color = look.clothingColor,
                topLeft = Offset(left, torsoTop),
                size = Size(torsoWidth, torsoHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.08f),
            )
            drawLine(
                color = look.clothingColor.copy(alpha = 0.55f),
                start = Offset(cx, torsoTop),
                end = Offset(cx, torsoTop + torsoHeight * 0.55f),
                strokeWidth = w * 0.015f,
            )
        }
        ClothingStyle.HOODED -> {
            val headRadius = w * 0.22f
            val path = Path().apply {
                moveTo(cx, torsoTop - headRadius * 0.15f)
                lineTo(left - w * 0.04f, torsoTop + torsoHeight)
                lineTo(cx + torsoWidth / 2f + w * 0.04f, torsoTop + torsoHeight)
                close()
            }
            drawPath(path, look.clothingColor.copy(alpha = 0.92f))
            drawPath(
                path,
                look.heroAccent.copy(alpha = 0.35f),
                style = Stroke(width = w * 0.02f),
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawHeroStyleOverlay(
    look: AvatarPreviewLook,
    cx: Float,
    cy: Float,
    headRadius: Float,
    w: Float,
) {
    when (look.heroStyle) {
        HeroStyle.EXPLORER -> {
            drawArc(
                color = look.heroAccent.copy(alpha = 0.9f),
                startAngle = 200f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(cx - headRadius * 1.15f, cy - headRadius * 1.35f),
                size = Size(headRadius * 2.3f, headRadius * 1.4f),
                style = Stroke(width = w * 0.035f),
            )
            drawLine(
                color = look.heroAccent,
                start = Offset(cx, cy - headRadius * 1.05f),
                end = Offset(cx, cy - headRadius * 1.45f),
                strokeWidth = w * 0.025f,
            )
        }
        HeroStyle.BUILDER -> {
            drawRoundRect(
                color = look.heroAccent.copy(alpha = 0.35f),
                topLeft = Offset(cx - headRadius * 1.25f, cy + headRadius * 0.55f),
                size = Size(headRadius * 2.5f, headRadius * 0.35f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(headRadius * 0.12f),
            )
        }
        HeroStyle.ARTIST -> {
            val hatPath = Path().apply {
                moveTo(cx - headRadius * 0.95f, cy - headRadius * 0.55f)
                lineTo(cx + headRadius * 0.95f, cy - headRadius * 0.55f)
                lineTo(cx, cy - headRadius * 1.55f)
                close()
            }
            drawPath(hatPath, look.heroAccent)
            drawCircle(
                color = look.heroAccent.copy(alpha = 0.75f),
                radius = headRadius * 0.12f,
                center = Offset(cx + headRadius * 0.55f, cy + headRadius * 0.35f),
            )
        }
        HeroStyle.GUARDIAN -> {
            drawArc(
                color = look.clothingColor.copy(alpha = 0.95f),
                startAngle = 190f,
                sweepAngle = 160f,
                useCenter = true,
                topLeft = Offset(cx - headRadius * 1.2f, cy - headRadius * 1.25f),
                size = Size(headRadius * 2.4f, headRadius * 1.55f),
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawHead(
    skinColor: Color,
    cx: Float,
    cy: Float,
    radius: Float,
) {
    drawCircle(color = skinColor, radius = radius, center = Offset(cx, cy))
    drawCircle(
        color = skinColor.copy(alpha = 0.85f),
        radius = radius * 0.92f,
        center = Offset(cx, cy),
        style = Stroke(width = radius * 0.06f),
    )
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawHair(
    look: AvatarPreviewLook,
    cx: Float,
    cy: Float,
    headRadius: Float,
) {
    when (look.hairStyle) {
        HairStyle.SHORT -> {
            drawArc(
                color = look.hairColor,
                startAngle = 200f,
                sweepAngle = 140f,
                useCenter = true,
                topLeft = Offset(cx - headRadius * 1.05f, cy - headRadius * 1.15f),
                size = Size(headRadius * 2.1f, headRadius * 1.5f),
            )
        }
        HairStyle.LONG -> {
            drawArc(
                color = look.hairColor,
                startAngle = 190f,
                sweepAngle = 160f,
                useCenter = true,
                topLeft = Offset(cx - headRadius * 1.1f, cy - headRadius * 1.2f),
                size = Size(headRadius * 2.2f, headRadius * 1.6f),
            )
            drawRoundRect(
                color = look.hairColor,
                topLeft = Offset(cx - headRadius * 0.95f, cy + headRadius * 0.1f),
                size = Size(headRadius * 1.9f, headRadius * 1.4f),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(headRadius * 0.4f),
            )
        }
        HairStyle.CURLY -> {
            val puffCount = 5
            for (i in 0 until puffCount) {
                val angle = Math.PI * (0.35 + i * 0.3)
                val px = cx + (kotlin.math.cos(angle) * headRadius * 0.85f).toFloat()
                val py = cy - headRadius * 0.55f + (kotlin.math.sin(angle) * headRadius * 0.35f).toFloat()
                drawCircle(
                    color = look.hairColor,
                    radius = headRadius * 0.38f,
                    center = Offset(px, py),
                )
            }
        }
        HairStyle.BALD -> Unit
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawEyes(
    eyeColor: Color,
    cx: Float,
    cy: Float,
    headRadius: Float,
) {
    val eyeRadius = headRadius * 0.11f
    val eyeOffsetX = headRadius * 0.38f
    val eyeY = cy + headRadius * 0.05f
    listOf(cx - eyeOffsetX, cx + eyeOffsetX).forEach { ex ->
        drawCircle(color = Color.White, radius = eyeRadius * 1.15f, center = Offset(ex, eyeY))
        drawCircle(color = eyeColor, radius = eyeRadius, center = Offset(ex, eyeY))
        drawCircle(
            color = Color.Black.copy(alpha = 0.85f),
            radius = eyeRadius * 0.45f,
            center = Offset(ex, eyeY),
        )
    }
}
