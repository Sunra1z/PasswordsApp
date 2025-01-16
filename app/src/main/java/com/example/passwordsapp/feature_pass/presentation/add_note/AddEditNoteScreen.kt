package com.example.passwordsapp.feature_pass.presentation.add_note

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.passwordsapp.feature_pass.domain.util.generateRandomColor
import com.example.passwordsapp.feature_pass.presentation.add_note.components.NoteModalBottomSheet
import com.example.passwordsapp.feature_pass.presentation.add_note.components.TransparentHintTextField
import com.example.passwordsapp.feature_pass.presentation.notes.NotesEvent
import com.example.passwordsapp.feature_pass.presentation.notes.components.NoItemsComposable
import com.example.passwordsapp.feature_pass.presentation.notes.components.NoteIcon
import com.example.passwordsapp.feature_pass.presentation.notes.components.NoteItem
import com.example.passwordsapp.feature_pass.presentation.notes.components.OrderSection
import com.example.passwordsapp.feature_pass.presentation.util.Screen
import com.example.passwordsapp.ui.theme.savoyBlue
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var isError by remember { mutableStateOf(false) }
    val noteColor = viewModel.noteColor.value

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

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Text(
                        "Add new password",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBackIosNew,
                            contentDescription = "NavigateBack"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        if (viewModel.noteTitle.value.text.isEmpty()
                            || viewModel.passContent.value.text.isEmpty()
                            || viewModel.usernameContent.value.text.isEmpty()) {
                            isError = true
                        } else {
                            viewModel.onEvent(AddEditNoteEvent.SaveNote)
                        }
                    })
                    {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = "Save password",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.AddEditNoteScreen.route)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add note",
                    tint = Color.White
                )
            }
        },
        content = { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ){
                NoteIcon(
                    noteTitle = viewModel.noteTitle.value.text,
                    fontSize = 24.sp,
                    backgroundColor = noteColor,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(64.dp)
                )
                OutlinedTextField(
                    value = viewModel.noteTitle.value.text,
                    leadingIcon = {
                        Icon(Icons.Outlined.Title, contentDescription = "TitleIcon")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface),
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
            }

                OutlinedTextField(
                    value = viewModel.usernameContent.value.text,
                    leadingIcon = {
                        Icon(Icons.Outlined.AccountCircle, contentDescription = "UsernameIcon")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface),
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
                    leadingIcon = {
                        Icon(Icons.Outlined.Lock, contentDescription = "PassIcon")
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface),
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

                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (viewModel.passContent.value.text.length >= 8){
                            Icons.Rounded.CheckCircle
                        } else {
                            Icons.Outlined.Circle
                        },
                        tint = if (viewModel.passContent.value.text.length >= 8){
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Gray
                        },
                        contentDescription = "PassHintAmount",
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                            .size(18.dp)
                    )

                    Text(
                        text = "At least 8 characters",
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        modifier = Modifier
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (viewModel.passContent.value.text.any { it.isLetterOrDigit().not() }){
                            Icons.Rounded.CheckCircle
                        } else {
                            Icons.Outlined.Circle
                        },
                        tint = if (viewModel.passContent.value.text.any { it.isLetterOrDigit().not() }){
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Gray
                        },
                        contentDescription = "PassHintSpecial",
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                            .size(18.dp)
                    )

                    Text(
                        text = "Special characters",
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp,
                        modifier = Modifier
                    )
                }

                if (isError && viewModel.passContent.value.text.isEmpty()) {
                    Text(
                        text = "Password must not be empty.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }


        }
    )
}
