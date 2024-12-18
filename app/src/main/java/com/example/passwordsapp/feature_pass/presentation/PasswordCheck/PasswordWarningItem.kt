package com.example.passwordsapp.feature_pass.presentation.PasswordCheck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.passwordsapp.feature_pass.domain.model.PasswordWarning

@Composable
fun PasswordWarningItem(
    warning: PasswordWarning,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = when (warning.score) {
                    0, 1 -> Color.Red.copy(alpha = 0.1f) // Weak password
                    2, 3 -> Color.Yellow.copy(alpha = 0.1f) // Moderate password
                    else -> Color.Green.copy(alpha = 0.1f) // Strong password
                },
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = warning.title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Username
        Text(
            text = "Username: ${warning.username}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Score
        Text(
            text = "Password Strength: ${warning.score}/4",
            style = MaterialTheme.typography.bodyMedium,
            color = when (warning.score) {
                0, 1 -> Color.Red
                2, 3 -> Color.Yellow
                else -> Color.Green
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Warning Message
        if (warning.warning.isNotEmpty()) {
            Text(
                text = "Warning: ${warning.warning}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red
            )
        }

        // Suggestions
        if (warning.suggestions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Suggestions:",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            warning.suggestions.forEach { suggestion ->
                Text(
                    text = "- $suggestion",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
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
        score = 1,
        warning = "Your password is too weak.",
        suggestions = listOf(
            "Use at least one special character.",
            "Make your password longer."
        )
    )
    PasswordWarningItem(warning = sampleWarning)
}
