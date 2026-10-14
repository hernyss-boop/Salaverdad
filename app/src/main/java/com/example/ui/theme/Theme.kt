package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CyanAccent,
    onPrimary = Slate950,
    primaryContainer = Slate800,
    onPrimaryContainer = CyanAccent,
    secondary = AmberAccent,
    onSecondary = Slate950,
    secondaryContainer = Slate850,
    onSecondaryContainer = AmberGlow,
    tertiary = PurpleAccent,
    onTertiary = Slate950,
    background = Slate950,
    onBackground = TextPrimary,
    surface = Slate900,
    onSurface = TextPrimary,
    surfaceVariant = Slate800,
    onSurfaceVariant = TextSecondary,
    outline = Slate700,
    error = RoseError,
    onError = TextPrimary
)

@Composable
fun OperacionVerdadTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
