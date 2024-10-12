package com.momocoffee.app.ui.chekout.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.momocoffee.app.R
import com.momocoffee.app.network.database.Cart
import com.momocoffee.app.ui.components.cart.BtnCart
import com.momocoffee.app.ui.components.cart.parseItemModifiers
import com.momocoffee.app.ui.components.cart.parseObject
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.BlueLight
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.ui.theme.stacionFamily
import com.momocoffee.app.viewmodel.CartViewModel

@Composable
fun ProductCartCheckout(
    product: Cart,
    cartViewModel: CartViewModel
) {
    val optionsSize = mutableListOf<String?>()
    val itemsModifiersOptions = parseItemModifiers(product.modifiersOptions)
    val itemsModifiersList = parseObject(product.modifiersList)

    for ((key, value) in itemsModifiersOptions) {
        optionsSize.add(value.name)
    }

    Card {
            Column {
                Row(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                    Column(
                        modifier = Modifier.weight(0.3f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (product.imageProduct.isNotEmpty()) {
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
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.no_found),
                                contentDescription = stringResource(id = R.string.momo_coffe),
                                modifier = Modifier
                                    .clip(
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .width(70.dp)
                                    .height(70.dp),
                                contentScale = ContentScale.FillWidth,
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Column(
                            modifier = Modifier.clip(shape = RoundedCornerShape(8.dp)),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                "x${product.countProduct}",
                                modifier = Modifier
                                    .width(30.dp)
                                    .height(20.dp)
                                    .background(BlueLight)
                                    .padding(top = 4.dp),
                                textAlign = TextAlign.Center,
                                fontFamily = stacionFamily,
                            )

                        }
                    }
                    Column(modifier = Modifier.padding(start = 5.dp).weight(0.8f)) {

                        Text(
                            product.titleProduct,
                            fontFamily = redhatFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight(700)
                        )
                        Text(
                            optionsSize.joinToString(" | "),
                            fontFamily = redhatFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(400),
                            color = BlueDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        itemsModifiersList.forEach {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp)
                            ) {
                                Text(
                                    it.name,
                                    modifier = Modifier.weight(0.6f),
                                    fontFamily = redhatFamily,
                                    fontSize = 12.sp
                                )
                                Text(
                                    it.price,
                                    modifier = Modifier.weight(0.4f),
                                    fontFamily = redhatFamily,
                                    fontSize = 12.sp
                                )
                            }
                        }

                    }
                    Column(
                        modifier = Modifier.padding(start = 5.dp).weight(0.2f),
                        horizontalAlignment = Alignment.End
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
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(0.8f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            "\$ ${product.priceProductMod}",
                            fontSize = 20.sp,
                            fontFamily = stacionFamily
                        )
                    }
                }
            }
    }
    Spacer(modifier = Modifier.height(5.dp))

}