package com.example.passwordsapp.feature_pass.domain.model

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

data class SettingButtons(
    val name: String,
    val description: String = "A button!",
    val category: String = "Default",
    val icon: ImageVector = Icons.Default.Info
)
