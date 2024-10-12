package com.momocoffee.app.ui.components.cart

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.momocoffee.app.R
import com.momocoffee.app.navigation.Destination
import com.momocoffee.app.network.data.CartProductEdit
import com.momocoffee.app.network.data.CartState
import com.momocoffee.app.network.database.Cart
import com.momocoffee.app.ui.components.SolidLineDivider
import com.momocoffee.app.ui.theme.*

import com.momocoffee.app.viewmodel.CartViewModel
import com.momocoffee.app.viewmodel.ItemModifier
import com.spr.jetpack_loading.components.indicators.BallClipRotatePulseIndicator
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


@Composable
fun ContentCart(
    onClickOutside: () -> Unit,
    navController: NavController,
    cartViewModel: CartViewModel,
    state: CartState
) {

    val loading = cartViewModel.loadingCartState.value

    LaunchedEffect(Unit) {
        if (state.carts.isNullOrEmpty()) {
            cartViewModel.loadingCartState.value = false
        } else {
            cartViewModel.countTotal()
            cartViewModel.priceSubTotal()
        }

        cartViewModel.loadingCartState.value = false
    }

    Box {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.8f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        stringResource(id = R.string.resum_pedido),
                        color = Color.Black,
                        fontFamily = redhatFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight(700)
                    )

                    if(cartViewModel.countCartState.value == 1) {
                        Text(
                            "${cartViewModel.countCartState.value ?: 0} " + stringResource(id = R.string.product),
                            color = Color.Black,
                            fontFamily = redhatFamily,
                            fontSize = 12.sp
                        )
                    }else{
                        Text(
                            "${cartViewModel.countCartState.value ?: 0} " + stringResource(id = R.string.products),
                            color = Color.Black,
                            fontFamily = redhatFamily,
                            fontSize = 12.sp
                        )
                    }

                }
                Column(
                    modifier = Modifier
                        .weight(0.2f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ) {
                    IconButton(
                        modifier = Modifier
                            .width(32.dp)
                            .height(32.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .background(OrangeDark)
                            .border(
                                width = 0.6.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(50.dp)
                            )
                            .padding(4.dp),
                        onClick = onClickOutside
                    ) {
                        Icon(
                            Icons.Rounded.Close,
                            contentDescription = stringResource(id = R.string.momo_coffe),
                            tint = Color.White,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

            }

            LazyColumn(modifier = Modifier.fillMaxHeight(0.8f)) {
                itemsIndexed(items = state.carts) { index, item ->
                    ProductCart(item, cartViewModel)
                }
            }

            TotalPayment(navController, cartViewModel)

        }
        if (loading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlueDarkTransparent)
            ) {
                BallClipRotatePulseIndicator()
            }
        }
    }

}

