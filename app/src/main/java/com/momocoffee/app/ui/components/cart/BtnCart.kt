package com.momocoffee.app.ui.components.cart

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.momocoffee.app.R

@Composable
fun BtnCart(
    onClickButton: () -> Unit,
    icon: Int,
    color: Color,
    iconColor: Color,
    borderColor: Color,
    size: Dp = 24.dp
){
    IconButton(
        modifier = Modifier
            .width(size)
            .height(size)
            .clip(RoundedCornerShape(6.dp))
            .background(color)
            .border(
                width = 1.2.dp,
                color = borderColor,
                shape = RoundedCornerShape(6.dp)
            )
            .padding(5.dp)
           ,
        onClick = onClickButton
    ) {
        Icon(
            painterResource(id = icon),
            contentDescription = stringResource(id = R.string.momo_coffe),
            tint = iconColor,
            modifier = Modifier.size(width = size, height = size)
        )
    }
}