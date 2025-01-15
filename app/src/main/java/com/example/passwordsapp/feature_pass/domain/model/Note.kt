package com.example.passwordsapp.feature_pass.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.passwordsapp.feature_pass.domain.util.generateRandomColor


@Entity
data class Note(
    val title: String,
    val username: ByteArray,
    val password: ByteArray,
    val timeStamp: Long,
    val usernameIv: ByteArray,
    val passwordIv: ByteArray,
    val color: Int = generateRandomColor().toArgb(),
    @PrimaryKey val id: Int? = null
)

class InvalidNoteException(message: String): Exception(message)
