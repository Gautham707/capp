package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = SwissRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFEEEE),
    onPrimaryContainer = SwissRedDark,
    secondary = SwissBlue,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEFF6FF),
    onSecondaryContainer = Color(0xFF1E40AF),
    tertiary = SwissGreen,
    onTertiary = Color.White,
    background = SwissBackgroundLight,
    onBackground = SwissInkPrimary,
    surface = SwissSurfaceLight,
    onSurface = SwissInkPrimary,
    surfaceVariant = SwissSurfaceVariantLight,
    onSurfaceVariant = SwissInkMuted,
    outline = SwissBorderLight,
    outlineVariant = SwissBorderStrong
)

private val DarkColorScheme = darkColorScheme(
    primary = SwissRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF450A0A),
    onPrimaryContainer = Color(0xFFFCA5A5),
    secondary = Color(0xFF38BDF8),
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF0C4A6E),
    onSecondaryContainer = Color(0xFFBAE6FD),
    tertiary = SwissGreen,
    onTertiary = Color.Black,
    background = SwissBackgroundDark,
    onBackground = SwissTextPrimaryDark,
    surface = SwissSurfaceDark,
    onSurface = SwissTextPrimaryDark,
    surfaceVariant = SwissSurfaceVariantDark,
    onSurfaceVariant = SwissTextMutedDark,
    outline = SwissBorderDark,
    outlineVariant = Color(0xFF64748B)
)

// Swiss Design Flat Geometry: Zero corner rounding (0dp) for pure modernist flat containers
val SwissShapes = Shapes(
    extraSmall = RoundedCornerShape(0.dp),
    small = RoundedCornerShape(0.dp),
    medium = RoundedCornerShape(0.dp),
    large = RoundedCornerShape(0.dp),
    extraLarge = RoundedCornerShape(0.dp)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = SwissShapes,
        content = content
    )
}
