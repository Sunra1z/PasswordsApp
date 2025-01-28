package com.example.passwordsapp.feature_pass.presentation.add_note.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.passwordsapp.ui.theme.redAlertColor
import kotlinx.coroutines.delay

@Composable
fun ExportAlert(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
){

    var isButtonEnabled by remember { mutableStateOf(false) }

    // Timer to enable the button after 3 seconds
    LaunchedEffect(Unit) {
        delay(3000)
        isButtonEnabled = true
    }

    AlertDialog(
        onDismissRequest = {  },
        icon = {
            Icon(imageVector = Icons.Rounded.Warning,
                contentDescription = "WarningHeroIcon",
                tint = redAlertColor
            )
        },
        title = { Text("Export passwords?") },
        text = { Text("Your saved passwords will be decrypted and exported as a plain text CSV file. Anyone with access to this file can view your passwords.\n" +
                "\n" +
                "Make sure to store it securely and delete it after use.\n" +
                "\n" +
                "Do you want to proceed?") },
        confirmButton = {
            TextButton(onClick = { onConfirm() },
                enabled = isButtonEnabled)
                {
                Text("Export")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }){
                Text("Cancel")
            }
        }
    )
}