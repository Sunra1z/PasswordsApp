package com.example.passwordsapp.feature_pass.presentation.notes.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.ArrowCircleRight
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import com.example.passwordsapp.ui.theme.greenAlertColor
import com.example.passwordsapp.ui.theme.yellowAlertColor
import kotlin.math.absoluteValue


@Composable
fun CarouselCard(
    title: String,
    subtext: String,
    icon: ImageVector,
    iconBackColor: Color,
    onClick: () -> Unit,
    pagerState: PagerState,
    index: Int
) {
    val pageOffset = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
    Card(
        shape = RoundedCornerShape(CornerSize(10.dp)),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier
            .wrapContentWidth()
            .clickable {
                onClick()
            }
            .padding(2.dp)
            .graphicsLayer {
                lerp(
                    start = 0.85f.dp,
                    stop = 1f.dp,
                    fraction = 1f - pageOffset.absoluteValue.coerceIn(0f,1f)
                    ).also { scale ->
                     scaleX = scale.value
                    scaleY = scale.value
                }
                alpha = lerp(
                    start = 0.5f.dp,
                    stop = 1f.dp,
                    fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f)
                ).value
            }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBackColor)
                    .align(Alignment.Start)
            ) {
                Icon(
                    imageVector = icon,
                    tint = MaterialTheme.colorScheme.inverseOnSurface,
                    contentDescription = "RowCardIcon",
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 12.dp)
        ) {
            Text(
                text = subtext,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                contentDescription = "KeyIcon",
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(24.dp)
            )
        }
    }
}

