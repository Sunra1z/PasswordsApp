package com.example.passwordsapp.feature_pass.presentation.notes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.passwordsapp.R
import com.example.passwordsapp.feature_pass.domain.model.Note


@Composable
fun NoteItem(
    note: Note,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 24.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(CornerSize(10.dp)),
        elevation = CardDefaults.cardElevation(6.dp)
    ){
        Row(){
            Image(
                painter = painterResource(id = R.drawable.baseline_account_box_24),
                contentDescription = "image",
                modifier = Modifier
                    .padding(6.dp)
                    .size(64.dp)
                    .clip(RoundedCornerShape(CornerSize(6.dp)))
                    .align(alignment = Alignment.CenterVertically)
            )
            Column(
                modifier = Modifier
                    .padding(1.dp)
            ) {
                Text(
                    text = note.title,
                    modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 6.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "••••••••", modifier = Modifier.padding(16.dp, 0.dp, 0.dp, 6.dp), fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24),
                contentDescription = "detail",
                modifier = Modifier
                    .padding(end = 12.dp)
                    .size(24.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
        }
    }
}