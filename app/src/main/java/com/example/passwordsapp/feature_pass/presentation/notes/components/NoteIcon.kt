package com.example.passwordsapp.feature_pass.presentation.notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun NoteIcon(
    noteTitle: String,
    modifier: Modifier,
    onClick: () -> Unit,
    backgroundColor: Color,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val firstLetter = noteTitle.firstOrNull()?.toString()?.uppercase() ?: ""
    Box(
        modifier = modifier
            .size(36.dp)
            .background(backgroundColor, shape = CircleShape)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = firstLetter,
            style = MaterialTheme.typography.labelLarge.copy(color = Color.White),
            fontSize = fontSize
        )
    }
}
