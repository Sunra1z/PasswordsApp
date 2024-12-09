package com.example.passwordsapp.feature_pass.domain.usecase

import com.example.passwordsapp.feature_pass.domain.model.InvalidNoteException
import com.example.passwordsapp.feature_pass.domain.model.Note
import com.example.passwordsapp.feature_pass.domain.repository.NoteRepository

class AddNoteUseCase(
    private val repository: NoteRepository
) {

    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if (note.title.isBlank()){
            throw InvalidNoteException("The title can't be empty.")
        }
        repository.insertNote(note)
    }
}