package com.example.passwordsapp.feature_pass.presentation.add_note

data class NoteTextFieldState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)