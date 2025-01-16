package com.example.passwordsapp.feature_pass.presentation.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColor
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.passwordsapp.feature_pass.domain.repository.PreferencesRepository
import com.example.passwordsapp.feature_pass.domain.util.NoteOrder
import com.example.passwordsapp.feature_pass.presentation.add_note.components.NoteModalBottomSheet
import com.example.passwordsapp.feature_pass.presentation.notes.components.NoItemsComposable
import com.example.passwordsapp.feature_pass.presentation.notes.components.NoteItem
import com.example.passwordsapp.feature_pass.presentation.notes.components.OrderSection
import com.example.passwordsapp.feature_pass.presentation.notes.components.ShimmerEffect
import com.example.passwordsapp.feature_pass.presentation.util.Screen
import com.example.passwordsapp.ui.theme.grayishCard
import com.example.passwordsapp.ui.theme.savoyBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
   viewModel: NotesViewModel = hiltViewModel(),
   navController: NavController
) {
   val state = viewModel.state.value
   val snackbarHostState = remember { SnackbarHostState() }
   val menuExpanded = remember { mutableStateOf(false) }
   val scope = rememberCoroutineScope()
   val sheetState = rememberModalBottomSheetState()
   var isEnabled by rememberSaveable { mutableStateOf(false) }
   var isSheetOpen by rememberSaveable { mutableStateOf(false) }
   var selectedNoteId by rememberSaveable { mutableStateOf<Int?>(null) }
   var selectedNoteColor by rememberSaveable { mutableStateOf<Int?>(null) }
   var showSnackbar by rememberSaveable { mutableStateOf(false) }

   Scaffold(
      containerColor = MaterialTheme.colorScheme.background,
      topBar = {
         TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
               containerColor = MaterialTheme.colorScheme.background,
               titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
               Text(
                  "Passwords",
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  fontWeight = FontWeight.Bold
               )
            },
            actions = {
               IconButton(onClick = { menuExpanded.value = true }) {
                  Icon(
                     imageVector = Icons.Filled.Menu,
                     contentDescription = "Localized description"
                  )
               }
            },
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
      snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
      content = { paddingValues ->

         Column(
            modifier = Modifier
               .fillMaxSize()
               .padding(paddingValues)
               .padding(horizontal = 8.dp)
         ) {
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
            AnimatedVisibility(
               visible = state.isLoading,
               enter = fadeIn(),
               exit = fadeOut()
            ) {
               // smth
            }
            AnimatedVisibility(
               visible = !state.isLoading,
               enter = fadeIn(),
               exit = fadeOut()
            ) {
               if (state.notes.isEmpty()) {
                  NoItemsComposable { navController.navigate(Screen.AddEditNoteScreen.route) }
               } else {
                  LazyColumn(modifier = Modifier.fillMaxSize()) {
                     items(state.notes, key = { it.id!! }) { note ->
                        NoteItem(
                           note = note,
                           onClick = {
                              scope.launch {
                                 selectedNoteId = note.id
                                 selectedNoteColor = note.color
                                 isSheetOpen = true
                              }
                           },
                           onDelete = {
                              viewModel.onEvent(NotesEvent.DeleteNote(note))
                              showSnackbar = true
                           },
                           hideUsername = state.hideUsername
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                     }
                  }
               }
            }
         }
         if (showSnackbar) {
            LaunchedEffect(snackbarHostState) {
               val result = snackbarHostState.showSnackbar(
                  message = "Note deleted",
                  duration = SnackbarDuration.Short
               )
               showSnackbar = false
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