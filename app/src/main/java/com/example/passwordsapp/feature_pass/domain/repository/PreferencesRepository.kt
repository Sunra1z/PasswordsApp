package com.example.passwordsapp.feature_pass.domain.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    val darkThemeEnabled: Flow<Boolean>
    val notificationEnabled: Flow<Boolean>
    suspend fun setDarkTheme(mode: Boolean)
    suspend fun setNotificationsEnabled(enabled: Boolean)
}