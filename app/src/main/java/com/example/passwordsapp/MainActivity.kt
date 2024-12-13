package com.example.passwordsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.passwordsapp.feature_pass.presentation.add_note.AddEditNoteScreen
import com.example.passwordsapp.feature_pass.presentation.notes.NotesScreen
import com.example.passwordsapp.feature_pass.presentation.util.BiometricPromptManager
import com.example.passwordsapp.feature_pass.presentation.util.Screen
import com.example.passwordsapp.ui.theme.PasswordsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val promptManager by lazy {
        BiometricPromptManager(this)
    }

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            promptManager.promptResults.collect { result ->
                when (result) {
                    is BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                        // Authentication succeeded, proceed to the main content
                        setContent {
                            PasswordsAppTheme {
                                Surface(
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    val navController = rememberNavController()
                                    NavHost(
                                        navController = navController,
                                        startDestination = Screen.NotesScreen.route
                                    ) {
                                        composable(
                                            route = Screen.NotesScreen.route) {
                                            NotesScreen(navController = navController)
                                        }
                                        composable(route = Screen.AddEditNoteScreen.route) {
                                            AddEditNoteScreen(
                                                navController = navController
                                            )
                                        }
                                    }
                                }
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
}

@Composable
fun ContentHiddenScreen(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                imageVector = Icons.Default.Lock,
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
@Preview(showBackground = true)
@Composable
fun ContentHiddenScreenPreview() {
    PasswordsAppTheme {
        ContentHiddenScreen(onRetry = {})
    }
}
