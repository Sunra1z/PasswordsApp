package com.example.passwordsapp.feature_pass.presentation.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.passwordsapp.feature_pass.presentation.notes.components.NoteItem
import com.example.passwordsapp.feature_pass.presentation.notes.components.OrderSection
import com.example.passwordsapp.feature_pass.presentation.util.Screen

@Composable
fun NotesScreen(
   viewModel: NotesViewModel = hiltViewModel(),
   navController: NavController
) {
   val state = viewModel.state.value
   val scope = rememberCoroutineScope()

   Scaffold(
      floatingActionButton = {
         FloatingActionButton(
            onClick = {
               // add note
               navController.navigate(Screen.AddEditNoteScreen.route)
            },
            containerColor = MaterialTheme.colorScheme.primary
         ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
         }
      }
   ) { paddingValues ->
      Column(
         modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
      ) {
         Text(
            text = "Passwords",
            style = MaterialTheme.typography.headlineLarge
         )
         IconButton(
            onClick = {
               viewModel.onEvent(NotesEvent.ToggleOrderSection)
            },
         ) {
            Icon(
               imageVector = Icons.AutoMirrored.Default.List,
               contentDescription = "Sort"
            )
         }
         AnimatedVisibility(
            visible = state.isOrderSectionVisible,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
         ) {
            OrderSection(
               modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 16.dp),
               noteOrder = state.noteOrder,
               onOrderChange = {
                  viewModel.onEvent(NotesEvent.Order(it))
               }
            )
         }
         Spacer(modifier = Modifier.height(16.dp))
         LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.notes) { note ->
               NoteItem(
                  note = note,
                  onClick = {
                     navController.navigate(Screen.AddEditNoteScreen.route + "?noteId=${note.id}")
                  }
               )
               Spacer(modifier = Modifier.height(16.dp))
            }
         }
      }
   }
}