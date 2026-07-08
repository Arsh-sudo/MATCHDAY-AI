package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Modifier to apply Apple's signature Liquid Glass styling.
 * It features a subtle glassy vertical linear gradient backdrop,
 * a dual-toned top-lit border that mimics the highlight refraction on a glass lens,
 * and a satin specular sheen at the top.
 */
fun Modifier.liquidGlass(
    shape: Shape = RoundedCornerShape(24.dp),
    borderWidth: Dp = 1.2.dp,
    topBorderColor: Color = Color(0x404ADE80), // 25% emerald green for high-tech border
    bottomBorderColor: Color = Color(0x0D4ADE80),
    bgStartColor: Color = Color(0xFF163E1F).copy(alpha = 0.70f), // Soft dark-green turf start
    bgEndColor: Color = Color(0xFF06140A).copy(alpha = 0.95f)   // Deep field-night end
): Modifier = this
    .clip(shape)
    .background(
        Brush.verticalGradient(
            colors = listOf(bgStartColor, bgEndColor)
        )
    )
    .drawBehind {
        // Draw a satin specular gleam at the top edge of the card
        val sheenGradient = Brush.verticalGradient(
            colors = listOf(
                Color.White.copy(alpha = 0.12f),
                Color.White.copy(alpha = 0.03f),
                Color.Transparent
            ),
            startY = 0f,
            endY = size.height * 0.35f
        )
        drawRect(brush = sheenGradient)
    }
    .border(
        width = borderWidth,
        brush = Brush.verticalGradient(
            colors = listOf(topBorderColor, bottomBorderColor)
        ),
        shape = shape
    )

/**
 * A beautiful, reusable Apple Liquid Glass container.
 */
@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier.liquidGlass(shape = shape)
    ) {
        content()
    }
}
