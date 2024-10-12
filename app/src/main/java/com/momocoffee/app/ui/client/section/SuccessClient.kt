package com.momocoffee.app.ui.client.section

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.momocoffee.app.R
import com.momocoffee.app.navigation.Destination
import com.momocoffee.app.ui.theme.BlueDark
import kotlinx.coroutines.delay

@Composable
fun SuccessClient(controller: NavController){
    LaunchedEffect(Unit){
        delay(4000)
        controller.navigate(Destination.Category.route)
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
                .fillMaxSize()
                .zIndex(88f),
            color = BlueDark
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    modifier = Modifier.width(180.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    stringResource(id = R.string.welcome),
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight(700)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.check_icon),
                    contentDescription = stringResource(id = R.string.momo_coffe),
                    modifier = Modifier.width(250.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    stringResource(id = R.string.welcome_success_fully_register_text_large),
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight(400),
                    textAlign = TextAlign.Center
                )

            }
        }
    }

}