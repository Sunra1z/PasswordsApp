package com.example.passwordsapp.feature_pass.presentation.notes

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import com.example.passwordsapp.feature_pass.presentation.add_note.components.ExportAlert
import com.example.passwordsapp.feature_pass.presentation.notes.components.ThemeSwitcher
import com.example.passwordsapp.ui.theme.PasswordsAppTheme
import com.example.passwordsapp.ui.theme.redAlertColor

val settingsButtons = listOf(
    SettingButtons("Export passwords as CSV", "Export all your data as CSV file", "Export", icon = Icons.Default.Save),
    SettingButtons("Import passwords from CSV", "Imports existing data from your CSV file", "Import", icon = Icons.Default.FileUpload ),
    SettingButtons("Password Alerts", "PasswordsApp will notify you when your passwords are found online.", "Notifications", icon = Icons.Filled.AddAlert),
    SettingButtons("Alert Cooldown", "PasswordsApp will check your passwords for leaks every 24h, you can change this period", "AlertTime", icon = Icons.Filled.Timer),
    SettingButtons("Appearance", "Change the theme of application", "Theme", icon = Icons.Filled.DarkMode),
    SettingButtons("Hide Username", "Hides account's username from preview card", "Username", icon = Icons.Filled.VisibilityOff),
    SettingButtons("About", "App information", "about"),
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
    val alertTime by viewModel.alertTime.collectAsState()
    val showAlert = remember { mutableStateOf(false) }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv"),
        onResult = { uri ->
            uri?.let {
                viewModel.exportDataAsCSV(context, it)
            }
        }
    )

    if (showAlert.value) {
        ExportAlert(
            onConfirm = {
                showAlert.value = false
                exportLauncher.launch("passwords.csv")
            },
            onDismiss = {
                showAlert.value = false
            }
        )
    }

    val alertTimeOptions = listOf("1h", "6h", "12h", "24h")
    val (expanded, setExpanded) = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                )
            )
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPadding
            ) {
                items(settingsButtons) { button ->
                    ListItem(
                        modifier = Modifier.clickable {
                            when (button.category) {
                                "Export" -> showAlert.value = true
                                "Import" -> {
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
                                "AlertTime" -> {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box {
                                            Text(
                                                text = alertTime,
                                                modifier = Modifier
                                                    .clickable { setExpanded(true) }
                                                    .padding(8.dp)
                                            )
                                            DropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = { setExpanded(false) }
                                            ) {
                                                alertTimeOptions.forEach { option ->
                                                    DropdownMenuItem(
                                                        text = { Text(option) },
                                                        onClick = {
                                                            viewModel.setAlertTime(option)
                                                            setExpanded(false)
                                                        }
                                                    )
                                                }
                                            }
                                        }
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