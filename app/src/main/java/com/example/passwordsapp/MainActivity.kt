package com.example.passwordsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                                    ContentHiddenScreen()
                                }
                            }
                        }
                    }
                    else -> {
                        // Handle other cases
                    }
                }
            }
        }

        promptManager.showBiometricPrompt(
            title = "Biometric login for my app",
            description = "Log in using your biometric credential"
        )
    }
}

@Composable
fun ContentHiddenScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Content is hidden")
    }
}

@Preview(showBackground = true)
@Composable
fun ContentHiddenScreenPreview() {
    PasswordsAppTheme {
        ContentHiddenScreen()
    }
}