package com.example.passwordsapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.passwordsapp.feature_pass.data.data_source.NoteDatabase
import com.example.passwordsapp.feature_pass.data.repository.NoteRepositoryImpl
import com.example.passwordsapp.feature_pass.data.repository.PasswordCheckRepositoryImpl
import com.example.passwordsapp.feature_pass.domain.repository.NoteRepository
import com.example.passwordsapp.feature_pass.domain.repository.PasswordCheckRepository
import com.example.passwordsapp.feature_pass.domain.usecase.AddNoteUseCase
import com.example.passwordsapp.feature_pass.domain.usecase.DeleteNoteUseCase
import com.example.passwordsapp.feature_pass.domain.usecase.GetNoteUseCase
import com.example.passwordsapp.feature_pass.domain.usecase.GetNotesUseCase
import com.example.passwordsapp.feature_pass.domain.usecase.NoteUseCases
import com.example.passwordsapp.feature_pass.domain.util.EncryptionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDatabase {
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun provideNoeUseCases(repository: NoteRepository): NoteUseCases {
        return NoteUseCases(
            getNotesUseCase = GetNotesUseCase(repository),
            deleteNoteUseCase = DeleteNoteUseCase(repository),
            addNoteUseCase = AddNoteUseCase(repository),
            getNoteUseCase = GetNoteUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun providePasswordCheckRepository(
        noteUseCases: NoteUseCases,
        encryptionManager: EncryptionManager,
    ): PasswordCheckRepository {
        return PasswordCheckRepositoryImpl(noteUseCases, encryptionManager)
    }

}