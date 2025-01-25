package com.example.passwordsapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(200, 191, 255),
    onPrimary = Color(48, 40, 95),
    primaryContainer = Color(70, 63, 119),
    onPrimaryContainer = Color(229, 222, 255),
    secondary = Color(201, 195, 220),
    onSecondary = Color(49, 46, 65),
    secondaryContainer = Color(71, 68, 89),
    onSecondaryContainer = Color(229, 223, 249),
    tertiary = Color(236, 184, 206),
    onTertiary = Color(72, 37, 54),
    tertiaryContainer = Color(97, 59, 77),
    background = Color(0xFF303030),
    surface = Color(0xFF414141),
    surfaceBright = Color(58, 56, 62),
    surfaceDim = Color(20, 19, 24),
    surfaceContainer = Color(32, 31, 37),
    surfaceContainerLow = Color(28, 27, 32),
    surfaceContainerLowest = Color(14, 14, 19),
    surfaceContainerHigh = Color(42, 41, 47),
    surfaceContainerHighest = Color(53, 52, 58),
    onSurface = Color(229, 225, 233),
    onSurfaceVariant = Color(201, 197, 208),
    outline = Color(146, 143, 153),
    outlineVariant = Color(72, 70, 79),
    error = Color(255, 180, 171),
    errorContainer = Color(147, 0, 10),
    onError = Color(105, 0, 5),
    onErrorContainer = Color(255, 218, 214),
    inversePrimary = Color(94, 87, 145),
    inverseSurface = Color(229, 225, 233),
    inverseOnSurface = Color(49, 48, 54)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(94, 87, 145),
    onPrimary = Color(255, 255, 255),
    primaryContainer = Color(229, 222, 255),
    onPrimaryContainer = Color(26, 18, 73),
    secondary = Color(95, 92, 113),
    onSecondary = Color(255, 255, 255),
    secondaryContainer = Color(229, 223, 249),
    onSecondaryContainer = Color(28, 25, 43),
    tertiary = Color(123, 82, 101),
    onTertiary = Color(255, 255, 255),
    tertiaryContainer = Color(255, 216, 231),
    background = Color(0xFFF3F1F5),
    surface = Color(252, 248, 255),
    surfaceBright = Color(252, 248, 255),
    surfaceDim = Color(221, 216, 224),
    surfaceContainer = Color(241, 236, 244),
    surfaceContainerLow = Color(247, 242, 250),
    surfaceContainerLowest = Color(255, 255, 255),
    surfaceContainerHigh = Color(235, 230, 239),
    surfaceContainerHighest = Color(229, 225, 233),
    onSurface = Color(28, 27, 32),
    onSurfaceVariant = Color(120, 118, 127),
    outline = Color(146, 143, 153),
    outlineVariant = Color(120, 118, 127),
    error = Color(186, 26, 26),
    errorContainer = Color(255, 218, 214),
    onErrorContainer = Color(65, 0, 2),
    onError = Color(255, 255, 255),
    inversePrimary = Color(200, 191, 255),
    inverseSurface = Color(49, 48, 54),
    inverseOnSurface = Color(244, 239, 247)
)


@Composable
fun PasswordsAppTheme(
    darkTheme: Boolean,
    content: @Composable() () -> Unit
) {
    val colors = if (!darkTheme) {
        LightColorScheme
    } else {
        DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}