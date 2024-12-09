package com.example.passwordsapp.feature_pass.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.passwordsapp.feature_pass.domain.model.Note

@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase: RoomDatabase() {

    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}