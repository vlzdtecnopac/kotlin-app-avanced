package com.momocoffee.app.ui.components.headers

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.momocoffee.app.BuildConfig
import com.momocoffee.app.R
import com.momocoffee.app.navigation.Destination
import com.momocoffee.app.ui.components.Sidebar
import com.momocoffee.app.ui.components.VerifyKiosko
import com.momocoffee.app.ui.components.cart.BtnCart
import com.momocoffee.app.ui.components.cart.Cart
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.viewmodel.CartViewModel

@Composable
fun HeaderCategory(navController: NavController, cartViewModel: CartViewModel, visibleCart: Boolean = false) {
    var textButton = stringResource(id = R.string.txt_back)

    VerifyKiosko(navController)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BlueDark)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Button(
            modifier = Modifier
                .width(180.dp)
                .border(0.dp, Color.Transparent, shape = RoundedCornerShape(10.dp)),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 20.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(width = 0.dp, color = Color.Transparent),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            onClick = {
                navController.navigate(Destination.Category.route)
            },
            elevation = ButtonDefaults.elevation(0.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.KeyboardArrowLeft,
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    tint = Color.White,
                    modifier = Modifier.size(width = 18.dp, height = 18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    textButton,
                    color = Color.White,
                    fontFamily = redhatFamily,
                    fontSize = 18.sp
                )
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.momo_coffe),
                modifier = Modifier.width(135.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            if(BuildConfig.BUILD_TYPE === "debug"){
                Box(
                    modifier = Modifier
                        .background(OrangeDark)
                        .padding(3.dp)
                        .clip(shape = RoundedCornerShape(12.dp))
                ){
                    androidx.compose.material3.Text(
                        "Dev",
                        fontSize = 14.sp,
                        color = Color.White,
                        fontFamily = redhatFamily,
                        fontWeight = FontWeight(600)
                    )
                }
            }
        }

        Sidebar(navController)
        if(visibleCart) {
            Cart(navController, cartViewModel, btnStyleOne = false)
        }
    }
}