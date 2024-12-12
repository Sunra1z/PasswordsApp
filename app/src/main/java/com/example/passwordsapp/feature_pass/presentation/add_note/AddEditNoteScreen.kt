package com.example.passwordsapp.feature_pass.presentation.add_note

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.passwordsapp.feature_pass.presentation.add_note.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackBar -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        TransparentHintTextField(
            text = viewModel.noteTitle.value.text,
            hint = viewModel.noteTitle.value.hint,
            onValueChange = {
                viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
            },
            onFocusChange = {
                viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
            },
            isHintVisible = viewModel.noteTitle.value.isHintVisible,
            singleLine = true,
            textStyle = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(16.dp))
        TransparentHintTextField(
            text = viewModel.usernameContent.value.text,
            hint = viewModel.usernameContent.value.hint,
            onValueChange = {
                viewModel.onEvent(AddEditNoteEvent.EnteredUsername(it))
            },
            onFocusChange = {
                viewModel.onEvent(AddEditNoteEvent.ChangeUsernameFocus(it))
            },
            isHintVisible = viewModel.usernameContent.value.isHintVisible,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(16.dp))
        TransparentHintTextField(
            text = viewModel.passContent.value.text,
            hint = viewModel.passContent.value.hint,
            onValueChange = {
                viewModel.onEvent(AddEditNoteEvent.EnteredPassword(it))
            },
            onFocusChange = {
                viewModel.onEvent(AddEditNoteEvent.ChangePasswordFocus(it))
            },
            isHintVisible = viewModel.passContent.value.isHintVisible,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.onEvent(AddEditNoteEvent.SaveNote)
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Save Note")
        }
    }
}