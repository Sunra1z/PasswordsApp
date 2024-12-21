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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            noteUseCases.getNotesUseCase()
                .flowOn(Dispatchers.IO) // Ensure Flow operates on IO dispatcher
                .collect { notes ->
                    val analyzedWarnings = analyzeNotePasswords(notes) // Process the list of notes
                    _passwordWarnings.value = analyzedWarnings
                }
        }
    }

    private suspend fun analyzeNotePasswords(notes: List<Note>): List<PasswordWarning> {
        return notes.mapNotNull { note ->
            try {
                // Perform decryption and analysis on a background thread
                withContext(Dispatchers.IO) {
                    val decryptedUsername = encryptionManager.decrypt(note.username, note.usernameIv)
                    val decryptedPassword = encryptionManager.decrypt(note.password, note.passwordIv)

                    val passwordString = decryptedPassword.toString(Charsets.UTF_8) // Convert to String
                    val analysisResult = zxcvbn.measure(passwordString)

                    PasswordWarning(
                        title = note.title,
                        username = decryptedUsername.toString(Charsets.UTF_8),
                        password = passwordString,
                        score = analysisResult.score,
                        warning = analysisResult.feedback.warning.orEmpty(),
                        suggestions = analysisResult.feedback.suggestions
                    )
                }
            } catch (e: Exception) {
                // Log and skip this note if an exception occurs
                Log.e(
                    "PasswordCheckViewModel",
                    "Error analyzing password for note: ${note.title}",
                    e
                )
                null
            }
        }
    }
}
