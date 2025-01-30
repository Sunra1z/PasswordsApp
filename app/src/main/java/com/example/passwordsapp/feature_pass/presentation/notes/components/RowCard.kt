package com.example.passwordsapp.feature_pass.presentation.notes.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.rounded.ArrowCircleRight
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.twotone.Key
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordsapp.ui.theme.yellowAlertColor

@Composable
fun RowCard(
    text: String,
    modifier: Modifier,
    icon: ImageVector,
    iconBackColor: Color,
    subtext: String,
){
    Card(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 24.dp)
            .size(width = 180.dp, height = 125.dp),
        shape = RoundedCornerShape(CornerSize(10.dp)),
        elevation = CardDefaults.cardElevation(8.dp),
    ){
        Box(
            modifier = Modifier
                .size(64.dp)
                .padding(12.dp)
                .clip(CircleShape)
                .background(iconBackColor)
                .align(Alignment.Start)
        ){
            Icon(
                imageVector = icon,
                tint = MaterialTheme.colorScheme.inverseOnSurface,
                contentDescription = "RowCardIcon",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = text,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 12.dp, top = 6.dp, bottom = 6.dp)
                )

                Text(
                    text = subtext,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 12.dp, bottom = 8.dp)

                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                contentDescription = "GoTo",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
            )
        }
    }
}

@Preview
@Composable
fun previewRowCard(){
    RowCard(
        text = "Security Check",
        subtext = "Scan now",
        icon = Icons.Default.Shield,
        iconBackColor = yellowAlertColor,
        modifier = Modifier
    )
}