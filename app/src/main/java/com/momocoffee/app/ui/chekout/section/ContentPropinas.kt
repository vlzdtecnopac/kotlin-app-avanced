package com.momocoffee.app.ui.chekout.section

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.momocoffee.app.R
import com.momocoffee.app.ui.chekout.components.PropinaModal
import com.momocoffee.app.ui.components.VerifyKiosko
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.BlueLight
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily


data class ListItemPropina(
    val title: String,
    val percentage: String,
    val active: Boolean
)

@Composable
fun ContentPropinas(
    onSelectValue: (Int) -> Unit,
    onSelectPropina: (Int) -> Unit,
    onTypePropina: (Int) -> Unit,
    navController: NavController,
    ) {

    var typePropina by remember { mutableStateOf(0) }
    var mountPropinaSelected by remember { mutableStateOf(2) }
    var showModal by remember { mutableStateOf(false) }

    VerifyKiosko(navController)

    Row(modifier = Modifier.height(110.dp)) {
        Image(
            painter = painterResource(id = R.drawable.barista),
            contentDescription = stringResource(id = R.string.momo_coffe),
            modifier = Modifier.width(105.dp),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                stringResource(id = R.string.start_barista_working),
                color = Color.White
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Icon(
                    painterResource(id = R.drawable.dollar_circle_icon),
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    tint = Color.White,
                    modifier = Modifier.size(width = 20.dp, height = 20.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    stringResource(id = R.string.add_tip),
                    color = Color.White,
                    fontFamily = redhatFamily,
                    fontSize = 14.sp
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(10.dp))

    if (showModal && typePropina == 1) {
        PropinaModal(
            title = stringResource(id = R.string.write_percent),
            onCancel = { showModal = false },
            onSelect = {
                onSelectPropina(4)
                onTypePropina(1)
                onSelectValue(it)
                showModal = false
            })
    }

    if (showModal && typePropina == 2) {
        PropinaModal(
            title = stringResource(id = R.string.write_amount_peso),
            onCancel = { showModal = false },
            onSelect = {
                onSelectPropina(4)
                onTypePropina(2)
                onSelectValue(it)
                showModal = false

            })
    }

    val optionsPropinas = listOf(
        ListItemPropina(
            title = stringResource(id = R.string.propina_never),
            percentage = "0%",
            active = false
        ),
        ListItemPropina(
            title = stringResource(id = R.string.extra_us_hero),
            percentage = "5%",
            active = false
        ),
        ListItemPropina(
            title = stringResource(id = R.string.excelent_choice),
            percentage = "10%",
            active = true
        ),
        ListItemPropina(
            title = stringResource(id = R.string.gesture_incredible),
            percentage = "15%",
            active = false
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(0.dp),
        content = {
            items(optionsPropinas.size) { index ->
                Card(
                    backgroundColor = Color.Transparent,
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth(),
                ) {
                    Button(
                        elevation = ButtonDefaults.elevation(0.dp),
                        modifier = Modifier.fillMaxWidth(0.5f),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(5.dp),
                        border = BorderStroke(
                            width = 0.dp,
                            color = if (mountPropinaSelected == index) OrangeDark else BlueLight
                        ),
                        colors = ButtonDefaults.buttonColors(backgroundColor = if (mountPropinaSelected == index) OrangeDark else BlueLight),
                        onClick = {
                            mountPropinaSelected = index
                            onSelectPropina(index)
                        }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                optionsPropinas[index].percentage,
                                color = if (mountPropinaSelected == index) Color.White else BlueDark,
                                fontFamily = redhatFamily,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Box(modifier = Modifier.weight(0.5f)) {
                                Text(
                                    optionsPropinas[index].title,
                                    color = if (mountPropinaSelected == index) Color.White else BlueDark,
                                    fontFamily = redhatFamily,
                                    fontSize = 10.sp,
                                    lineHeight = 12.sp
                                )
                            }

                            Icon(
                                painterResource(id = R.drawable.dollar_circle_icon),
                                contentDescription = stringResource(id = R.string.momo_coffe),
                                tint = if (mountPropinaSelected == index) Color.White else BlueDark,
                                modifier = Modifier.size(width = 18.dp, height = 18.dp)
                            )
                        }
                    }
                }

            }
        }
    )

    Row(
        modifier = Modifier.padding(start = 3.dp, end = 3.dp)
    ) {
        Button(
            elevation = ButtonDefaults.elevation(0.dp),
            modifier = Modifier.fillMaxWidth(0.5f),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(
                width = 0.dp,
                color = if (mountPropinaSelected == 4) OrangeDark else BlueLight
            ),
            colors = ButtonDefaults.buttonColors(backgroundColor = if (mountPropinaSelected == 4) OrangeDark else BlueLight),
            onClick = {
                mountPropinaSelected = 4
            }) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(id = R.string.other),
                    color = if (mountPropinaSelected == 4) Color.White else BlueDark,
                    fontFamily = redhatFamily,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.weight(0.5f)) {
                    Text(
                        stringResource(id = R.string.yuo_decided),
                        color = if (mountPropinaSelected == 4) Color.White else BlueDark,
                        fontFamily = redhatFamily,
                        fontSize = 10.sp,
                        lineHeight = 12.sp
                    )
                }

                Icon(
                    painterResource(id = R.drawable.dollar_circle_icon),
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    tint = if (mountPropinaSelected == 4) Color.White else BlueDark,
                    modifier = Modifier.size(width = 18.dp, height = 18.dp)
                )
            }
        }
        if (mountPropinaSelected == 4) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp),
            ) {
                Button(
                    elevation = ButtonDefaults.elevation(0.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .height(41.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(
                        width = 0.dp,
                        color = if (typePropina == 1) OrangeDark else BlueLight
                    ),
                    colors = ButtonDefaults.buttonColors(backgroundColor = if (typePropina == 1) OrangeDark else BlueLight),
                    onClick = {
                        showModal = true
                        typePropina = 1
                        onTypePropina(0)
                        onSelectValue(0)
                    }
                ) {
                    Text(
                        "%",
                        color = if (typePropina == 1) Color.White else BlueDark,
                        fontFamily = redhatFamily,
                        fontSize = 20.sp,
                        lineHeight = 12.sp
                    )
                }
                Button(
                    elevation = ButtonDefaults.elevation(0.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp)
                        .height(41.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(
                        width = 0.dp,
                        color = if (typePropina == 2) OrangeDark else BlueLight
                    ),
                    colors = ButtonDefaults.buttonColors(backgroundColor = if (typePropina == 2) OrangeDark else BlueLight),
                    onClick = {
                        showModal = true
                        typePropina = 2
                        onTypePropina(0)
                        onSelectValue(0)
                    }
                ) {
                    Text(
                        "$",
                        color = if (typePropina == 2) Color.White else BlueDark,
                        fontFamily = redhatFamily,
                        fontSize = 20.sp,
                        lineHeight = 12.sp
                    )
                }
            }
        }

    }
}