package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val ElegantDarkColorScheme = darkColorScheme(
    primary = AccentLight,
    onPrimary = AccentDark,
    primaryContainer = AccentLight,
    onPrimaryContainer = AccentDark,
    secondary = BgCardSecondary,
    onSecondary = TextPrimary,
    secondaryContainer = BgCard,
    onSecondaryContainer = TextPrimary,
    background = BgMain,
    onBackground = TextPrimary,
    surface = BgCard,
    onSurface = TextPrimary,
    surfaceVariant = BgCardSecondary,
    onSurfaceVariant = TextPrimary,
    outline = BorderColor,
    outlineVariant = BorderColor,
    error = Color(0xFFCF6679),
    onError = Color.Black
)

private val AppShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(28.dp)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
      colorScheme = ElegantDarkColorScheme,
      typography = Typography,
      shapes = AppShapes,
      content = content
  )
}
