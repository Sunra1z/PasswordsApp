package com.example.passwordsapp.feature_pass.presentation.notes

import android.graphics.drawable.Icon
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.passwordsapp.feature_pass.domain.model.SettingButtons
import com.example.passwordsapp.feature_pass.presentation.notes.components.ThemeSwitcher
import com.example.passwordsapp.ui.theme.PasswordsAppTheme

val settingsButtons = listOf(
    SettingButtons("Export passwords as CSV", "Export all your data as CSV file", "Export", icon = Icons.Default.Save),
    SettingButtons("Import passwords from CSV", "Imports existing data from your CSV file", "Import", icon = Icons.Default.FileUpload ),
    SettingButtons("Password Alerts", "PasswordsApp will notify you when your passwords are found online.", "Notifications", icon = Icons.Filled.AddAlert),
    SettingButtons("Appearance", "Change the theme of application", "Theme", icon = Icons.Filled.DarkMode),
    SettingButtons("Hide Username", "Hides account's username from preview card", "Username", icon = Icons.Filled.VisibilityOff),
    SettingButtons("About", "App information", "about")
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val darkThemeEnabled by viewModel.darkThemeEnabled.collectAsState()
    val hideUsernameEnabled by viewModel.hideUsernameEnabled.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Settings",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    )
                )
            },
            content = { innerPaddding ->
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
                                        val toast = Toast.makeText(context, "Not implemented yet :(", Toast.LENGTH_SHORT)
                                        toast.show()
                                    }
                                    "about" -> {
                                        val toast = Toast.makeText(context, "PasswordsApp v.0.9.6\n@sunra1z", Toast.LENGTH_SHORT)
                                        toast.show()
                                    }
                                }
                            },
                            leadingContent = {
                                Icon(
                                    button.icon,
                                    contentDescription = button.category,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            headlineContent = {
                                Text(
                                    text = button.name,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            supportingContent = {
                                Text(
                                    text = button.description
                                )
                            },
                            trailingContent = {
                                when (button.category) {
                                    "Notifications" -> {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Switch(
                                                checked = notificationsEnabled,
                                                onCheckedChange = { isChecked ->
                                                    viewModel.setNotificationsEnabled(isChecked)
                                                }
                                            )
                                        }
                                    }
                                    "Theme" -> {
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            ThemeSwitcher(
                                                darkTheme = darkThemeEnabled,
                                                size = 28.dp,
                                                padding = 5.dp,
                                                onClick = { viewModel.setThemeMode(!darkThemeEnabled) }
                                            )
                                        }
                                    }
                                    "Username" -> {
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Switch(
                                                checked = hideUsernameEnabled,
                                                onCheckedChange = { isChecked ->
                                                    viewModel.setUsernameHideEnabled(isChecked)
                                                }
                                            )
                                        }
                                    }
                                }
                            },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.background
                            )
                        )
                    }
                }
            }
        )
}