package com.momocoffee.app.ui.client.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.momocoffee.app.R
import com.momocoffee.app.ui.theme.BlueDark

data class Country(val label: String, val value: String)

@Composable
fun DropDownOutline(
    selected: MutableState<String>,
    selectedLabel: MutableState<String>
) {
    var selectedIndex by remember{ mutableStateOf(0) }
    var expanded by remember { mutableStateOf(false) }

    val items = listOf(
        Country(label = "", value = "" ),
        Country(label = "España", value = "+34" ),
        Country(label = "Italia", value = "+39" ),
        Country(label = "Peru", value = "+51" ),
        Country(label = "Mexico", value = "+52" ),
        Country(label = "Cuba", value = "+53" ),
        Country(label = "Argentina", value = "+54" ),
        Country(label = "Brazil", value = "+55" ),
        Country(label = "Chile", value = "+56" ),
        Country(label = "Colombia", value = "+57" ),
        Country(label = "Venezuela", value = "+58" ),
        Country(label = "USA/Canada", value = "+1" ),
        Country(label = "Rusia", value = "+7" ),
    )

    val selectModifierCard =
        Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable(onClick = { expanded = true })
            .padding(start = 20.dp)
            .background(BlueDark)
            .border(
                width = 1.1.dp,
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )

    Row(
        modifier = selectModifierCard,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ){
        Text(items[selectedIndex].value, color = Color.White)
        Icon(
            Icons.Rounded.KeyboardArrowDown,
            contentDescription = stringResource(id = R.string.momo_coffe),
            tint = Color.White
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier
            .width(180.dp)
            .background(
                Color.White
            )
    ) {
        items.forEachIndexed { index, s ->
            DropdownMenuItem(onClick = {
                selectedIndex = index
                selected.value = items[index].value
                selectedLabel.value = items[index].label
                expanded = false
            }) {
                Text(text = s.value )
            }
        }
    }
}