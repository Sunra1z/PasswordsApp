package com.example.passwordsapp.feature_pass.data.repository

import android.util.Log
import androidx.core.graphics.toColor
import com.example.passwordsapp.feature_pass.domain.model.Note
import com.example.passwordsapp.feature_pass.domain.model.PasswordWarning
import com.example.passwordsapp.feature_pass.domain.repository.PasswordCheckRepository
import com.example.passwordsapp.feature_pass.domain.usecase.NoteUseCases
import com.example.passwordsapp.feature_pass.domain.util.EncryptionManager
import com.example.passwordsapp.feature_pass.domain.util.checkPasswordBreach
import com.example.passwordsapp.feature_pass.domain.util.toColor
import com.example.passwordsapp.ui.theme.redAlertColor
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
                val decryptedPassword = withContext(Dispatchers.Default) {
                    encryptionManager.decrypt(note.password, note.passwordIv)
                }

                val passwordString = decryptedPassword.toString(Charsets.UTF_8)
                val analysisResult = withContext(Dispatchers.Default) {
                    zxcvbn.measure(passwordString)
                }
                if (analysisResult.feedback.warning.isNotEmpty()){
                    val warning = PasswordWarning(
                        title = note.title,
                        username = note.username,
                        password = passwordString,
                        score = analysisResult.score,
                        warning = analysisResult.feedback.warning,
                        noteId = note.id,
                        suggestions = analysisResult.feedback.suggestions,
                        color = toColor(note.color)
                    )
                    noteUseCases.addNoteUseCase(
                        Note(
                            id = note.id,
                            title = note.title,
                            username = note.username,
                            password = note.password,
                            passwordIv = note.passwordIv,
                            timeStamp = note.timeStamp,
                            color = note.color,
                            isFavorite = note.isFavorite,
                            isWeak = true
                        )
                    )
                    Log.d("PasswordCheckRepository", "Note updated as Weak with ID: ${note.id}")
                    warning
                } else {
                    noteUseCases.addNoteUseCase(
                        Note(
                            id = note.id,
                            title = note.title,
                            username = note.username,
                            password = note.password,
                            passwordIv = note.passwordIv,
                            timeStamp = note.timeStamp,
                            color = note.color,
                            isFavorite = note.isFavorite,
                            isWeak = false
                        )
                    )
                    Log.d("PasswordCheckRepository", "Note updated with ID: ${note.id}")
                    null
                }
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
                val decryptedPassword = withContext(Dispatchers.Default) {
                    encryptionManager.decrypt(note.password, note.passwordIv)
                }

                val passwordString = decryptedPassword.toString(Charsets.UTF_8)
                val breachCount = withContext(Dispatchers.Default) {
                    checkPasswordBreach(passwordString)
                }

                if (breachCount > 0) {
                    val breach = PasswordWarning(
                        title = note.title,
                        username = note.username,
                        password = passwordString,
                        score = 0,
                        warning = "Password has been breached $breachCount times!",
                        suggestions = listOf("Change this password immediately!"),
                        noteId = note.id,
                        color = toColor(note.color)
                    )
                    noteUseCases.addNoteUseCase(
                        Note(
                            id = note.id,
                            title = note.title,
                            username = note.username,
                            password = note.password,
                            passwordIv = note.passwordIv,
                            timeStamp = note.timeStamp,
                            color = note.color,
                            isFavorite = note.isFavorite,
                            isLeaked = true
                        )
                    )
                    Log.d("PasswordCheckRepository", "Note updated as LEAKED with ID: ${note.id}")
                    breach
                } else {
                    noteUseCases.addNoteUseCase(
                        Note(
                            id = note.id,
                            title = note.title,
                            username = note.username,
                            password = note.password,
                            passwordIv = note.passwordIv,
                            timeStamp = note.timeStamp,
                            color = note.color,
                            isFavorite = note.isFavorite,
                            isLeaked = false
                        )
                    )
                    Log.d("PasswordCheckRepository", "Note updated as FALSE with ID: ${note.id}")
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

    override suspend fun getPasswordWarningsForSinglePassword(password: String): PasswordWarning? {
        val analysisResult = zxcvbn.measure(password)
        return if (analysisResult.feedback.warning.isNotEmpty()) {
            PasswordWarning(
                title = "",
                username = "",
                password = password,
                score = analysisResult.score,
                warning = analysisResult.feedback.warning,
                noteId = 0,
                suggestions = analysisResult.feedback.suggestions,
                color = redAlertColor
            )
        } else {
            null
        }
    }

    override suspend fun checkPasswordForBreachesForSinglePassword(password: String): PasswordWarning? {
        val breachCount = checkPasswordBreach(password)
        return if (breachCount > 0) {
            PasswordWarning(
                title = "",
                username = "",
                password = password,
                score = 0,
                warning = "Password has been breached $breachCount times!",
                suggestions = listOf("Change this password immediately!"),
                noteId = 0,
                color = redAlertColor
            )
        } else {
            null
        }
    }
}
