package com.example.passwordsapp.feature_pass.domain.model

import androidx.compose.ui.graphics.Color

data class PasswordWarning(
    val title: String,
    val username: String,
    val password: String,
    val score: Int,
    val breaches: Int? = null,
    val warning: String,
    val isLeaked: Boolean,
    val color: Color,
    val suggestions: List<String>,
    val noteId: Int?
)
