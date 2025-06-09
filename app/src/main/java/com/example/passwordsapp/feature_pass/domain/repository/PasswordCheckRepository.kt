package com.example.passwordsapp.feature_pass.domain.repository

import com.example.passwordsapp.feature_pass.domain.model.PasswordWarning

interface PasswordCheckRepository {
    suspend fun checkPasswordForBreaches(): List<PasswordWarning>
    suspend fun checkPasswordsHealth(): List<PasswordWarning>
    suspend fun getPasswordWarningsForSinglePassword(password: String): PasswordWarning?
    suspend fun checkPasswordForBreachesForSinglePassword(password: String): PasswordWarning?
    suspend fun checkPasswordHealth(password: String): PasswordWarning?
}