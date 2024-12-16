package com.example.passwordsapp.feature_pass.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object NotesScreen: Screen("notes_screen", "Home", Icons.Default.Home)
    object AddEditNoteScreen: Screen("add_edit_note_screen", "AddNoteScreen", Icons.Default.Home)
    object PasswordCheckScreen : Screen("password_check", "Check-up", Icons.Default.CheckCircle)
    object SettingsScreen : Screen("settings", "Settings", Icons.Default.Settings)
}