package com.example.passwordsapp.feature_pass.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.passwordsapp.ui.theme.savoyBlue

data class CarouselCardDataClass(
    val title: String,
    val subtext: String,
    val icon: ImageVector,
    val iconBackColor: Color,
    val onClick: () -> Unit
)
