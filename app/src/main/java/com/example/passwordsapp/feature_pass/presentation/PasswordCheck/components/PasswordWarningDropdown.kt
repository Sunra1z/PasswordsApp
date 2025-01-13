package com.example.passwordsapp.feature_pass.presentation.PasswordCheck.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.passwordsapp.feature_pass.domain.model.PasswordWarning
import com.example.passwordsapp.ui.theme.redAlertColor

@Composable
fun PasswordWarningDropdown(
    title: String,
    warnings: List<PasswordWarning>,
    cardColor: Color,
    alertColor: Color,
    subtext: String,
    onOpenNote: (Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(16.dp)),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Error,
                    contentDescription = "warning",
                    tint = alertColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtext,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand",
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
            if (expanded) {
                LazyColumn {
                    items(warnings) { warning ->
                        PasswordWarningItem(warning = warning, onOpenNote = onOpenNote)
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PasswordWarningDropdownPreview() {
    val sampleWarnings = listOf(
        PasswordWarning(
            title = "Critical Warning",
            username = "user1",
            password = "password1",
            score = 0,
            warning = "Your password is extremely weak.",
            suggestions = listOf("Use a mix of characters.", "Avoid common words."),
            noteId = 1
        ),
        PasswordWarning(
            title = "High Warning",
            username = "user2",
            password = "password2",
            score = 1,
            warning = "Your password is very weak.",
            suggestions = listOf("Add special characters.", "Increase length."),
            noteId = 2
        ),
        PasswordWarning(
            title = "Moderate Warning",
            username = "user3",
            password = "password3",
            score = 2,
            warning = "Your password is weak.",
            suggestions = listOf("Use uppercase letters.", "Include numbers."),
            noteId = 3
        ),
        PasswordWarning(
            title = "Low Warning",
            username = "user4",
            password = "password4",
            score = 3,
            warning = "Your password is somewhat weak.",
            suggestions = listOf("Avoid sequential characters.", "Use unique words."),
            noteId = 4
        )
    )

    PasswordWarningDropdown(
        title = "Password Warnings",
        warnings = sampleWarnings,
        cardColor = MaterialTheme.colorScheme.surface,
        alertColor = redAlertColor,
        subtext = "Placeholder",
        onOpenNote = { }
    )
}