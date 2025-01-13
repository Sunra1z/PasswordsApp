package com.example.passwordsapp.feature_pass.presentation.notes

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.passwordsapp.feature_pass.domain.model.SettingButtons
import com.example.passwordsapp.ui.theme.PasswordsAppTheme

val settingsButtons = listOf(
    SettingButtons("Export passwords as CSV", "Export all your data as CSV file", "Export"),
    SettingButtons("Import passwords from CSV", "Imports existing data from your CSV file", "Import"),
    SettingButtons("Something else", "Not implemented yet :(", "smth")
)


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
        Scaffold(modifier = Modifier.fillMaxSize()
            ) {
            innerPaddding ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPaddding
                ) {
                    items(settingsButtons) { button ->
                        ListItem(
                            modifier = Modifier.clickable {
                                when (button.category) {
                                    "Export" -> viewModel.exportDataAsCSV(context)
                                    "Import" -> {
                                        // Handle import data action
                                    }
                                }
                            },
                            leadingContent = {
                                when (button.category) {
                                    "Export" -> Icon(Icons.Default.Save, contentDescription = "Export Icon", tint = MaterialTheme.colorScheme.primary)
                                    "Import" -> Icon(Icons.Default.FileUpload, contentDescription = "Import Icon", tint = MaterialTheme.colorScheme.primary)
                                    else -> Icon(Icons.Default.Info, contentDescription = "Default Icon", tint = MaterialTheme.colorScheme.primary)
                                }
                            },
                            headlineContent = { Text(
                                text = button.name,
                                color = MaterialTheme.colorScheme.onPrimary
                            )},
                            supportingContent = { Text(
                                text = button.description
                            ) },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        )
                    }
                }
            }
        }