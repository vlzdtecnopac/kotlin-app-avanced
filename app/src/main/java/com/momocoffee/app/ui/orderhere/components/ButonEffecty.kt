package com.momocoffee.app.ui.orderhere.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.momocoffee.app.R
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.stacionFamily

@Composable
fun ButtonEffecty(
    onclick: () -> Unit
){
    val imageModifier = Modifier
        .size(60.dp)

    val modifierCard = Modifier
        .width(260.dp)
        .height(80.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(Color.White)
        .border(
            width = 1.2.dp,
            color = Color.White,
            shape = RoundedCornerShape(14.dp)
        )
        .padding(8.dp)

    Box(modifier = modifierCard.clickable { onclick() }){
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement =  Arrangement.Center,
            verticalAlignment =  Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.catch_icon),
                contentDescription = stringResource(id = R.string.lang_content_description),
                contentScale = ContentScale.Fit,
                modifier = imageModifier
            )
            Spacer(modifier = Modifier.width(25.dp))
            Text(
                stringResource(id = R.string.reason_not_accepting_cash),
                color = BlueDark,
                fontSize = 16.sp,
                fontFamily = stacionFamily,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }
    }


}