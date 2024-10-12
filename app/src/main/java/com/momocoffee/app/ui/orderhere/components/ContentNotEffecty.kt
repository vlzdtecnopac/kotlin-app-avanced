package com.momocoffee.app.ui.orderhere.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.momocoffee.app.R
import com.momocoffee.app.navigation.Destination
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.viewmodel.RegionInternational

@Composable
fun ContentNotEffecty(navController : NavController) {

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
            color = BlueDark ) {
            Column(
                modifier = Modifier
                    .fillMaxSize() // Fills entire Surface
                    .padding(vertical = 30.dp, horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(4f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(stringResource(id = R.string.not_effecty), color = Color.White, fontSize = 26.sp ,fontFamily = redhatFamily)
                    Spacer(modifier = Modifier.height(10.dp))
                    Image(
                        painter = painterResource(id = R.drawable.coffe_momo),
                        contentDescription = stringResource(id = R.string.momo_coffe),
                        modifier = Modifier.width(240.dp)
                    )
                }

                Column(
                    modifier =Modifier
                        .widthIn(0.dp, 852.dp).weight(8f),
                    verticalArrangement = Arrangement.Top
                ){

                    Text(
                        stringResource(id = R.string.text_payment),
                        color = Color.White, fontSize = 18.sp ,fontFamily = redhatFamily)

                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(3f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(5f)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                       /* Row{
                            ButtonLang(onclick= {
                                RegionInternational.setLocale(context, "es")
                            }, text= stringResource(id = R.string.lang_es), icon = painterResource(id = R.drawable.mexico_flag))
                            Spacer(modifier = Modifier.width(15.dp))
                            ButtonLang(onclick= {
                                RegionInternational.setLocale(context, "en")
                            }, text= stringResource(id = R.string.lang_en), icon = painterResource(id = R.drawable.usa_flag))
                        }*/
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(7f)
                            .padding(2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {}
                    Column(
                        horizontalAlignment =  Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(5f)
                            .padding(20.dp)
                    ) {
                        ButtonBack(
                            text = stringResource(id = R.string.txt_back),
                            onclick = { navController.navigate(Destination.OrderHere.route)
                        })
                    }
                }

            }
        }
    }

}

