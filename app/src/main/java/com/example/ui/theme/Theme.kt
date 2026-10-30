package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MerddColorScheme = darkColorScheme(
  primary = ElectricBlue,
  onPrimary = Color.White,
  primaryContainer = ElectricBlueDark,
  onPrimaryContainer = Color.White,
  secondary = CyanAccent,
  onSecondary = Color.Black,
  secondaryContainer = ObsidianSurfaceVariant,
  onSecondaryContainer = CyanAccent,
  tertiary = NeonPurple,
  onTertiary = Color.White,
  background = ObsidianBackground,
  onBackground = TextPrimary,
  surface = ObsidianSurface,
  onSurface = TextPrimary,
  surfaceVariant = ObsidianSurfaceVariant,
  onSurfaceVariant = TextSecondary,
  outline = ObsidianCardBorder,
  error = NeonRed,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Forced dark obsidian cyber theme for Merdd Lab
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = MerddColorScheme,
    typography = Typography,
    content = content
  )
}
