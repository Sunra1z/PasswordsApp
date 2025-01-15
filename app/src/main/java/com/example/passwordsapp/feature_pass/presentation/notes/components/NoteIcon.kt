package com.example.passwordsapp.feature_pass.presentation.notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun NoteIcon(
    noteTitle: String,
    modifier: Modifier,
    backgroundColor: Color
) {
    val firstLetter = noteTitle.firstOrNull()?.toString()?.uppercase() ?: ""
    Box(
        modifier = modifier
            .size(36.dp)
            .background(backgroundColor, shape = RoundedCornerShape(CornerSize(18.dp))),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = firstLetter,
            style = MaterialTheme.typography.labelLarge.copy(color = Color.White)
        )
    }
}