@Composable
fun ProductCart(
    product: Cart,
    cartViewModel: CartViewModel,
) {

    var count by remember { mutableStateOf(value = 1) }
    val optionsGeneral = mutableListOf<String?>()
    val itemsModifiersOptions = parseItemModifiers(product.modifiersOptions)
    val itemsModifiersList = parseObject(product.modifiersList)
    for ((key, value) in itemsModifiersOptions) {
        optionsGeneral.add(value.name)
    }

    LaunchedEffect(Unit) {
        count = product.countProduct
    }

    Column(
        modifier = Modifier.padding(horizontal = 8.dp)
    ){
        Row(modifier = Modifier.padding(8.dp)) {
            Column {
                if (product.imageProduct.isNullOrBlank()) {
                    Image(
                        painter = painterResource(id = R.drawable.no_found),
                        contentDescription = stringResource(id = R.string.momo_coffe),
                        modifier = Modifier
                            .clip(
                                shape = RoundedCornerShape(8.dp)
                            )
                            .width(70.dp)
                            .height(70.dp),
                        contentScale = ContentScale.Crop,
                    )
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(product.imageProduct)
                            .crossfade(true)
                            .build(),
                        placeholder = painterResource(R.drawable.no_found),
                        contentDescription = stringResource(R.string.momo_coffe),
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .clip(
                                shape = RoundedCornerShape(8.dp)
                            )
                            .width(70.dp)
                            .height(70.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BtnCart(
                        onClickButton = {
                            if (count > 1) {
                                count -= 1
                                cartViewModel.editCart(
                                    CartProductEdit(
                                        product.id,
                                        countProduct = count,
                                        (product.priceProduct.toInt() + product.priceExtras.toInt()) * count
                                    )
                                )
                            }
                        },
                        icon = R.drawable.menus_icon,
                        color = Color.White,
                        iconColor = OrangeDark,
                        borderColor = OrangeDark
                    )
                    Text(
                        count.toString(),
                        modifier = Modifier.width(22.dp),
                        textAlign = TextAlign.Center,
                        fontFamily = stacionFamily
                    )
                    BtnCart(
                        onClickButton = {
                            count += 1
                            cartViewModel.editCart(
                                CartProductEdit(
                                    product.id,
                                    countProduct = count,
                                    (product.priceProduct.toInt() + product.priceExtras.toInt()) * count
                                )
                            )
                        },
                        icon = R.drawable.pluss_icon,
                        color = OrangeDark,
                        iconColor = Color.White,
                        borderColor = OrangeDark
                    )
                }
            }
            Column(modifier = Modifier.padding(start = 5.dp)) {

                Text(
                    product.titleProduct,
                    fontFamily = redhatFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700)
                )
                Text(
                    optionsGeneral.joinToString(" | "),
                    fontFamily = redhatFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight(400),
                    color = BlueDark
                )
                Spacer(modifier = Modifier.height(8.dp))
                itemsModifiersList.forEach {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            it.name,
                            modifier = Modifier.weight(0.6f),
                            fontFamily = redhatFamily,
                            fontSize = 12.sp
                        )
                        Text(
                            "$ ${it.price}",
                            modifier = Modifier.weight(0.4f),
                            fontFamily = redhatFamily,
                            fontSize = 12.sp
                        )
                    }
                }

            }
        }
        Row(
            modifier = Modifier.padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.weight(0.8f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    "\$ ${(product.priceProductMod.toInt())}",
                    fontSize = 20.sp,
                    fontFamily = stacionFamily
                )
            }
            Box(
                modifier = Modifier.weight(0.2f),
                contentAlignment = Alignment.Center
            ) {
                BtnCart(
                    onClickButton = {
                        cartViewModel.deleteProduct(product)
                    },
                    icon = R.drawable.trash_icon,
                    color = Color.White,
                    iconColor = OrangeDark,
                    borderColor = OrangeDark
                )
            }
        }
        SolidLineDivider(color= GrayDark)
    }

}


@Composable
fun TotalPayment(navController: NavController, cartViewModel: CartViewModel) {
    val context = LocalContext.current

    val symbols = DecimalFormatSymbols().apply {
        decimalSeparator = '.'
    }

    val decimalFormat = DecimalFormat("#.##", symbols)


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .background(GrayLight)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if(cartViewModel.countCartState.value == 1) {
                Text(
                    "Subtotal (${cartViewModel.countCartState.value ?: 0} producto)",
                    fontSize = 16.sp,
                    fontFamily = redhatFamily
                )
            }else{
                Text(
                    "Subtotal (${cartViewModel.countCartState.value ?: 0} productos)",
                    fontSize = 16.sp,
                    fontFamily = redhatFamily
                )
            }


            Spacer(modifier = Modifier.width(8.dp))

            Text(
                "$${decimalFormat.format(cartViewModel.stateTotalSub.value) ?: 0}",
                fontSize = 18.sp,
                fontFamily = stacionFamily
            )

        }
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = OrangeDark),
            onClick = {
                if(cartViewModel.countCartState.value > 0){
                    navController.navigate(Destination.Checkout.route)
                }else{
                    Toast.makeText(
                        context,
                        R.string.required_cart_not_null,
                        Toast.LENGTH_LONG
                    ).show()
                }

            }) {
            Text(stringResource(id = R.string.continue_payment), fontSize = 20.sp ,color = Color.White, fontFamily = stacionFamily)
        }
    }

}

fun parseObject(input: String): List<ItemModifier> {

    val regex = Regex("""ItemModifier\(id=(.*?), name=(.*?), price=(\d+)\)""")

    val matches = regex.findAll(input)
    val itemModifiers = matches.map { matchResult ->
        val (id, name, price) = matchResult.destructured
        ItemModifier(id, name.trim(), price)
    }.toList()

    return itemModifiers
}

fun parseItemModifiers(input: String): Map<String, ItemModifier> {

    val itemModifiersRegex = Regex("([a-zA-Z]+)=ItemModifier\\(id=([^\\s,]+), name=([^,]+), price=(\\d+)\\)")
    val itemModifiers = mutableMapOf<String, ItemModifier>()

    itemModifiersRegex.findAll(input).forEach { matchResult ->
        val (key, id, name, price) = matchResult.destructured
        itemModifiers[key] = ItemModifier(id, name, price)
    }

    return itemModifiers
}

