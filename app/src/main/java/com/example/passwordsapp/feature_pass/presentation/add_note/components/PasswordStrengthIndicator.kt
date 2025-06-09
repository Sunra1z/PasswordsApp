package com.example.passwordsapp.feature_pass.presentation.add_note.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordsapp.ui.theme.greenAlertColor
import com.example.passwordsapp.ui.theme.redAlertColor
import com.example.passwordsapp.ui.theme.yellowAlertColor
import com.nulabinc.zxcvbn.Zxcvbn
import com.nulabinc.zxcvbn.Strength

@Composable
fun PasswordStrengthIndicator(password: String) {
    val zxcvbn = Zxcvbn()
    val strength: Strength = zxcvbn.measure(password)
    val score = strength.score

    val strengthColor = when (score) {
        0 -> redAlertColor
        1 -> redAlertColor
        2 -> yellowAlertColor
        3 -> Color.Green
        4 -> Color.Green
        else -> Color.Gray
    }

    val strengthText = when (score) {
        0 -> "Very Weak"
        1 -> "Weak"
        2 -> "Fair"
        3 -> "Good"
        4 -> "Strong"
        else -> "Unknown"
    }

    Row(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (score > 2){
                Icons.Rounded.CheckCircle
            } else {
                Icons.Rounded.Error
            },
            tint = strengthColor,
            contentDescription = "PassStrength",
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                .size(18.dp)
        )

        Text(
            text = strengthText,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            modifier = Modifier
        )
    }

    if (score < 3) {
        strength.feedback.suggestions.forEach { suggestion ->
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically){
                Icon(
                    imageVector = Icons.Rounded.Info,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "PassHint",
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                        .size(18.dp)
                )
                Text(
                    text = suggestion,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp,
                    modifier = Modifier
                )
            }
        }
    }
}