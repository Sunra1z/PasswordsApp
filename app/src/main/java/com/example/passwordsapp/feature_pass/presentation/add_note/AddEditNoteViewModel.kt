package com.example.passwordsapp.feature_pass.presentation.add_note

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordsapp.feature_pass.domain.model.InvalidNoteException
import com.example.passwordsapp.feature_pass.domain.model.Note
import com.example.passwordsapp.feature_pass.domain.repository.NoteRepository
import com.example.passwordsapp.feature_pass.domain.usecase.CheckPasswordForBreachesUseCase
import com.example.passwordsapp.feature_pass.domain.usecase.CheckPasswordForWarningsUseCase
import com.example.passwordsapp.feature_pass.domain.usecase.NoteUseCases
import com.example.passwordsapp.feature_pass.domain.util.EncryptionManager
import com.example.passwordsapp.feature_pass.domain.util.generateRandomColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val encryptionManager: EncryptionManager,
    private val CheckPasswordForBreachesUseCase: CheckPasswordForBreachesUseCase,
    private val CheckPasswordForWarningsUseCase: CheckPasswordForWarningsUseCase
) : ViewModel() {

    private val _noteTitle = mutableStateOf(NoteTextFieldState(
        hint = "Enter title..."
    ))
    val noteTitle: State<NoteTextFieldState> = _noteTitle

    private val _usernameContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter username..."
    ))
    val usernameContent: State<NoteTextFieldState> = _usernameContent

    private val _passContent = mutableStateOf(NoteTextFieldState(
        hint = "Enter password"
    ))
    val passContent: State<NoteTextFieldState> = _passContent

    val selectedColor = mutableStateOf(generateRandomColor())

    val isFavorite = mutableStateOf(false)
    val isWeak = mutableStateOf(false)
    val isLeaked = mutableStateOf(false)

    fun onColorSelected(hue: Float, saturation: Float, value: Float) {
        viewModelScope.launch {
            val color = withContext(Dispatchers.Default) {
                Color.hsv(hue, saturation, value)
            }
            selectedColor.value = color
        }
    }

    fun toggleFavorite(){
        isFavorite.value = !isFavorite.value
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null


    fun loadNoteById(noteId: Int) {
        viewModelScope.launch {
            noteUseCases.getNoteUseCase(noteId)?.also { note ->
                currentNoteId = note.id
                val decryptedPassword = encryptionManager.decrypt(
                    note.password,
                    note.passwordIv
                )
                _noteTitle.value = noteTitle.value.copy(
                    text = note.title,
                    isHintVisible = false,
                )
                _usernameContent.value = usernameContent.value.copy(
                    text = note.username,
                    isHintVisible = false,
                )
                _passContent.value = passContent.value.copy(
                    text = String(decryptedPassword),
                    isHintVisible = false,
                )
                selectedColor.value = Color(note.color)
                isFavorite.value = note.isFavorite
                isWeak.value = note.isWeak
                isLeaked.value = note.isLeaked
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredUsername -> {
                _usernameContent.value = usernameContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangeUsernameFocus -> {
                _usernameContent.value = usernameContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            usernameContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.EnteredPassword -> {
                _passContent.value = passContent.value.copy(
                    text = event.value
                )
            }
            is AddEditNoteEvent.ChangePasswordFocus -> {
                _passContent.value = passContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            passContent.value.text.isBlank()
                )
            }
            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        if (usernameContent.value.text.isBlank() || passContent.value.text.isBlank()) {
                            _eventFlow.emit(UiEvent.ShowSnackBar("Username and password cannot be empty"))
                            return@launch
                        }
                        val username = usernameContent.value.text
                        val (encryptedPassword, passwordIv) = encryptionManager.encrypt(passContent.value.text.toByteArray())

                        // Perform password checks in the background
                        val passwordWarnings = CheckPasswordForWarningsUseCase(passContent.value.text)
                        val passwordBreaches = CheckPasswordForBreachesUseCase(passContent.value.text)

                        isWeak.value = passwordWarnings != null
                        isLeaked.value = passwordBreaches != null

                        // Add the note with the password check results
                        noteUseCases.addNoteUseCase(
                            Note(
                                title = noteTitle.value.text,
                                username = username,
                                password = encryptedPassword,
                                passwordIv = passwordIv,
                                timeStamp = System.currentTimeMillis(),
                                id = currentNoteId,
                                color = selectedColor.value.toArgb(),
                                isFavorite = isFavorite.value,
                                isWeak = isWeak.value,
                                isLeaked = isLeaked.value
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                        Log.d("AddEditNoteViewModel", "Note checked and added with ID: ${currentNoteId}")
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }
}