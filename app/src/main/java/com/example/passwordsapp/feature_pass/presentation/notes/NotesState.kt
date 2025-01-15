package com.example.passwordsapp.feature_pass.presentation.notes

import com.example.passwordsapp.feature_pass.domain.model.Note
import com.example.passwordsapp.feature_pass.domain.util.NoteOrder
import com.example.passwordsapp.feature_pass.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val isLoading: Boolean = true,
    val hideUsername: Boolean = true
)
