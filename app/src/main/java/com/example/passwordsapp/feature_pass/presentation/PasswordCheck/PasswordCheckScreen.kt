package com.example.passwordsapp.feature_pass.presentation.PasswordCheck

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NoEncryptionGmailerrorred
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.passwordsapp.feature_pass.presentation.PasswordCheck.components.CircleMeter
import com.example.passwordsapp.feature_pass.presentation.PasswordCheck.components.PasswordNoWarningCard
import com.example.passwordsapp.feature_pass.presentation.PasswordCheck.components.PasswordWarningDropdown
import com.example.passwordsapp.feature_pass.presentation.add_note.components.NoteModalBottomSheet
import com.example.passwordsapp.feature_pass.presentation.util.isNetworkAvailable
import com.example.passwordsapp.ui.theme.greenAlertColor
import com.example.passwordsapp.ui.theme.redAlertColor
import com.example.passwordsapp.ui.theme.yellowAlertColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordCheckScreen(
    viewModel: PasswordCheckViewModel = hiltViewModel()
) {
    val passwordWarnings by viewModel.passwordWarnings.collectAsState()
    val context = LocalContext.current
    val passwordLeaks by viewModel.passwordLeaks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedNoteId by rememberSaveable { mutableStateOf<Int?>(null) }
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    val showAlert = rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            if (!isNetworkAvailable(context)){
                showAlert.value = true
            }
        Log.d("PasswordCheckScreen", "LaunchedEffect: Start loading password data")
        viewModel.loadPasswordData()
        Log.d("PasswordCheckScreen", "LaunchedEffect: End loading password data")
    }

    if (showAlert.value) {
        AlertDialog(
            onDismissRequest = {
                showAlert.value = false
                               },
            icon = {
                Icon(imageVector = Icons.Rounded.Warning,
                    contentDescription = "WarningHeroIcon",
                    tint = Color.Gray
                )
            },
            title = { Text("No Internet Connection") },
            text = { Text("Password Leak Detection System is unavailable") },
            confirmButton = {
                Button(onClick = {
                    showAlert.value = false
                }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Passwords Check",
                    fontWeight = FontWeight.Bold
                ) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                )
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (isLoading) {
                    Log.d("PasswordCheckScreen", "Loading state: true")
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Checking Passwords",
                            modifier = Modifier
                                .padding(top = 8.dp)
                        )
                    }
                } else {
                    Log.d("PasswordCheckScreen", "Loading state: false")
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 16.dp),
                            contentAlignment = Alignment.TopCenter,
                        ) {
                            CircleMeter(
                                warningsCount = passwordWarnings.size, // Example count
                                leaksCount = passwordLeaks.size, // Example count
                                modifier = Modifier,
                                middleText = "Warnings",
                                backgroundColor = MaterialTheme.colorScheme.surface,
                                allGoodColor = greenAlertColor
                            )
                        }

                        if (passwordLeaks.isNotEmpty()) {
                            PasswordWarningDropdown(
                                title = "Compromised Passwords",
                                warnings = passwordLeaks,
                                cardColor = MaterialTheme.colorScheme.surface,
                                alertColor = redAlertColor,
                                icon = Icons.Rounded.WarningAmber,
                                subtext = "${passwordLeaks.size} found",
                                onOpenNote = { noteId ->
                                    selectedNoteId = noteId
                                    isSheetOpen = true
                                    Log.d("PasswordCheckScreen", "Opening note with ID: $noteId")
                                }
                            )
                        }

                        if (passwordWarnings.isNotEmpty()) {
                            PasswordWarningDropdown(
                                title = "Weak passwords",
                                warnings = passwordWarnings,
                                cardColor = MaterialTheme.colorScheme.surface,
                                alertColor = yellowAlertColor,
                                icon = Icons.Rounded.NoEncryptionGmailerrorred,
                                subtext = "${passwordWarnings.size} found",
                                onOpenNote = { noteId ->
                                    selectedNoteId = noteId
                                    isSheetOpen = true
                                    Log.d("PasswordCheckScreen", "Opening note with ID: $noteId")
                                }
                            )
                        }

                        if (passwordLeaks.isEmpty() && passwordWarnings.isEmpty()) {
                            PasswordNoWarningCard(
                                title = "Great!",
                                subtext = "All passwords met with conditions",
                                color = MaterialTheme.colorScheme.surface
                            )
                        }
                    }
                }
            }

            if (isSheetOpen) {
                NoteModalBottomSheet(
                    sheetState = sheetState,
                    viewModel = hiltViewModel(),
                    scope = scope,
                    onDismissRequest = { isSheetOpen = false },
                    noteId = selectedNoteId
                )
            }
        }
    )
}