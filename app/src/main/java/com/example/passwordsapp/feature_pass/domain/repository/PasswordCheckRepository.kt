package com.example.passwordsapp.feature_pass.domain.repository

import com.example.passwordsapp.feature_pass.domain.model.PasswordWarning

interface PasswordCheckRepository {
    suspend fun getPasswordWarnings(): List<PasswordWarning>
    suspend fun checkPasswordForBreaches(): List<PasswordWarning>
}