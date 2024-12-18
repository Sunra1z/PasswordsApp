package com.example.passwordsapp.feature_pass.domain

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordsapp.feature_pass.domain.model.Note
import com.example.passwordsapp.feature_pass.domain.model.PasswordWarning
import com.example.passwordsapp.feature_pass.domain.repository.NoteRepository
import com.example.passwordsapp.feature_pass.domain.usecase.NoteUseCases
import com.example.passwordsapp.feature_pass.domain.util.EncryptionManager
import com.nulabinc.zxcvbn.Zxcvbn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PasswordCheckViewModel @Inject constructor (
    private val repository: NoteRepository,
    private val noteUseCases: NoteUseCases,
    private val encryptionManager: EncryptionManager
) : ViewModel() {

    private val zxcvbn = Zxcvbn()

    private val _passwordWarnings = MutableStateFlow<List<PasswordWarning>>(emptyList())
    val passwordWarnings: StateFlow<List<PasswordWarning>> = _passwordWarnings

    fun loadNotes() {
        viewModelScope.launch {
            noteUseCases.getNotesUseCase().collect { notes ->
                val analyzedWarnings = notes.mapNotNull { note ->
                    try {
                        analyzeNotePassword(note)
                    } catch (e: Exception) {
                        Log.e("PasswordCheckViewModel", "Error decrypting note: ${note.title}", e)
                        null // Skip this note if decryption fails
                    }
                }
                _passwordWarnings.value = analyzedWarnings
            }
        }
    }



    private fun analyzeNotePassword(note: Note): PasswordWarning {
        val decryptedUsername = encryptionManager.decrypt(note.username, note.usernameIv)
        val decryptedPassword = encryptionManager.decrypt(note.password, note.passwordIv)

        val passwordString = String(decryptedPassword)
        val analysisResult = zxcvbn.measure(passwordString)

        return PasswordWarning(
            title = note.title,
            username = String(decryptedUsername),
            password = passwordString,
            score = analysisResult.score,
            warning = analysisResult.feedback.warning.orEmpty(),
            suggestions = analysisResult.feedback.suggestions
        )
    }


}