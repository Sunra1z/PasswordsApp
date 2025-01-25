package com.example.passwordsapp.feature_pass.presentation.add_note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.passwordsapp.feature_pass.presentation.notes.components.NoteIcon

@Composable
fun ColorPickerDialog(
    initialHue: Float,
    initialSaturation: Float,
    initialValue: Float,
    title: String,
    onDismissRequest: () -> Unit,
    onColorSelected: (Float, Float, Float) -> Unit
) {
    val openDialog = remember { mutableStateOf(true) }
    val hsv = remember {
        mutableStateOf(Triple(initialHue, initialSaturation, initialValue))
    }
    val backgroundColor = remember(hsv.value) {
        mutableStateOf(Color.hsv(hsv.value.first, hsv.value.second, hsv.value.third))
    }

    if (openDialog.value) {
        AlertDialog(
            containerColor = MaterialTheme.colorScheme.surface,
            onDismissRequest = {
                openDialog.value = false
                onDismissRequest()
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        openDialog.value = false
                        onDismissRequest()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "CloseAlert"
                        )
                    }
                    NoteIcon(
                        noteTitle = title,
                        fontSize = 24.sp,
                        backgroundColor = backgroundColor.value,
                        onClick = { },
                        modifier = Modifier.size(64.dp)
                    )
                    IconButton(onClick = {
                        openDialog.value = false
                        onColorSelected(hsv.value.first, hsv.value.second, hsv.value.third)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "SaveColor",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    SatValPanel(hue = hsv.value.first) { sat, value ->
                        hsv.value = Triple(hsv.value.first, sat, value)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    HueBar { hue ->
                        hsv.value = Triple(hue, hsv.value.second, hsv.value.third)
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}