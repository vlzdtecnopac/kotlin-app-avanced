package com.momocoffee.app.ui.orderhere.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.OrangeDarkLight
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.ui.theme.stacionFamily

@Composable
fun ButtonField(
    onclick: () -> Unit,
    enabled: Boolean,
    text: String
){
    Button(
        onClick = onclick,
        modifier = Modifier.width(340.dp).height(50.dp).padding(horizontal = 25.dp),
        enabled = enabled,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = OrangeDark,
            disabledBackgroundColor = OrangeDarkLight,
            disabledContentColor = OrangeDarkLight
        )
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
            color = Color.White,
            fontFamily = redhatFamily,
        )
    }
}