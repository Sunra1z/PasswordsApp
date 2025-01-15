package com.example.passwordsapp.feature_pass.presentation.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

const val USER_DATASTORE = "user_data"

val Context.preferenceDataStore : DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)

class DataStoreManager() {

    object PreferenceKeys {
        val DARK_THEME = booleanPreferencesKey("dark_theme_enabled") // True/False
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled") // True/False
        val SHOW_USERNAME = booleanPreferencesKey("show_username_enabled") // True/False
    }

}