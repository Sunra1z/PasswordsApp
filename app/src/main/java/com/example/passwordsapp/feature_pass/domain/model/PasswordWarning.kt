package com.example.passwordsapp.feature_pass.domain.model

import androidx.compose.ui.graphics.Color

data class PasswordWarning(
    val title: String,
    val username: String,
    val password: String,
    val score: Int,
    val warning: String,
    val color: Color,
    val suggestions: List<String>,
    val noteId: Int?
)
