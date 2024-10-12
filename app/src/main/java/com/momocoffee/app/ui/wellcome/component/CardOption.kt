package com.momocoffee.app.ui.wellcome.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.ui.theme.stacionFamily


@Composable
fun CardOption(text: String, icon: Painter, color: Color, textColor: Color) {

    Box(
        modifier = Modifier
            .width(210.dp)
            .height(210.dp)
            .padding(10.dp)
            .background(Color.Transparent)

    ) {
        Column(
            modifier = Modifier
                .width(190.dp)
                .height(190.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(color)
                .border(
                    width = 1.2.dp,
                    color = textColor,
                    shape = RoundedCornerShape(14.dp)
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = "Icon Clear",
                tint = textColor,
                modifier = Modifier
                    .width(160.dp)
                    .height(80.dp)
                    .scale(0.5f)
            )
            Text(
                text,
                textAlign = TextAlign.Center,
                lineHeight = 28.sp,
                color = textColor,
                fontSize = 24.sp,
                fontFamily = redhatFamily,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "Conectado",
                textAlign = TextAlign.Center,
                color = textColor,
                fontSize = 18.sp,
                fontFamily = stacionFamily,
                fontWeight = FontWeight.Normal
            )
        }
    }
}