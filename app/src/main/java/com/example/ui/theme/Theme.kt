package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NextGenVfxColorScheme = darkColorScheme(
    primary = ElectricBlue,
    onPrimary = TextWhite,
    primaryContainer = SecondaryVfxBackground,
    onPrimaryContainer = NeonCyan,
    secondary = NeonCyan,
    onSecondary = DeepVfxBackground,
    secondaryContainer = VfxCardSurface,
    onSecondaryContainer = TextWhite,
    tertiary = VfxGlowCyan,
    onTertiary = DeepVfxBackground,
    background = DeepVfxBackground,
    onBackground = TextWhite,
    surface = VfxCardSurface,
    onSurface = TextWhite,
    surfaceVariant = SecondaryVfxBackground,
    onSurfaceVariant = TextMuted,
    outline = VfxBorderGlow.copy(alpha = 0.4f),
    outlineVariant = ElectricBlue.copy(alpha = 0.2f),
    error = NeonRed,
    onError = TextWhite
)

@Composable
fun NextGenTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NextGenVfxColorScheme,
        typography = Typography,
        content = content
    )
}
