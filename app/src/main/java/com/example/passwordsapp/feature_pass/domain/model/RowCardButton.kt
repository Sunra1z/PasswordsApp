package com.example.passwordsapp.feature_pass.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.passwordsapp.ui.theme.savoyBlue

data class RowCardButton(
    val title: String = "Title text sample",
    val subtext: String = "Subtext sample",
    val icon: ImageVector = Icons.Default.Info,
    val iconColor: Color = savoyBlue,
    val action: String = "no"
)
