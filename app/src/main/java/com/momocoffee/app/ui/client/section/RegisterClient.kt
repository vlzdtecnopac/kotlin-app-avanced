package com.momocoffee.app.ui.client.section


import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.momocoffee.app.ui.theme.*
import com.momocoffee.app.R
import com.momocoffee.app.navigation.Destination
import com.momocoffee.app.network.dto.ClientReceptorEmailRequest
import com.momocoffee.app.network.dto.ClientReceptorSMSRequest
import com.momocoffee.app.network.dto.ClientRequest
import com.momocoffee.app.ui.client.components.*
import com.momocoffee.app.viewmodel.ClientViewModel
import com.momocoffee.app.viewmodel.EmailSmsViewModel
import com.spr.jetpack_loading.components.indicators.BallClipRotatePulseIndicator

@Composable
fun RegisterClient(navController: NavController,
                   clientViewModel: ClientViewModel = viewModel(),
                   emailSmsViewModel: EmailSmsViewModel = viewModel()) {
    val context = LocalContext.current
    val loading = clientViewModel.loadingState.value;

    var firstName by remember { mutableStateOf(value = "") }
    var lastName by remember { mutableStateOf(value = "") }

    val selected = remember { mutableStateOf(value = "") }
    val selectedLabel = remember { mutableStateOf(value = "") }
    val checkedState = remember { mutableStateOf(true) }
    val email = remember { mutableStateOf(value = "") }
    var phone = remember { mutableStateOf(value = "") }
    val stateRegister = remember { mutableStateOf(false) }
    val showModalTerm =  remember { mutableStateOf(false) }

    val isValidate by derivedStateOf { email.value.isNotBlank() && firstName.isNotBlank() && lastName.isNotBlank()  && phone.value.isNotBlank()  }

    LaunchedEffect(clientViewModel.clientResultState.value) {
        clientViewModel.clientResultState.value?.let { result ->
            when {
                result.isSuccess -> {
                    val newClientResponse = result.getOrThrow()
                    val newPhone = "${newClientResponse.code}${newClientResponse.phone}"
                    emailSmsViewModel.sendEmail(ClientReceptorEmailRequest("Activar Cuenta <davidvalenzuela@tecnopac.com.co>", newClientResponse.email, "Confirma Nueva Cuenta"))
                    emailSmsViewModel.sendSms(ClientReceptorSMSRequest("arn:aws:sns:us-east-1:946074075589:Momo", newPhone, "Momo", "Bienvenido a Momo Coffe, ingresando aqui puedes verificar tu cuenta. https://tudominio.com/verificar-cuenta" ))
                    Toast.makeText(context, R.string.create_success_fully_client, Toast.LENGTH_LONG)
                        .show()
                    clientViewModel.clientResultState.value = null
                    stateRegister.value = true
                }
                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Toast.makeText(context, R.string.error_register_client, Toast.LENGTH_LONG)
                        .show()
                    Log.d("Result.ViewModel", exception.toString())
                    stateRegister.value = false
                }
            }
        }
    }

    if(stateRegister.value){
        SuccessClient(navController)
    }else {

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
                if(showModalTerm.value){
                    ModalTermCondition(onClose = {
                        showModalTerm.value = false
                    })
                }

                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BlueDark),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = stringResource(id = R.string.momo_coffe),
                            modifier = Modifier.width(180.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            stringResource(id = R.string.register),
                            color = Color.White,
                            fontSize = 22.sp,
                            fontFamily = redhatFamily,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.width(520.dp)) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        stringResource(id = R.string.enter_data_person),
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontFamily = stacionFamily,
                                        fontWeight = FontWeight.Normal
                                    )
                                    OutlineTextField(
                                        label = stringResource(id = R.string.first_name),
                                        placeholder = stringResource(id = R.string.first_name),
                                        icon = R.drawable.user,
                                        keyboardType = KeyboardType.Text,
                                        textValue = firstName,
                                        onValueChange = { firstName = it },
                                        onClickButton = { firstName = "" },
                                      )
                                    OutlineTextField(
                                        label = stringResource(id = R.string.last_name),
                                        placeholder = stringResource(id = R.string.last_name),
                                        icon = R.drawable.user,
                                        keyboardType = KeyboardType.Text,
                                        textValue = lastName,
                                        onValueChange = { lastName = it },
                                        onClickButton = { lastName = "" },
                                       )
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
                                            PhoneOutlineTextField(
                                                label = stringResource(id = R.string.phone),
                                                placeholder = stringResource(id = R.string.phone),
                                                keyboardType = KeyboardType.Number,
                                                icon = R.drawable.phone,
                                                selected = phone)
                                        }
                                    }
                                    EmailOutlineTextField(
                                        label = stringResource(id = R.string.mail),
                                        placeholder = "tech@momocoffee.app",
                                        icon = R.drawable.mail_icon,
                                        keyboardType = KeyboardType.Email,
                                        selected = email
                                    )

                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = checkedState.value,
                                        onCheckedChange = { checkedState.value = it }
                                    )
                                    Text(
                                        modifier = Modifier.clickable {
                                            showModalTerm.value = true
                                        },
                                        text = stringResource(id = R.string.term_condition),
                                        color = Color.White
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .weight(0.5f)
                                    ) {
                                        ButtonBack(onclick = {
                                            navController.navigate(Destination.Client.route)
                                        })
                                    }
                                    Box(
                                        modifier = Modifier
                                            .weight(0.5f)
                                    ) {
                                        ButtonContinue(onclick = {
                                            Log.d("Register.Client", isValidate.toString())
                                            if (isValidate) {
                                                if (checkedState.value) {
                                                    clientViewModel.register(
                                                        clientDto = ClientRequest(
                                                            firstName,
                                                            lastName,
                                                            phone.value,
                                                            selected.value,
                                                            selectedLabel.value,
                                                            email.value
                                                        )
                                                    )
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        R.string.accept_term,
                                                        Toast.LENGTH_LONG
                                                    )
                                                        .show()
                                                }
                                            }
                                        })
                                    }
                                }
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
}