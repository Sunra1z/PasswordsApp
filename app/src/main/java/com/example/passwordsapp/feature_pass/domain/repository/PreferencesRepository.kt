package com.example.passwordsapp.feature_pass.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface PreferencesRepository {
    val darkThemeEnabled: Flow<Boolean>
    val notificationEnabled: Flow<Boolean>
    val hideUsernameEnabled: Flow<Boolean>
    val alertTime: Flow<String>
    suspend fun setDarkTheme(mode: Boolean)
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setHideUsernameEnabled(enabled: Boolean)
    suspend fun setAlertTime(time: String)
}