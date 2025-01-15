package com.example.passwordsapp.feature_pass.data.repository

import android.util.Log
import androidx.core.graphics.toColor
import com.example.passwordsapp.feature_pass.domain.model.PasswordWarning
import com.example.passwordsapp.feature_pass.domain.repository.PasswordCheckRepository
import com.example.passwordsapp.feature_pass.domain.usecase.NoteUseCases
import com.example.passwordsapp.feature_pass.domain.util.EncryptionManager
import com.example.passwordsapp.feature_pass.domain.util.checkPasswordBreach
import com.example.passwordsapp.feature_pass.domain.util.toColor
import com.nulabinc.zxcvbn.Zxcvbn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PasswordCheckRepositoryImpl @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val encryptionManager: EncryptionManager
) : PasswordCheckRepository {

    private val zxcvbn = Zxcvbn()

    override suspend fun getPasswordWarnings(): List<PasswordWarning> = withContext(Dispatchers.IO) {
        Log.d("PasswordCheckRepository", "getPasswordWarnings: Start")
        val notes = noteUseCases.getNotesUseCase().flowOn(Dispatchers.IO).first()
        val warnings = notes.mapNotNull { note ->
            try {
                val decryptedPassword = encryptionManager.decrypt(note.password, note.passwordIv)
                val passwordString = decryptedPassword.toString(Charsets.UTF_8)
                val analysisResult = zxcvbn.measure(passwordString)

                PasswordWarning(
                    title = note.title,
                    username = note.username,
                    password = passwordString,
                    score = analysisResult.score,
                    warning = analysisResult.feedback.warning.orEmpty(),
                    noteId = note.id,
                    suggestions = analysisResult.feedback.suggestions,
                    color = toColor(note.color) // yep that's cringe
                )
            } catch (e: Exception) {
                Log.e("PasswordCheckRepository", "getPasswordWarnings: Error processing note ${note.id}", e)
                null
            }
        }
        Log.d("PasswordCheckRepository", "getPasswordWarnings: End")
        warnings
    }

    override suspend fun checkPasswordForBreaches(): List<PasswordWarning> = withContext(Dispatchers.IO) {
        Log.d("PasswordCheckRepository", "checkPasswordForBreaches: Start")
        val notes = noteUseCases.getNotesUseCase().flowOn(Dispatchers.IO).first()
        val breaches = notes.mapNotNull { note ->
            try {
                val decryptedPassword = encryptionManager.decrypt(note.password, note.passwordIv)
                val passwordString = decryptedPassword.toString(Charsets.UTF_8)

                val breachCount = checkPasswordBreach(passwordString)

                if (breachCount > 0) {
                    PasswordWarning(
                        title = note.title,
                        username = note.username,
                        password = passwordString,
                        score = 0,
                        warning = "Password has been breached $breachCount times!",
                        suggestions = listOf("Change this password immediately!"),
                        noteId = note.id,
                        color = toColor(note.color)
                    )
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e("PasswordCheckRepository", "checkPasswordForBreaches: Error processing note ${note.id}", e)
                null
            }
        }
        Log.d("PasswordCheckRepository", "checkPasswordForBreaches: End")
        breaches
    }
}