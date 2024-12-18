package com.example.passwordsapp.feature_pass.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Note(
    val title: String,
    val username: ByteArray,
    val password: ByteArray,
    val timeStamp: Long,
    val usernameIv: ByteArray,
    val passwordIv: ByteArray,
    @PrimaryKey val id: Int? = null
)

class InvalidNoteException(message: String): Exception(message)
