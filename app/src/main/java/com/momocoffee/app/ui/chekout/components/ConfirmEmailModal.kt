package com.momocoffee.app.ui.chekout.components


import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.momocoffee.app.R
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.BlueDarkTransparent
import com.momocoffee.app.ui.theme.BlueLight
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.viewmodel.BuildingViewModel
import com.spr.jetpack_loading.components.indicators.BallClipRotatePulseIndicator

@Composable
fun ConfirmEmailModal(
    title: String,
    subTitle: String,
    onCancel: () -> Unit,
    onSelect: (String) -> Unit,
    buildingViewModel: BuildingViewModel = viewModel()
){
    val context = LocalContext.current
    var textEmailState by remember { mutableStateOf(value = "") }
    val focusManager = LocalFocusManager.current

    val stringEmailCorrect = stringResource(id = R.string.email_corresct_text)

    fun validateEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
        return email.matches(emailRegex)
    }

    Dialog(
        onDismissRequest = {},
        DialogProperties(
            usePlatformDefaultWidth = true
        )
    ) {
        Box {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .padding(0.dp)
                .widthIn(min = 460.dp, max = 830.dp)
                .height(560.dp)
                .zIndex(88f),
            color = BlueLight
        ) {

               Column(
                   modifier = Modifier
                       .verticalScroll(rememberScrollState())
                       .fillMaxSize()
                       .padding(10.dp)
                       .background(BlueLight),
                   horizontalAlignment = Alignment.CenterHorizontally,
                   verticalArrangement = Arrangement.Center
               ) {

                   Image(
                       painter = painterResource(id = R.drawable.clock_icon),
                       contentDescription = stringResource(id = R.string.momo_coffe),
                       modifier = Modifier.width(120.dp),
                       contentScale = ContentScale.FillWidth,
                   )
                   Spacer(modifier = Modifier.height(8.dp))
                   Text(
                       title,
                       fontFamily = redhatFamily,
                       fontSize = 28.sp,
                       fontWeight = FontWeight(400)
                   )
                   Spacer(modifier = Modifier.height(8.dp))
                   Text(
                       subTitle,
                       fontFamily = redhatFamily,
                       fontSize = 22.sp,
                       fontWeight = FontWeight(600)
                   )
                   Spacer(modifier = Modifier.height(8.dp))
                   Column(
                       modifier = Modifier
                           .widthIn(0.dp, 700.dp),
                       verticalArrangement = Arrangement.Center,
                       horizontalAlignment = Alignment.CenterHorizontally
                   ) {
                       OutlineTextField(
                           label = stringResource(id = R.string.email),
                           placeholder = stringResource(id = R.string.email),
                           icon = R.drawable.mail_icon,
                           keyboardType = KeyboardType.Email,
                           textValue = textEmailState,
                           onValueChange = { textEmailState = it },
                           onClickButton = { textEmailState = "" },
                           borderColor = BlueDark,
                           onDone = {
                               focusManager.clearFocus()
                           }
                       )
                       Spacer(modifier = Modifier.height(20.dp))
                       Row(
                           modifier = Modifier.fillMaxWidth(),
                           verticalAlignment = Alignment.CenterVertically,
                           horizontalArrangement = Arrangement.Center
                       ) {
                           Button(
                               onClick = {
                                   onCancel()
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
                                   text = stringResource(id = R.string.cancel),
                                   fontSize = 22.sp,
                                   color = BlueDark,
                                   fontFamily = redhatFamily,
                                   fontWeight = FontWeight(700)
                               )
                           }
                           Spacer(modifier = Modifier.width(8.dp))
                           Button(
                               onClick = {
                                   if(textEmailState.isNotEmpty() && validateEmail(textEmailState) ){
                                       onSelect(textEmailState)
                                   }else{
                                       Toast.makeText(context, stringEmailCorrect, Toast.LENGTH_SHORT)
                                           .show()
                                   }
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
                                   text = stringResource(id = R.string.txt_continue),
                                   fontSize = 22.sp,
                                   color = Color.White,
                                   fontFamily = redhatFamily,
                                   fontWeight = FontWeight(700)
                               )
                           }
                       }
                   }
               }
               if(buildingViewModel.loadingState.value){
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