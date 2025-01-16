package com.example.passwordsapp.feature_pass.presentation.PasswordCheck.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordsapp.ui.theme.greenAlertColor
import com.example.passwordsapp.ui.theme.redAlertColor
import com.example.passwordsapp.ui.theme.yellowAlertColor
import kotlinx.coroutines.launch

@Composable
fun CircleMeter(
    warningsCount: Int,
    leaksCount: Int,
    middleText: String,
    backgroundColor: Color,
    allGoodColor: Color,
    modifier: Modifier = Modifier
) {
    val totalCount = warningsCount + leaksCount
    val warningsPercentage = if (totalCount == 0) 0f else warningsCount.toFloat() / totalCount
    val leaksPercentage = if (totalCount == 0) 0f else leaksCount.toFloat() / totalCount

    val animatedGreenPercentage = remember { Animatable(0f) }
    val animatedWarningsPercentage = remember { Animatable(0f) }
    val animatedLeaksPercentage = remember { Animatable(0f) }

    LaunchedEffect(warningsCount, leaksCount) {
        if (totalCount == 0) {
            launch {
                animatedGreenPercentage.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(durationMillis = 1000)
                )
            }
        } else {
            animatedGreenPercentage.snapTo(0f)
            launch {
                animatedWarningsPercentage.animateTo(
                    targetValue = warningsPercentage,
                    animationSpec = tween(durationMillis = 1000)
                )
            }
            launch {
                animatedLeaksPercentage.animateTo(
                    targetValue = leaksPercentage,
                    animationSpec = tween(durationMillis = 1000)
                )
            }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(200.dp)
            .padding(16.dp)
    ) {
        Canvas(modifier = Modifier
            .fillMaxSize()
            .shadow(elevation = 20.dp, shape = CircleShape, clip = false)) {
            val strokeWidth = 20.dp.toPx()
            val radius = size.minDimension / 2

            // Draw background circle
            drawCircle(
                color = backgroundColor,
                radius = radius - strokeWidth / 2
            )

            if (totalCount == 0) {
                drawArc(
                    color = allGoodColor,
                    startAngle = -90f,
                    sweepAngle = animatedGreenPercentage.value * 360,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )
            } else {
                drawArc(
                    color = redAlertColor,
                    startAngle = -90f,
                    sweepAngle = animatedLeaksPercentage.value * 360,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )
                drawArc(
                    color = yellowAlertColor,
                    startAngle = -90f + animatedLeaksPercentage.value * 360,
                    sweepAngle = animatedWarningsPercentage.value * 360,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = totalCount.toString(),
                fontSize = 48.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = middleText,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
fun PreviewCircleMeter() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        CircleMeter(
            warningsCount = 9, // Example count
            leaksCount = 3, // Example count
            modifier = Modifier,
            middleText = "warnings",
            backgroundColor = MaterialTheme.colorScheme.primary,
            allGoodColor = MaterialTheme.colorScheme.secondary
        )
    }
}