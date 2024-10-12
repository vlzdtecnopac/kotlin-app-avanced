package com.momocoffee.app.ui.zettle


import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.momocoffee.app.navigation.Destination
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.BlueLight
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily

import com.momocoffee.app.R
@Composable
fun ZettlePayment(navController: NavHostController){
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(BlueDark)
    ) {
        Image(
            painter = painterResource(id = R.drawable.izettle_logo),
            contentDescription = stringResource(id = R.string.momo_coffe),
            modifier = Modifier.width(190.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(modifier = Modifier.height(10.dp))
        Text("Antes de comenzar comprueba el estado del sistema de pagos con Zettle MOMO.", color = Color.White, fontFamily = redhatFamily, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = {
                try {
                    val intent = Intent()
                    intent.component = ComponentName("com.momocoffe.izettlemomo", "com.momocoffe.izettlemomo.MainActivity")
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.e("TAG", "Error al abrir la aplicación", e)
                    Toast.makeText(context, "Falta por instalar MOMO Zettle Payment en tu dispositivo.", Toast.LENGTH_LONG)
                        .show()
                }

            },
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(60.dp)
                .padding(horizontal = 5.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = OrangeDark,
                disabledBackgroundColor = OrangeDark,
                disabledContentColor = OrangeDark
            )
        ) {
            Text(
                text = stringResource(id = R.string.check_zettle),
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = redhatFamily,
                fontWeight = FontWeight(700)
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            onClick = {
                navController.navigate(Destination.Login.route)
            },
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .height(60.dp)
                .padding(horizontal = 5.dp),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = BlueLight,
                disabledBackgroundColor = BlueLight,
                disabledContentColor = BlueLight
            )
        ) {
            Text(
                text = stringResource(id = R.string.not_want),
                fontSize = 18.sp,
                color = BlueDark,
                fontFamily = redhatFamily,
                fontWeight = FontWeight(700)
            )
        }
    }

}

