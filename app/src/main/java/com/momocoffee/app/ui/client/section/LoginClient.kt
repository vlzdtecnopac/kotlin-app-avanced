package com.momocoffee.app.ui.client.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.momocoffee.app.navigation.Destination
import com.momocoffee.app.ui.client.components.ButtonBack
import com.momocoffee.app.ui.client.components.ButtonOutLine
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.ui.theme.stacionFamily
import com.momocoffee.app.R

@Composable
fun LoginClient(navController: NavController) {
    var isModalPhone by remember { mutableStateOf(false) }
    var isModalEmail by remember { mutableStateOf(false) }
    if (isModalPhone) {
        StartByPhone(navController)
    }
    if (isModalEmail) {
        StartByEmail(navController)
    }
    Dialog(
        onDismissRequest = {},
        DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .padding(0.dp)
                .zIndex(88f),
            color = BlueDark
        ) {
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
                    verticalArrangement = Arrangement.SpaceAround,

                    ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = stringResource(id = R.string.momo_coffe),
                            modifier = Modifier.width(190.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            stringResource(id = R.string.start_session),
                            color = Color.White,
                            fontSize = 30.sp,
                            fontFamily = redhatFamily,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            stringResource(id = R.string.select_option_session),
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = stacionFamily,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ButtonOutLine(
                            text = stringResource(id = R.string.phone),
                            icon = R.drawable.phone,
                            onclick = {
                                isModalPhone = true
                            })
                        Spacer(modifier = Modifier.height(16.dp))
                        ButtonOutLine(
                            text = stringResource(id = R.string.mail),
                            icon = R.drawable.mail_icon,
                            onclick = {
                                isModalEmail = true
                            })
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalAlignment = Alignment.End
                    ) {

                        ButtonBack(onclick = {
                            navController.navigate(Destination.Client.route)
                        })
                    }
                }
            }
        }
    }
}