package com.example.passwordsapp.feature_pass.presentation.notes

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordsapp.feature_pass.domain.repository.PreferencesRepository
import com.example.passwordsapp.feature_pass.domain.usecase.NoteUseCases
import com.example.passwordsapp.feature_pass.domain.util.EncryptionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileWriter
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val encryptionManager: EncryptionManager,
    private val repository: PreferencesRepository
) : ViewModel() {

    // Preferences as StateFlow
    val darkThemeEnabled: StateFlow<Boolean> = repository.darkThemeEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val notificationsEnabled: StateFlow<Boolean> = repository.notificationEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val hideUsernameEnabled: StateFlow<Boolean> = repository.hideUsernameEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun exportDataAsCSV(context: Context){
        viewModelScope.launch(Dispatchers.IO){
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
            val csvFile = File(downloadsDir, "passwords.csv")
            val notes = noteUseCases.getNotesUseCase().flowOn(Dispatchers.IO).first()
            try {
                FileWriter(csvFile).use { writer ->
                    writer.append("Title,Username,Password\n")
                    notes.forEach { note ->
                        val decryptedPassword = encryptionManager.decrypt(
                            iv = note.password,
                            encryptedBytes = note.passwordIv
                        ).toString(Charsets.UTF_8)
                        writer.append("${note.title},${note.username},$decryptedPassword\n")
                    }
                }
                withContext(Dispatchers.Main){
                    val toast = Toast.makeText(context, "File exported successfully!", Toast.LENGTH_SHORT)
                    toast.show()
                }
                Log.d("ExportAsCSV", "File successfully created")
                } catch (e: IOException){
                    e.printStackTrace()
                Log.e("ExportAsCSV", "Something went wrong")
                }
            }
        }

    fun setThemeMode(mode: Boolean){
        viewModelScope.launch {
            repository.setDarkTheme(mode)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean){
        viewModelScope.launch {
            repository.setNotificationsEnabled(enabled)
        }
    }

    fun setUsernameHideEnabled(enabled: Boolean){
        viewModelScope.launch {
            repository.setHideUsernameEnabled(enabled)
        }
    }




    }
