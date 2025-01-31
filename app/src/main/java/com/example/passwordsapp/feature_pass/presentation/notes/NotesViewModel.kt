package com.example.passwordsapp.feature_pass.presentation.notes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordsapp.feature_pass.domain.model.Note
import com.example.passwordsapp.feature_pass.domain.repository.PasswordCheckRepository
import com.example.passwordsapp.feature_pass.domain.repository.PreferencesRepository
import com.example.passwordsapp.feature_pass.domain.usecase.NoteUseCases
import com.example.passwordsapp.feature_pass.domain.util.NoteOrder
import com.example.passwordsapp.feature_pass.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private val _totalNotesCount = mutableStateOf(0)
    val totalNotesCount: State<Int> = _totalNotesCount

    private val _totalLeaks = mutableStateOf(0)
    val totalLeaks: State<Int> = _totalLeaks

    private val _totalWarnings = mutableStateOf(0)
    val totalWarnings: State<Int> = _totalWarnings

    private var recentlyDeletedNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
        observePreferences()
    }

    fun onEvent(event: NotesEvent){
        when(event){
            is NotesEvent.Order -> {
                if(state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType)
                {
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNoteUseCase(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNoteUseCase(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }

            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            noteUseCases.getNotesUseCase(noteOrder)
                .onEach { notes ->
                    _state.value = state.value.copy(
                        notes = notes,
                        noteOrder = noteOrder,
                        isLoading = false
                    )
                    _totalNotesCount.value = notes.size
                    _totalLeaks.value = notes.count { it.isLeaked }
                    _totalWarnings.value = notes.count { it.isWeak }
                }
                .launchIn(this)
        }
    }



    private fun observePreferences(){
        viewModelScope.launch {
            preferencesRepository.hideUsernameEnabled.collect{ hideUsernameEnabled ->
                _state.value = state.value.copy(
                    hideUsername = hideUsernameEnabled
                )

            }
        }
    }


}