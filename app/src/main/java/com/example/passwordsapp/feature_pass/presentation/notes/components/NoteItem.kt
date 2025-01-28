package com.example.passwordsapp.feature_pass.presentation.notes.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordsapp.R
import com.example.passwordsapp.feature_pass.domain.model.Note
import com.example.passwordsapp.feature_pass.domain.util.toColor
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteItem(
    note: Note,
    hideUsername: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    animationDuration: Int = 500
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if(value == SwipeToDismissBoxValue.EndToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        },
        positionalThreshold = { it * .25f }
    )

    LaunchedEffect(key1 = isRemoved) {
        if(isRemoved){
            delay(animationDuration.toLong())
            onDelete()
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        Box {
            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromStartToEnd = false,
                backgroundContent = { DeleteBackground(dismissState) },
                content = {
                    Card(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .fillMaxWidth()
                            .clickable { onClick() },
                        shape = RoundedCornerShape(CornerSize(10.dp)),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                NoteIcon(
                                    noteTitle = note.title,
                                    modifier = Modifier
                                        .padding(start = 16.dp, top = 12.dp, end = 8.dp, bottom = 12.dp)
                                        .size(40.dp),
                                    backgroundColor = toColor(note.color),
                                    onClick = { }
                                )
                                Column(modifier = Modifier
                                    .weight(1f)) {
                                    Text(
                                        text = note.title,
                                        modifier = Modifier.padding(8.dp, 16.dp, 0.dp, 6.dp),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        text = if(!hideUsername){
                                            note.username
                                        } else {
                                            "••••••••"
                                        },
                                        modifier = Modifier.padding(8.dp, 0.dp, 0.dp, 16.dp),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                    contentDescription = "OpenNote",
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .size(24.dp),
                                    tint = MaterialTheme.colorScheme.onBackground,
                                )
                            }
                        }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun NoteItemPreview() {
    val sampleNote = Note(
        title = "Sample Note",
        username = "tokaev.04@gmail.com",
        password = byteArrayOf(),
        timeStamp = System.currentTimeMillis(),
        passwordIv = byteArrayOf(),
        id = 1
    )
    NoteItem(
        note = sampleNote,
        onClick = { /* Handle click */ },
        onDelete = { /* Handle delete */ },
        hideUsername = false
    )
}