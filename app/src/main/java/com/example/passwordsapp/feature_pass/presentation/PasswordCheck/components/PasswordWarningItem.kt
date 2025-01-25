package com.example.passwordsapp.feature_pass.presentation.PasswordCheck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.twotone.Error
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import com.example.passwordsapp.ui.theme.greenAlertColor
import com.example.passwordsapp.ui.theme.redAlertColor
import com.example.passwordsapp.ui.theme.savoyBlue
import com.example.passwordsapp.ui.theme.yellowAlertColor

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
                .padding(8.dp)
                .clickable { onOpenNote(warning.noteId) }, // edit note with warning
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row {
                Box(modifier = Modifier.padding(16.dp).size(36.dp)) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = Color.Red,
                                contentColor = Color.White,
                                modifier = Modifier.size(12.dp)
                            ) {
                            }
                        }
                    ) {
                        NoteIcon(
                            warning.title,
                            modifier = Modifier.align(Alignment.Center),
                            backgroundColor = warning.color,
                            onClick = { }
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp) // Reduced padding
                ) {
                    Text(
                        text = warning.title,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (warning.warning.isNotEmpty()) {
                        Text(
                            text = "Warning: ${warning.warning}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
//                    if (warning.suggestions.isNotEmpty()) {
//                        Spacer(modifier = Modifier.height(2.dp)) // Reduced spacing
//                        warning.suggestions.forEach { suggestion ->
//                            Text(
//                                text = "- $suggestion",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.onBackground,
//                                modifier = Modifier.padding(start = 4.dp) // Reduced padding
//                            )
//                        }
//                    }
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "detail",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .size(24.dp)
                        .align(alignment = Alignment.CenterVertically)
                )

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
        score = 0,
        warning = "Your password is too weak.",
        suggestions = listOf(
            "Use at least one special character.",
            "Make your password longer."
        ),
        noteId = 1,
        color = savoyBlue
    )
    PasswordWarningItem(
        warning = sampleWarning,
        onOpenNote = { }
    )
}
