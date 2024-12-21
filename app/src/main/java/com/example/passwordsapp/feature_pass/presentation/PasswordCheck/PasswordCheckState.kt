package com.example.passwordsapp.feature_pass.presentation.PasswordCheck

import com.example.passwordsapp.feature_pass.domain.model.PasswordWarning

data class PasswordCheckState(
    val isLoading: Boolean = true,
    val passwordWarnings: List<PasswordWarning> = emptyList()
)