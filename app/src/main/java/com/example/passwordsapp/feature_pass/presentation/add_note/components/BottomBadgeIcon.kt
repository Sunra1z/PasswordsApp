package com.example.passwordsapp.feature_pass.presentation.add_note.components

import android.media.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.passwordsapp.ui.theme.yellowAlertColor

@Composable
    fun IconWithBottomEndBadge(
        mainIcon: @Composable () -> Unit,
        badgeIcon: @Composable () -> Unit
    ) {
        Box {
            BadgedBox(
                badge = {
                    Box(
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        badgeIcon()
                    }
                }
            ) {
                mainIcon()
            }
        }
    }