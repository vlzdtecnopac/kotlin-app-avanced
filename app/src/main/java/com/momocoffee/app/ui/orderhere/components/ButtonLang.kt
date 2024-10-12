package com.momocoffee.app.ui.orderhere.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.momocoffee.app.R
import com.momocoffee.app.ui.theme.stacionFamily

@Composable
fun ButtonLang(
    onclick: () -> Unit,
    icon: Painter,
    text: String
){

    val imageModifier = Modifier
        .size(50.dp)

    val modifierCard = Modifier
        .width(120.dp)
        .height(100.dp)
        .clip(RoundedCornerShape(14.dp))
        .border(
            width = 1.2.dp,
            color = Color.White,
            shape = RoundedCornerShape(14.dp)
        ).padding(8.dp)

    Box(modifier = modifierCard.clickable {
        onclick()
    }){
        Column (
            modifier =  Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = icon,
                contentDescription = stringResource(id = R.string.lang_content_description),
                contentScale = ContentScale.Fit,
                modifier = imageModifier
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text,
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = stacionFamily,
                fontWeight = FontWeight.Normal
            )
        }
    }

}