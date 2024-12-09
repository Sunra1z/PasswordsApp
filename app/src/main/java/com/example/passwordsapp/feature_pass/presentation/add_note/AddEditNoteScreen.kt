package com.example.passwordsapp.feature_pass.presentation.add_note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.passwordsapp.feature_pass.presentation.add_note.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest


@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val usernameState = viewModel.usernameContent.value
    val passwordState = viewModel.passContent.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is AddEditNoteViewModel.UiEvent.ShowSnackBar -> {
                    // snackbar show
                }
                is AddEditNoteViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Save note")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
           Spacer(modifier = Modifier.height(16.dp))
           TransparentHintTextField(
               text = titleState.text,
               hint = titleState.hint,
               onValueChange = {
                   viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
               },
               onFocusChange = {
                   viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
               },
               isHintVisible = titleState.isHintVisible,
               singleLine = true,
               textStyle = MaterialTheme.typography.headlineLarge
           )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = usernameState.text,
                hint = usernameState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredUsername(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeUsernameFocus(it))
                },
                isHintVisible = usernameState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = passwordState.text,
                hint = passwordState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredPassword(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangePasswordFocus(it))
                },
                isHintVisible = passwordState.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
