package com.momocoffee.app.ui.orderhere


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material.Button
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.momocoffee.app.BuildConfig
import com.momocoffee.app.ui.orderhere.components.ButtonField
import com.momocoffee.app.navigation.Destination
import com.momocoffee.app.ui.orderhere.components.ButtonEffecty
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.R
import com.momocoffee.app.network.response.ItemShopping
import com.momocoffee.app.ui.components.VerifyKiosko
import com.momocoffee.app.ui.orderhere.components.ContentNotEffecty
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.viewmodel.CartViewModel
import com.momocoffee.app.viewmodel.KioskoViewModel
import com.momocoffee.app.viewmodel.ShoppingViewModel


@Composable
fun OrderHere(
    navController: NavController,
    cartViewModel: CartViewModel,
    kioskoViewModel: KioskoViewModel = viewModel(),
    shoppingViewModel: ShoppingViewModel = viewModel(),
) {
    val context = LocalContext.current
    var effecty by remember { mutableStateOf(false) }
    val sharedPreferences = context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
    val shoppingId = sharedPreferences.getString("shoppingId", null) ?: ""
    val kioskoId = sharedPreferences.getString("kioskoId", null) ?: ""

    var isModalVisible by remember { mutableStateOf(false) }
    var kioskoIdentifierName by remember { mutableStateOf("") }

    if (isModalVisible) {
        ContentNotEffecty(navController)
    }


    LaunchedEffect(Unit) {
        shoppingViewModel.getShopping(shoppingId)
        kioskoViewModel.kioskoById(shoppingId, kioskoId)
    }

    LaunchedEffect(shoppingViewModel.shoppingResultState.value) {
        shoppingViewModel.shoppingResultState.value?.let { result ->
            when {
                result.isSuccess -> {
                    val shoppingResponse = result.getOrThrow()
                    if (shoppingResponse.items.isNotEmpty()) {
                        effecty = shoppingResponse.items.first().effecty
                    }
                }
                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Log.d("Result.ShoppingModel", exception.toString())
                }

                else -> {
                    Log.d("Result.ShoppingModel", "Error")
                }
            }
        }
    }

    LaunchedEffect(kioskoViewModel.kioskoByIdState.value) {
        kioskoViewModel.kioskoByIdState.value?.let { result ->
            when {
                result.isSuccess -> {
                    val configResponse = result.getOrThrow()
                    Log.d("Result.KioskoModelView", configResponse.toString())
                    if (configResponse.isNotEmpty()) {
                        kioskoIdentifierName = configResponse.first().nombre.uppercase()
                    }
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Log.d("Result.KioskoModelView", exception.toString())
                }
            }
        }
    }

    LaunchedEffect(kioskoViewModel.kioskoUpdateResultState.value) {
        kioskoViewModel.kioskoUpdateResultState.value.let { result ->
            if (result != null) {
                when {
                    result.isSuccess -> {
                        val configResponse = result.getOrThrow()
                        Log.d("Result.KioskoModelView", configResponse.toString())
                        val editor = sharedPreferences.edit()
                        editor.remove("kioskoId")
                        editor.remove("shoppingId")
                        editor.remove("token")
                        editor.remove("clientId")
                        editor.apply()

                        Toast.makeText(
                            context,
                            "Exit sesion",
                            Toast.LENGTH_SHORT
                        ).show()
                        //navController.navigate(Destination.Login.route)
                    }

                    result.isFailure -> {
                        val exception = result.exceptionOrNull()
                        Log.d("Result.KioskoModelView", exception.toString())
                    }
                }
            }
        }
    }

    VerifyKiosko(navController)

    Column(
        modifier = Modifier.background(BlueDark),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(
                    kioskoIdentifierName,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontFamily = redhatFamily,
                    fontWeight = FontWeight(600)
                )
                Spacer(modifier = Modifier.width(8.dp))
                if (BuildConfig.BUILD_TYPE === "debug") {
                    Box(
                        modifier = Modifier
                            .background(OrangeDark)
                            .padding(3.dp)
                            .clip(shape = RoundedCornerShape(12.dp))
                    ) {
                        Text(
                            "Dev",
                            fontSize = 14.sp,
                            color = Color.White,
                            fontFamily = redhatFamily,
                            fontWeight = FontWeight(600)
                        )
                    }
                }
            }

            Box {}
            Button(
                onClick = {
                    kioskoViewModel.updateKiosko(kioskoId = kioskoId, shoppingId, false)
                },
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .border(
                        width = 0.6.dp,
                        color = Color.White,
                        shape = RoundedCornerShape(34.dp)
                    ),
                shape = RoundedCornerShape(34.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = BlueDark,
                    disabledBackgroundColor = BlueDark,
                    disabledContentColor = BlueDark
                ),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.exit_icon),
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit,
                )
            }

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_momo_white),
                contentDescription = stringResource(id = R.string.momo_coffe),
                modifier = Modifier.width(310.dp)
            )
            Spacer(modifier = Modifier.height(25.dp))
            ButtonField(
                text = stringResource(id = R.string.orderhere),
                onclick = {
                    cartViewModel.clearAllCart()
                    val editor = sharedPreferences.edit()
                    editor.remove("clientId")
                    editor.remove("bildingId")
                    editor.remove("cuponName")
                    editor.remove("nameClient")
                    editor.apply()
                    navController.navigate(Destination.Category.route)
                },
                enabled = true
            )
        }


        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(7f)
                    .padding(20.dp)
            ) {
                /* Row {
                     ButtonLang(
                         onclick = {
                             RegionInternational.setLocale(context, "es")
                         },
                         text = stringResource(id = R.string.lang_es),
                         icon = painterResource(id = R.drawable.mexico_flag)
                     )
                     Spacer(modifier = Modifier.width(15.dp))
                     ButtonLang(
                         onclick = {
                             RegionInternational.setLocale(context, "en")
                         },
                         text = stringResource(id = R.string.lang_en),
                         icon = painterResource(id = R.drawable.usa_flag)
                     )
                 }*/
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(7f)
                    .padding(20.dp)
            ) {}
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(7f)
                    .padding(20.dp)
            ) {
                if (!effecty) {
                    ButtonEffecty(onclick = { isModalVisible = true })
                }
            }
        }
    }
}
