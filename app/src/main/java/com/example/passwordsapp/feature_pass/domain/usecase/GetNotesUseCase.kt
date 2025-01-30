package com.example.passwordsapp.feature_pass.domain.usecase

import com.example.passwordsapp.feature_pass.domain.model.Note
import com.example.passwordsapp.feature_pass.domain.repository.NoteRepository
import com.example.passwordsapp.feature_pass.domain.util.NoteOrder
import com.example.passwordsapp.feature_pass.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCase(
    private val repository: NoteRepository
) {

    operator fun invoke(
        noteOrder: NoteOrder = NoteOrder.Title(OrderType.Descending)
    ): Flow<List<Note>> {
        return repository.getNotes().map { notes ->
            when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedWith(compareBy<Note> { !it.isFavorite }.thenBy { it.title.lowercase() })
                        is NoteOrder.Date -> notes.sortedWith(compareBy<Note> { !it.isFavorite }.thenBy { it.timeStamp })
                    }
                }

                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> notes.sortedWith(compareBy<Note> { !it.isFavorite }.thenByDescending { it.title.lowercase() })
                        is NoteOrder.Date -> notes.sortedWith(compareBy<Note> { !it.isFavorite }.thenByDescending { it.timeStamp })
                    }

                }

            }
        }
    }
}