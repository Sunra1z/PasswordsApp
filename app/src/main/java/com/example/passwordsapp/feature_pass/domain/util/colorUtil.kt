package com.example.passwordsapp.feature_pass.domain.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.random.Random

fun generateRandomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1.0f
    )
}

fun fromColor(color: Color): Int{
    return color.toArgb()
}

fun toColor(colorInt: Int): Color {
    return Color(colorInt)
}