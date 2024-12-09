package com.example.passwordsapp.feature_pass.presentation.add_note

import androidx.compose.ui.focus.FocusState

sealed class AddEditNoteEvent{
    data class EnteredTitle(val value: String): AddEditNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditNoteEvent()
    data class EnteredUsername(val value: String): AddEditNoteEvent()
    data class ChangeUsernameFocus(val focusState: FocusState): AddEditNoteEvent()
    data class EnteredPassword(val value: String): AddEditNoteEvent()
    data class ChangePasswordFocus(val focusState: FocusState): AddEditNoteEvent()
    object SaveNote: AddEditNoteEvent()
}
