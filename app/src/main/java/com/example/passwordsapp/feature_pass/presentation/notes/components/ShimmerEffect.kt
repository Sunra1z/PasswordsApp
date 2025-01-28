package com.example.passwordsapp.feature_pass.presentation.notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun ShimmerEffect(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(8.dp)
            .shimmer()
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
    )
}

@Composable
fun ShimmerRowCard(){
    Box(
        modifier = Modifier
            .size(width = 180.dp, height = 125.dp)
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .shimmer()
            .background(Color.Gray, shape = RoundedCornerShape(10.dp))
    )
}