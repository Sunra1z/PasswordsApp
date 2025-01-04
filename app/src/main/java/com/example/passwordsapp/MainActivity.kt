package com.example.passwordsapp

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passwordsapp.feature_pass.presentation.PasswordCheck.PasswordCheckScreen
import com.example.passwordsapp.feature_pass.presentation.SettingsScreen
import com.example.passwordsapp.feature_pass.presentation.add_note.AddEditNoteScreen
import com.example.passwordsapp.feature_pass.presentation.add_note.components.BottomNavigationBar
import com.example.passwordsapp.feature_pass.presentation.notes.NotesScreen
import com.example.passwordsapp.feature_pass.presentation.util.BiometricPromptManager
import com.example.passwordsapp.feature_pass.presentation.util.Screen
import com.example.passwordsapp.ui.theme.PasswordsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 1
    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestNotificationPermission()

        lifecycleScope.launch {
            promptManager.promptResults.collect { result ->
                when (result) {
                    is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                        // Authentication succeeded, proceed to the main content
                        setContent {
                            PasswordsAppTheme {
                                MainScreen()

                            }
                        }
                    }
                    is BiometricPromptManager.BiometricResult.AuthenticationError,
                    is BiometricPromptManager.BiometricResult.AuthenticationFailed -> {
                        // Handle authentication error or failure
                        setContent {
                            PasswordsAppTheme {
                                Surface(
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    ContentHiddenScreen(onRetry = {
                                        promptManager.showBiometricPrompt(
                                            title = "Login to view your passwords",
                                            description = "Without authentication content is prohibited"
                                        )
                                    })
                                }
                            }
                        }
                    }
                    else -> {
                        setContent {
                            PasswordsAppTheme {
                                Surface(
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Image(
                                                imageVector = Icons.Default.Warning,
                                                contentDescription = "locked",
                                                modifier = Modifier
                                                    .size(64.dp)
                                            )
                                            Text(
                                                text = "It seems like your device has no security measures" +
                                                        "\nTo use this app set PIN/Fingerprint/Face Unlock",
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        promptManager.showBiometricPrompt(
            title = "Login to view your passwords",
            description = "Without authentication content is prohibited"
        )
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // For older Android versions, permission is implicitly granted
            true
        }
    }


    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 or higher
            if (!isNotificationPermissionGranted()) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, continue with your functionality
                } else {
                    // Permission denied, show a message or redirect to settings
                    showPermissionDialog() // Show the dialog again
                }
            }
        }
    }


    private fun showPermissionDialog() {
        // Create a dialog explaining the need for notification permission
        val dialog = AlertDialog.Builder(this)
            .setTitle("Notification Permission")
            .setMessage("It is recommended to set Notification permission" +
                    "\nThis app will notify you in case of compromised passwords!")
            .setPositiveButton("Yes") { _, _ ->
                // Open app settings if user clicks "Yes"
                openAppSettings()
            }
            .setNegativeButton("No") { dialog, _ ->
                // Dismiss the dialog if user clicks "No"
                dialog.dismiss()
            }
            .setCancelable(false) // To prevent accidental dismissal
            .create()

        // Show the dialog if the permission is not granted
        if (!isNotificationPermissionGranted()) {
            dialog.show()
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}

@Composable
fun ContentHiddenScreen(onRetry: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "locked",
                    modifier = Modifier
                        .size(64.dp)
                )
                Text(text = "Content is restricted")
                Button(onClick = onRetry) {
                    Text(text = "Authenticate")
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ContentHiddenScreenPreview() {
    PasswordsAppTheme {
        ContentHiddenScreen(onRetry = {})
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.NotesScreen.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.NotesScreen.route) { NotesScreen(navController = navController) }
            composable(Screen.PasswordCheckScreen.route) { PasswordCheckScreen() }
            composable(Screen.SettingsScreen.route) { SettingsScreen() }
            composable(Screen.AddEditNoteScreen.route){ AddEditNoteScreen(navController) }
        }
    }
}
