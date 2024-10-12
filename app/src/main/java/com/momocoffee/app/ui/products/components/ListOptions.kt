package com.momocoffee.app.ui.products.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.momocoffee.app.R
import com.momocoffee.app.ui.components.SolidLineDivider
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.viewmodel.ProductsViewModel

data class ItemList(val id: String, val name: String, val price: Int)

@Composable
fun ListOptions(
    iconResource: Int,
    onSelectPrice: (String, Int, String) -> Unit,
    onScrolling: () -> Unit,
    items: List<ItemList>,
    defaultSelect: String
) {
    val isItemActive = remember { mutableStateOf("") }
    val selectOption = remember { mutableStateOf("") }
    val checked = remember { mutableStateOf(false) }

    LaunchedEffect(Unit){
        val selected = items.find { it.name == defaultSelect}
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
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.1f),
            ) {
                Icon(
                    painterResource(id = iconResource),
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    tint = Color.White,
                    modifier = Modifier.size(width = 40.dp, height = 40.dp)
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            text = item.name,
                            fontSize = 16.sp,
                            color = Color.White,
                            fontFamily = redhatFamily,
                            textAlign = TextAlign.Start
                        )

                        Text(
                            modifier = Modifier.fillMaxWidth(0.4f),
                            text = "$ ${item.price}",
                            fontSize = 16.sp,
                            color = Color.White,
                            fontFamily = redhatFamily,
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Checkbox(
                            checked = isItemActive.value == item.id,
                            onCheckedChange = {
                                checked.value = it
                                onScrolling()
                                if (checked.value) {
                                    onSelectPrice(item.id,item.price, item.name)
                                    isItemActive.value = item.id
                                    selectOption.value = item.name
                                }else{
                                    if(items.size <= 1){
                                        onSelectPrice("",0, "")
                                        isItemActive.value = ""
                                        selectOption.value = ""
                                    }
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = OrangeDark,
                                uncheckedColor = Color.White

                            ),
                        )
                    }

                }
            }

        }
        SolidLineDivider()
    }
}