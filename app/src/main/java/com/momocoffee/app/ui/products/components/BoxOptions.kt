package com.momocoffee.app.ui.products.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.momocoffee.app.ui.theme.stacionFamily
import com.momocoffee.app.R
import com.momocoffee.app.ui.components.SolidLineDivider
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily

data class ItemBox(val id: String, val name: String, val price: Int)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BoxOptions(
    iconResource: Int,
    textResource: Int,
    onSelectPrice: (String, Int, String) -> Unit,
    onScrolling: () -> Unit,
    items: List<ItemBox>,

) {
    val isItemActive = remember { mutableStateOf("") }
    val selectOption = remember { mutableStateOf("") }

    LaunchedEffect(items){
            val selected = items.first()
            if (selected != null) {
                onSelectPrice(selected.id, selected.price, selected.name)
                isItemActive.value = selected.id
                selectOption.value = selected.name
            }
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 2.dp, bottom = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(0.3f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = iconResource),
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    tint = Color.White,
                    modifier = Modifier.size(width = 40.dp, height = 40.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    stringResource(id = textResource),
                    fontFamily = stacionFamily,
                    color = Color.White,
                    fontSize = 14.5.sp
                )
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(0.7f),
                horizontalArrangement = Arrangement.End
            ) {
                items.forEach { item ->
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .width(130.dp)
                            .height(55.dp)
                    ) {
                        Button(
                            onClick = {
                                onSelectPrice(item.id, item.price, item.name)
                                onScrolling()
                                isItemActive.value = item.id
                                selectOption.value = item.name
                            },
                            modifier = Modifier.fillMaxSize(),
                            shape = RoundedCornerShape(4.dp),
                            border = if (isItemActive.value == item.id) BorderStroke(
                                1.dp,
                                OrangeDark
                            ) else BorderStroke(1.dp, Color.White),
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (isItemActive.value == item.id) OrangeDark else BlueDark,
                                disabledBackgroundColor = if (isItemActive.value == item.id) OrangeDark else BlueDark,
                                disabledContentColor = if (isItemActive.value == item.id) OrangeDark else BlueDark,
                            )
                        ) {
                            val txt_price = if (item.price == 0) "" else "\n" + "$${item.price}"
                            Text(
                                text = "${item.name} $txt_price",
                                fontSize = 12.sp,
                                color = Color.White,
                                fontFamily = redhatFamily,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }
            }
        }
        SolidLineDivider()
    }
}