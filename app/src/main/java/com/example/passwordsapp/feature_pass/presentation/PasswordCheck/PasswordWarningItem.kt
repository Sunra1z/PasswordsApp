package com.example.passwordsapp.feature_pass.presentation.PasswordCheck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.passwordsapp.feature_pass.domain.model.PasswordWarning
import com.example.passwordsapp.feature_pass.presentation.notes.components.NoteIcon

@Composable
fun PasswordWarningItem(
    warning: PasswordWarning,
    modifier: Modifier = Modifier,
    onOpenNote: (Int?) -> Unit
) {
    if (warning.score < 4) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp), // Reduced padding
            colors = CardDefaults.cardColors(
                containerColor = when (warning.score) {
                    0, 1 -> Color.Red.copy(alpha = 0.1f) // Weak password
                    2, 3 -> Color.Yellow.copy(alpha = 0.1f) // Moderate password
                    else -> Color.Green.copy(alpha = 0.1f) // Strong password
                }
            )
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "warning",
                    modifier = modifier
                        .padding(4.dp) // Reduced padding
                        .size(48.dp) // Reduced size
                        .align(Alignment.CenterVertically)
                )
                Column(
                    modifier = Modifier
                        .padding(4.dp) // Reduced padding
                ) {
                    Text(
                        text = warning.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (warning.warning.isNotEmpty()) {
                        Text(
                            text = "Warning: ${warning.warning}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red
                        )
                    }
                    Button(onClick = {
                        onOpenNote(warning.noteId)
                    }) {
                        Text("Change Password")
                    }
                    if (warning.suggestions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp)) // Reduced spacing
                        warning.suggestions.forEach { suggestion ->
                            Text(
                                text = "- $suggestion",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 4.dp) // Reduced padding
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PasswordWarningItemPreview() {
    val sampleWarning = PasswordWarning(
        title = "Example Note",
        username = "example_user",
        password = "weakpassword123",
        score = 1,
        warning = "Your password is too weak.",
        suggestions = listOf(
            "Use at least one special character.",
            "Make your password longer."
        ),
        noteId = 1
    )
}
