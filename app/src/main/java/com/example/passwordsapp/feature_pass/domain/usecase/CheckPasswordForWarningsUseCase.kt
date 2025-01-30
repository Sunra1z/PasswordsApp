package com.example.passwordsapp.feature_pass.domain.usecase

import com.example.passwordsapp.feature_pass.domain.model.PasswordWarning
import com.example.passwordsapp.feature_pass.domain.repository.PasswordCheckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckPasswordForWarningsUseCase @Inject constructor(
    private val passwordCheckRepository: PasswordCheckRepository
) {
    suspend operator fun invoke(password: String): PasswordWarning? {
        return withContext(Dispatchers.IO){
            passwordCheckRepository.getPasswordWarningsForSinglePassword(password)
        }
    }
}