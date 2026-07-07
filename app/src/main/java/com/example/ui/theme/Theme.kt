package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val OperationalDarkColorScheme = darkColorScheme(
    primary = AccentLight,
    onPrimary = Color.Black,
    primaryContainer = ColorAiBlue,
    onPrimaryContainer = Color.White,
    secondary = ColorAiPurple,
    onSecondary = Color.White,
    secondaryContainer = BgCardSecondary,
    onSecondaryContainer = TextPrimary,
    background = BgMain,
    onBackground = TextPrimary,
    surface = BgCard,
    onSurface = TextPrimary,
    surfaceVariant = GlassBg,
    onSurfaceVariant = TextPrimary,
    outline = BorderColor,
    outlineVariant = GlassBorder,
    error = ColorCritical,
    onError = Color.White,
    errorContainer = Color(0xFF450a0a),
    onErrorContainer = Color(0xFFfca5a5)
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp), // Requested 20px rounded corners
    large = RoundedCornerShape(24.dp)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
      colorScheme = OperationalDarkColorScheme,
      typography = Typography,
      shapes = AppShapes,
      content = content
  )
}
