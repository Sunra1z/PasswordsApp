package com.example.passwordsapp.feature_pass.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.example.passwordsapp.feature_pass.domain.repository.PreferencesRepository
import com.example.passwordsapp.feature_pass.presentation.util.DataStoreManager
import com.example.passwordsapp.feature_pass.presentation.util.preferenceDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesRepository {

    // Retrieving theme preference (default: System)
    override val darkThemeEnabled: Flow<Boolean> = context.preferenceDataStore.data
        .map { preferences ->
            preferences[DataStoreManager.PreferenceKeys.DARK_THEME] ?: false
        }

    // Retrieving notifications preference (default: True)
    override val notificationEnabled: Flow<Boolean> = context.preferenceDataStore.data
        .map { preferences ->
            preferences[DataStoreManager.PreferenceKeys.NOTIFICATIONS_ENABLED] ?: true
        }

    override val hideUsernameEnabled: Flow<Boolean> = context.preferenceDataStore.data
        .map { preferences ->
            preferences[DataStoreManager.PreferenceKeys.SHOW_USERNAME] ?: true
        }

    override val alertTime: Flow<String> = context.preferenceDataStore.data
        .map { preferences ->
            preferences[DataStoreManager.PreferenceKeys.ALERT_TIME] ?: "24h"
        }

    // Saving theme mode preference
    override suspend fun setDarkTheme(mode: Boolean){
        context.preferenceDataStore.edit { preferences ->
            preferences[DataStoreManager.PreferenceKeys.DARK_THEME] = mode
        }
    }

    // Saving notifications preference
    override suspend fun setNotificationsEnabled(enabled: Boolean){
        context.preferenceDataStore.edit { preferences ->
            preferences[DataStoreManager.PreferenceKeys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    override suspend fun setHideUsernameEnabled(enabled: Boolean) {
        context.preferenceDataStore.edit { preferences ->
            preferences[DataStoreManager.PreferenceKeys.SHOW_USERNAME] = enabled
        }
    }

    override suspend fun setAlertTime(time: String) {
        context.preferenceDataStore.edit { preferences ->
            preferences[DataStoreManager.PreferenceKeys.ALERT_TIME] = time
        }
    }

}