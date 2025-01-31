package com.example.passwordsapp.feature_pass.presentation.add_note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PagerIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    activeDotSize: Dp = 8.dp,
    inactiveDotSize: Dp = 4.dp,
    dotSpacing: Dp = 4.dp, // Increased spacing
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize() // Center the PagerIndicator on the screen
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            repeat(pagerState.pageCount) { index ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = dotSpacing)
                        .size(if (pagerState.currentPage == index) activeDotSize else inactiveDotSize)
                        .background(
                            color = if (pagerState.currentPage == index) activeColor else inactiveColor,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}