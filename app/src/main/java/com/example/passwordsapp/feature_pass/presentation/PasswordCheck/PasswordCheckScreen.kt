package com.example.passwordsapp.feature_pass.presentation.PasswordCheck

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.passwordsapp.feature_pass.domain.PasswordCheckViewModel
import com.example.passwordsapp.feature_pass.domain.model.PasswordWarning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordCheckScreen(
    viewModel: PasswordCheckViewModel = hiltViewModel()
) {
    val passwordWarnings by viewModel.passwordWarnings.collectAsState()

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
            PasswordWarningList(
                warnings = passwordWarnings,
                modifier = Modifier.padding(padding)
            )
        }
    )
}

@Composable
fun PasswordWarningList(
    warnings: List<PasswordWarning>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(warnings) { warning ->
            PasswordWarningItem(
                warning = warning,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}