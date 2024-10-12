package com.momocoffee.app.ui.chekout.components

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.momocoffee.app.App
import com.momocoffee.app.R
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.BlueLight
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.viewmodel.BuildingViewModel

@Composable
fun ConfirmPayment(
    title: String,
    subTitle: String,
    onSelect: (Boolean) -> Unit,
    buildingViewModel: BuildingViewModel = viewModel()
) {
    val context = LocalContext.current
    val sharedPreferences = App.instance.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
    val bildingId = sharedPreferences.getString("bildingId", null) ?: ""

    val stringPayment = stringResource(id = R.string.wait_pedido_try_again)

    LaunchedEffect(buildingViewModel.buildingTurnoResultState.value) {
        buildingViewModel.buildingTurnoResultState.value?.let { result ->
            when {
                result.isSuccess -> {
                    val validPaymentResponse = result.getOrThrow()
                    if (validPaymentResponse.items.isNullOrEmpty()) {
                        onSelect(false);
                        Toast.makeText(context, stringPayment, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        if (validPaymentResponse.items[0].state == "completed") {
                            onSelect(true);
                        }
                    }

                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Toast.makeText(
                        context,
                        "Error en el proceso del recibo del pedido.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Log.d("Result.ClientViewModel", exception.toString())
                    onSelect(false);
                }
            }
        }
    }


    Dialog(
        onDismissRequest = {},
        DialogProperties(
            usePlatformDefaultWidth = true
        )
    ) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .padding(0.dp)
                .widthIn(min = 460.dp, max = 830.dp)
                .heightIn(min = 540.dp, max = 560.dp)
                .zIndex(88f),
            color = BlueLight
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
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
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    modifier = Modifier.widthIn(min = 180.dp, max = 230.dp),
                    text = stringResource(id = R.string.pass_approval),
                    fontSize = 16.sp,
                    color = BlueDark,
                    fontFamily = redhatFamily,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        buildingViewModel.validPayment(bildingId, "completed")
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .padding(horizontal = 5.dp)
                        .border(
                            width = 0.6.dp,
                            color = BlueDark,
                            shape = RoundedCornerShape(14.dp)
                        ),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = BlueLight,
                        disabledBackgroundColor = BlueLight,
                        disabledContentColor = BlueLight
                    ),
                    elevation = ButtonDefaults.elevation(0.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.confirm_payment),
                        fontWeight = FontWeight(700),
                        fontSize = 16.sp,
                        color = BlueDark,
                        fontFamily = redhatFamily,
                    )
                }
            }

        }

    }
}