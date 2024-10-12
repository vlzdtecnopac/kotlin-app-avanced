package com.momocoffee.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp

import androidx.compose.ui.unit.dp


@Composable
fun SolidLineDivider(
    color: Color = Color.White,
    thickness: Dp = .6.dp,
    paddingTop: Dp = 0.dp,
    paddingButton: Dp = 0.dp,
    paddingLeft: Dp = 0.dp,
    paddingRight: Dp = 0.dp
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(
                top = paddingTop,
                bottom = paddingButton,
                end = paddingRight,
                start = paddingLeft
            )
            .height(thickness)
            .background(color)
    )
}

@Composable
fun DashedDivider(
    color: Color = Color.White,
    thickness: Dp = 2.dp,
    dashLength: Dp = 2.dp,
    gapLength: Dp = 4.dp
) {
    Box(
        modifier = Modifier
            .height(thickness)
            .fillMaxWidth()
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {

            val dashPx = dashLength.toPx()
            val gapPx = gapLength.toPx()
            val totalLength = size.width

            translate(left = 0f, top = size.height / 2 - thickness.toPx() / 2) {
                var currentX = 0f
                while (currentX < totalLength) {
                    drawLine(
                        color = color,
                        start = Offset(currentX, 0f),
                        end = Offset(currentX + dashPx, 0f),
                        strokeWidth = thickness.toPx()
                    )
                    currentX += dashPx + gapPx
                }
            }
        }
    }
}
