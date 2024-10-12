package com.momocoffee.app.ui.components.cart


import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.momocoffee.app.R
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.stacionFamily
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.momocoffee.app.network.data.CartProductEdit
import com.momocoffee.app.network.data.CartState
import com.momocoffee.app.ui.theme.BlueDarkTransparent
import com.momocoffee.app.ui.theme.OrangeDarkLight
import com.momocoffee.app.viewmodel.CartViewModel

@Composable
fun Cart(navController: NavController,
         cartViewModel: CartViewModel,
         btnStyleOne:Boolean
         ) {

    val state = cartViewModel.state

    var showPopup by rememberSaveable {
        mutableStateOf(false)
    }

    Spacer(modifier = Modifier.width(10.dp))

    Box {

        if(btnStyleOne){
            BtnCart(
                onClickButton = {
                    showPopup = true
                },
                icon = R.drawable.cart_icon,
                color = OrangeDark,
                iconColor = Color.White,
                borderColor = OrangeDark,
                size = 40.dp
            )
        }else{
            BtnCartMomo(
                text = stringResource(id = R.string.show_cart),
                onclick = { showPopup = true },
                cartViewModel = cartViewModel,
                icon = R.drawable.cart_icon,
                state = state)
        }

        if (showPopup) {
            PopupBox() {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .fillMaxHeight()
                        .background(Color.White)
                        .align(Alignment.TopEnd)
                        .zIndex(40F),
                ) {
                        ContentCart(onClickOutside = { showPopup = false }, navController, cartViewModel ,state)
                }
            }
        }
    }
}

@Composable
fun PopupBox(
    content: @Composable () -> Unit) {
    // full screen background
    Dialog(
        onDismissRequest = {},
        DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BlueDarkTransparent)
                .zIndex(10F),
        ) {
            content()
        }
    }
}


@Composable
fun BtnCartMomo(
    text: String,
    icon: Int,
    onclick: () -> Unit,
    cartViewModel: CartViewModel,
    state: CartState
) {
    LaunchedEffect(state){
        cartViewModel.countTotal()
    }

    val modifierCard = Modifier
        .width(130.dp)
        .height(45.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(OrangeDark)
        .border(
            width = 1.2.dp,
            color = OrangeDark,
            shape = RoundedCornerShape(14.dp)
        )
        .padding(8.dp)

    Box {
        Row(
            modifier = modifierCard.clickable { onclick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text,
                color = Color.White,
                fontSize = 14.sp,
                fontFamily = stacionFamily,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
            Icon(
                painterResource(id = icon),
                contentDescription = stringResource(id = R.string.momo_coffe),
                tint = Color.White,
                modifier = Modifier.size(width = 30.dp, height = 30.dp)
            )
        }

        Box(
          modifier = Modifier
              .offset(x = 106.dp, y = 2.dp)
              .clip(RoundedCornerShape(14.dp))
              .background(OrangeDarkLight)
              .padding(1.dp)
              .width(20.dp)
              .height(20.dp),
            contentAlignment = Alignment.Center
        ){
            cartViewModel.countCartState.value.let {
                Text(
                    it.toString(),
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = stacionFamily,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    lineHeight = 11.sp
                )
            }

        }
    }
}
