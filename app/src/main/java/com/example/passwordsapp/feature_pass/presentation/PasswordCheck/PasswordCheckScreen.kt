package com.example.passwordsapp.feature_pass.presentation.PasswordCheck

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.passwordsapp.feature_pass.domain.PasswordCheckViewModel
import com.example.passwordsapp.feature_pass.presentation.add_note.components.NoteModalBottomSheet
import com.example.passwordsapp.feature_pass.presentation.notes.components.PassLoadingAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordCheckScreen(
    viewModel: PasswordCheckViewModel = hiltViewModel()
) {
    val passwordWarnings by viewModel.passwordWarnings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var selectedNoteId by rememberSaveable { mutableStateOf<Int?>(null) }
    var isSheetOpen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadNotes() // Load and analyze notes on screen initialization
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Password Check") }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    val filteredWarnings = passwordWarnings.filter { it.warning.isNotEmpty() }
                    PasswordWarningDropdown(
                        title = "Password Warnings",
                        warnings = filteredWarnings,
                        color = Color.Red,
                        onOpenNote = { noteId ->
                            selectedNoteId = noteId
                            isSheetOpen = true
                        },
                    )
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