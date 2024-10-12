package com.momocoffee.app.ui.client.section

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.momocoffee.app.navigation.Destination
import com.momocoffee.app.network.dto.ClientSessionPhoneRequest
import com.momocoffee.app.ui.client.components.ButtonBack
import com.momocoffee.app.ui.client.components.DropDownOutline
import com.momocoffee.app.ui.client.components.OutTextField
import com.momocoffee.app.ui.login.components.ButtonField
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.ui.theme.stacionFamily
import com.momocoffee.app.viewmodel.ClientViewModel
import com.momocoffee.app.R
import com.momocoffee.app.ui.theme.BlueDarkTransparent
import com.spr.jetpack_loading.components.indicators.BallClipRotatePulseIndicator


@Composable
fun StartByPhone(navController: NavController, clientViewModel: ClientViewModel =  viewModel()) {
    val context = LocalContext.current
    var phone by remember { mutableStateOf(value = "") }
    val selected = remember { mutableStateOf(value = "") }
    val selectedLabel = remember { mutableStateOf(value = "") }
    val focusManager = LocalFocusManager.current
    val loading = clientViewModel.loadingState.value

    val isValidate by derivedStateOf { phone.isNotBlank() && selected.value.isNotBlank() }

    LaunchedEffect(clientViewModel.clientResultSession.value){
        clientViewModel.clientResultSession.value?.let{ result ->
            when {
                result.isSuccess -> {
                    navController.navigate(Destination.Category.route)
                }
                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Toast.makeText(context, R.string.start_failed_session, Toast.LENGTH_LONG)
                        .show()
                    Log.d("Result.ClientViewModel", exception.toString())
                }
            }
        }
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
            Box{
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
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box {}
                        Column(
                            modifier = Modifier
                                .widthIn(0.dp, 480.dp),
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
                                stringResource(id = R.string.enter_yuor_email),
                                color = Color.White,
                                fontSize = 20.sp,
                                fontFamily = stacionFamily,
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(0.3f)
                                ) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    DropDownOutline(
                                        selected = selected,
                                        selectedLabel = selectedLabel
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .weight(0.8f)
                                ) {
                                    OutTextField(
                                        textValue = phone,
                                        onValueChange = { phone = it },
                                        onClickButton = { phone = "" },
                                        text = stringResource(id = R.string.phone),
                                        keyboardType = KeyboardType.Phone,
                                        icon = painterResource(R.drawable.phone),
                                        onDone = {
                                            focusManager.clearFocus()
                                        }
                                    )
                                }

                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Spacer(modifier = Modifier.height(30.dp))
                                ButtonField(
                                    text = stringResource(id = R.string.enter),
                                    onclick = {
                                        if(isValidate){
                                            clientViewModel.getSessionPhoneClient(
                                                ClientSessionPhoneRequest(
                                                    phone,
                                                    code = selected.value
                                                )
                                            )
                                        }else{
                                            Toast.makeText(
                                                context,
                                                R.string.required_inputs_phone_and_code,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                    },
                                    enabled = true
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.End
                        ) {
                            ButtonBack(onclick = {
                                navController.popBackStack()
                            })
                        }
                    }

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



    }

}