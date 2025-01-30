package com.example.passwordsapp.feature_pass.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.passwordsapp.feature_pass.domain.util.generateRandomColor


@Entity
data class Note(
    val title: String,
    val username: String,
    val password: ByteArray,
    val timeStamp: Long,
    val passwordIv: ByteArray,
    val color: Int = generateRandomColor().toArgb(),
    val isFavorite: Boolean = false,
    val isLeaked: Boolean = false,
    val isWeak: Boolean = false,
    @PrimaryKey val id: Int? = null
)

class InvalidNoteException(message: String): Exception(message)
