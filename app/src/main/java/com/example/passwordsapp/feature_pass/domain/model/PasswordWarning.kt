package com.example.passwordsapp.feature_pass.domain.model

data class PasswordWarning(
    val title: String,
    val username: String,
    val password: String,
    val score: Int,
    val warning: String,
    val suggestions: List<String>,
    val noteId: Int?
)
