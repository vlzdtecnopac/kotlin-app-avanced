package com.momocoffee.app.ui.chekout.section

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.momocoffee.app.App
import com.momocoffee.app.R
import com.momocoffee.app.navigation.Destination
import com.momocoffee.app.network.dto.TurnoRequest
import com.momocoffee.app.network.dto.UpdateBilingRequest
import com.momocoffee.app.ui.chekout.components.OutlineTextField
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.BlueLight
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.ui.theme.stacionFamily
import com.momocoffee.app.viewmodel.BuildingViewModel
import com.momocoffee.app.viewmodel.CartViewModel
import com.momocoffee.app.viewmodel.TurnoViewModel

@Composable
fun SuccessFullyPayment(
    navController: NavController,
    cartViewModel: CartViewModel,
    resetState: () -> Unit,
    buildingViewModel: BuildingViewModel = viewModel(),
    turnoViewModel: TurnoViewModel = viewModel()
){
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
    val bilding_id = sharedPreferences.getString("bildingId", null) ?: ""
    val nameClient = sharedPreferences.getString("nameClient", null) ?: ""

    var textNameState by remember { mutableStateOf(value = nameClient) }
    var navState by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val stringEnterPedidoRepresent = stringResource(id = R.string.enter_name_receive_pedido)
    fun changeHandleSuccess(){
        if(textNameState.isNullOrEmpty()){
            Toast.makeText(
                context,
                stringEnterPedidoRepresent,
                Toast.LENGTH_LONG
            )
                .show()
            return
        }

        buildingViewModel.updatePayment(
            bilding_id, UpdateBilingRequest(
                name = textNameState,
            )
        )

        turnoViewModel.getTurnByBilding(bilding_id, "completed")
    }


    LaunchedEffect(turnoViewModel.turnoBildingState.value){
        turnoViewModel.turnoBildingState.value?.let { result ->
            when{
                result.isSuccess -> {
                    val bildingResponse = result.getOrThrow()
                    turnoViewModel.updateTurnoPedido(bildingResponse.items.first().turnoID, textNameState)
                    cartViewModel.clearAllCart()
                    val editor = sharedPreferences.edit()
                    editor.remove("clientId")
                    editor.remove("bildingId")
                    editor.remove("cuponName")
                    editor.remove("nameClient")
                    editor.apply()
                    resetState()

                    if(navState) {
                        navController.navigate(Destination.RegisterClient.route)
                    }else{
                        navController.navigate(Destination.OrderHere.route)
                    }

                }
                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    resetState()
                    Log.d("Result.BildingViewModel", exception.toString())
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BlueDark)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = stringResource(id = R.string.momo_coffe),
            modifier = Modifier.width(130.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            stringResource(id = R.string.congratulations_text),
            color = Color.White,
            fontSize = 24.sp,
            fontFamily = redhatFamily,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Image(
            painter = painterResource(id = R.drawable.momo_coffe_mug),
            contentDescription = stringResource(id = R.string.momo_coffe),
            modifier = Modifier.width(110.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            stringResource(id = R.string.thank_purchase_text),
            color = Color.White,
            fontSize = 22.sp,
            fontFamily = stacionFamily,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.widthIn(min=230.dp, max = 380.dp),
            text=stringResource(id = R.string.in_just_minutes_momo),
            color = Color.White,
            fontSize = 18.sp,
            fontFamily = stacionFamily,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            modifier = Modifier.widthIn(min=230.dp, max = 450.dp),
            text=stringResource(id = R.string.help_name_call_register),
            color = Color.White,
            fontSize = 16.sp,
            fontFamily = stacionFamily,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.widthIn(min=220.dp, max=410.dp)
        ){
            OutlineTextField(
                label = stringResource(id = R.string.text_name),
                placeholder = stringResource(id = R.string.text_name),
                icon = R.drawable.user,
                keyboardType = KeyboardType.Text,
                textValue = textNameState,
                onValueChange = { textNameState = it },
                onClickButton = { textNameState = "" },
                borderColor = Color.White,
                onDone = {
                    focusManager.clearFocus()
                }
            )
            Spacer(modifier = Modifier.height(20.dp))

        }
        Row(
            modifier = Modifier.widthIn(min=220.dp, max=610.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    navState = false
                    changeHandleSuccess()
                },
                modifier = Modifier
                    .weight(0.5f)
                    .height(60.dp)
                    .padding(horizontal = 5.dp)
                    .border(
                        width = 2.dp,
                        color = BlueDark,
                        shape = RoundedCornerShape(44.dp)
                    ),
                shape = RoundedCornerShape(44.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = BlueLight,
                    disabledBackgroundColor = BlueLight,
                    disabledContentColor = BlueLight
                ),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.end_order_text),
                    fontSize = 22.sp,
                    color = BlueDark,
                    fontFamily = redhatFamily,
                    fontWeight = FontWeight(700)
                )
            }
            //Spacer(modifier = Modifier.width(4.dp))
            /*Button(
                onClick = {
                    navState = true
                    changeHandleSuccess()
                },
                modifier = Modifier
                    .weight(0.5f)
                    .height(60.dp)
                    .padding(horizontal = 5.dp)
                    .border(
                        width = 2.dp,
                        color = OrangeDark,
                        shape = RoundedCornerShape(44.dp)
                    ),
                shape = RoundedCornerShape(44.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = OrangeDark,
                    disabledBackgroundColor = OrangeDark,
                    disabledContentColor = OrangeDark
                ),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp,
                    hoveredElevation = 0.dp,
                    focusedElevation = 0.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.register_text),
                    fontSize = 22.sp,
                    color = Color.White,
                    fontFamily = redhatFamily,
                    fontWeight = FontWeight(700)
                )
            }*/
        }
    }
}