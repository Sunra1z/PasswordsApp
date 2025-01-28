package com.example.passwordsapp.feature_pass.presentation.PasswordCheck

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordsapp.feature_pass.domain.model.PasswordWarning
import com.example.passwordsapp.feature_pass.domain.repository.PasswordCheckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordCheckViewModel @Inject constructor(
    private val passwordCheckRepository: PasswordCheckRepository
) : ViewModel() {

    private val _passwordWarnings = MutableStateFlow<List<PasswordWarning>>(emptyList())
    val passwordWarnings: StateFlow<List<PasswordWarning>> = _passwordWarnings

    private val _passwordLeaks = MutableStateFlow<List<PasswordWarning>>(emptyList())
    val passwordLeaks: StateFlow<List<PasswordWarning>> = _passwordLeaks

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

     fun loadPasswordData() {
        viewModelScope.launch {
            Log.d("PasswordCheckViewModel", "loadPasswordData: Start")
            _isLoading.value = true
            try {
                val warnings = passwordCheckRepository.getPasswordWarnings()
                val breaches = passwordCheckRepository.checkPasswordForBreaches()
                _passwordWarnings.value = warnings
                _passwordLeaks.value = breaches
            } catch (e: Exception) {
                Log.e("PasswordCheckViewModel", "Error loading password data", e)
            } finally {
                _isLoading.value = false
                Log.d("PasswordCheckViewModel", "loadPasswordData: End")
            }
        }
    }
}