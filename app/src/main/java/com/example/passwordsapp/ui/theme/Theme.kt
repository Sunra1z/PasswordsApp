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
    primary = tropicalIndigo,
    secondary = savoyBlue,
    tertiary = Pink80,
    background = Color(0xFF303030),
    surface = Color(0xFF414141),
    onSurface = Color(0xFFFFFFFF),
    error = Color(0xFFFF6659),
    onPrimary = Color(0xFFF8F7F7),
)

private val LightColorScheme = lightColorScheme(
    primary = savoyBlue,
    secondary = tropicalIndigo,
    tertiary = Pink40,
    background = Color(0xFFF4F4F8),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF000000),
    error = Color(0xFFD32F2F),
    onPrimary = Color(0xFFFFFFFF),
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