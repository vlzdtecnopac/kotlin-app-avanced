package com.momocoffee.app.ui.client

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DoDisturbOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.R
import com.momocoffee.app.navigation.Destination
import com.momocoffee.app.ui.client.components.ButtonOutLine
import com.momocoffee.app.ui.client.section.LoginClient
import com.momocoffee.app.ui.client.section.RegisterClient
import com.momocoffee.app.ui.components.VerifyKiosko
import com.momocoffee.app.ui.theme.*

@Composable
fun Client(navController: NavController){
    var isModalLogin by remember { mutableStateOf(false) }
    var isModalRegister by remember { mutableStateOf(false) }

    if (isModalLogin) {
        LoginClient(navController)
    }
    if(isModalRegister){
        RegisterClient(navController)
    }

    VerifyKiosko(navController)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(4f),
        ) {


            Image(
                painter = painterResource(id = R.drawable.client_session),
                contentDescription = stringResource(id = R.string.momo_coffe),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(7f)
                .background(BlueDark),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                horizontalArrangement = Arrangement.End
            ){
                Image(
                    painter =  painterResource(id = R.drawable.close_icon),
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    modifier = Modifier.size(width = 60.dp, height = 60.dp).clickable {
                        navController.navigate(Destination.OrderHere.route)
                    }
                )

            }
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(id = R.string.momo_coffe),
                modifier = Modifier.width(190.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                stringResource(id = R.string.welcome),
                color = Color.White,
                fontSize = 30.sp,
                fontFamily = redhatFamily,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                stringResource(id = R.string.register_discovery_benefits),
                color = Color.White,
                fontSize = 20.sp,
                fontFamily = stacionFamily,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            ButtonOutLine(text = stringResource(id = R.string.client_register), icon = R.drawable.user, onclick = {
                isModalLogin = true
            } )
           Spacer(modifier = Modifier.height(16.dp) )
            ButtonOutLine(text = stringResource(id = R.string.create_account), icon = R.drawable.user_square, onclick = {
                isModalRegister = true
            })
            Spacer(modifier = Modifier.height(16.dp))
            ButtonOutLine(text = stringResource(id = R.string.order_without_register), icon = R.drawable.user_octagon, onclick = {
                navController.navigate(Destination.Category.route)
            })

        }
    }
}