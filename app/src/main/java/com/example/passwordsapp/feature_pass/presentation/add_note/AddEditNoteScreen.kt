package com.example.passwordsapp.feature_pass.presentation.add_note

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
    var isError by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }

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
            .offset(y = (-75).dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add Password",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            value = viewModel.noteTitle.value.text,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground),
            onValueChange = {
                viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                isError = it.isEmpty()
            },
            label = { Text("Title") },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            isError = isError && viewModel.noteTitle.value.text.isEmpty()
        )

        OutlinedTextField(
            value = viewModel.usernameContent.value.text,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground),
            onValueChange = {
                viewModel.onEvent(AddEditNoteEvent.EnteredUsername(it))
                isError = it.isEmpty()
            },
            label = { Text("Username") },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            isError = isError && viewModel.usernameContent.value.text.isEmpty()
        )

        OutlinedTextField(
            value = viewModel.passContent.value.text,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground),
            onValueChange = {
                viewModel.onEvent(AddEditNoteEvent.EnteredPassword(it))
                isError = it.isEmpty()
            },
            label = { Text("Password") },
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            visualTransformation = PasswordVisualTransformation(),
            isError = isError && viewModel.passContent.value.text.isEmpty(),
            trailingIcon = {
                if (isError && viewModel.passContent.value.text.isEmpty()) {
                    Icon(Icons.Default.Error, contentDescription = "Error")
                }
            }
        )

        if (isError && viewModel.passContent.value.text.isEmpty()) {
            Text(
                text = "Password must not be empty.",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = {
                if (viewModel.noteTitle.value.text.isEmpty()
                    || viewModel.passContent.value.text.isEmpty()
                    || viewModel.usernameContent.value.text.isEmpty()) {
                    isError = true
                } else {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                }
            }) {
                Text("Save")
            }

            Button(onClick = { navController.navigateUp() }) {
                Text("Cancel")
            }
        }
    }
}