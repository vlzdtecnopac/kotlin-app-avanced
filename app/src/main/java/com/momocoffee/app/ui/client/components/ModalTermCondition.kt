package com.momocoffee.app.ui.client.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.momocoffee.app.R
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.BlueLight
import com.momocoffee.app.ui.theme.OrangeDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.ui.theme.stacionFamily

data class SectionItem(val id: Int, val text: String)

@Composable
fun ModalTermCondition(
    onClose: () -> Unit
){
    val sectionItems = listOf(
        SectionItem(1, stringResource(id = R.string.data_service_incredible_offers)),
        SectionItem(2, stringResource(id = R.string.security_data_safety)),
        SectionItem(3, stringResource(id = R.string.privacy_not_share_info) ),
        SectionItem(4, stringResource(id = R.string.yuor_are_control_acces)),
    )
    Dialog(
        onDismissRequest = {},
        DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {Surface(
        modifier = Modifier.fillMaxSize(),
        color = BlueDark
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.momo_coffe_mug),
                contentDescription = stringResource(id = R.string.momo_coffe),
                modifier = Modifier.width(120.dp),
                contentScale = ContentScale.Crop,
            )

            Text(
                text = stringResource(id = R.string.hello_momo_lovers),
                fontSize = 20.sp,
                color = Color.White,
                fontFamily = redhatFamily,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = R.string.momo_privacity),
                fontSize = 18.sp,
                color = Color.White,
                fontFamily = redhatFamily,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = stringResource(id = R.string.here_data_drive),
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = redhatFamily,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier.widthIn(min = 290.dp , max = 680.dp),
            ){
                SectionList(sectionItems = sectionItems)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Gracias por confiar en MOMO.",
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = redhatFamily,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = R.string.have_questions_help_you),
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = redhatFamily,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    onClose()
                },
                modifier = Modifier
                    .width(180.dp)
                    .padding(horizontal = 5.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = OrangeDark,
                    disabledBackgroundColor = OrangeDark,
                    disabledContentColor = BlueLight
                )
            ) {
                Text(
                    text = stringResource(id = R.string.txt_continue),
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = redhatFamily,
                )
            }

        }
    }
    }
}

@Composable
fun SectionList(sectionItems: List<SectionItem>) {
    
    LazyColumn {
        items(sectionItems.size) { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = sectionItems[item].id.toString(),
                    modifier = Modifier
                        .weight(0.05f)
                        .padding(vertical = 2.dp, horizontal = 10.dp),
                    color = Color.White,
                    fontFamily = stacionFamily
                )
                Text(
                    text = sectionItems[item].text,
                    modifier = Modifier
                        .weight(0.9f)
                        .padding(vertical = 5.dp, horizontal = 10.dp),
                    color = Color.White,
                    fontFamily = redhatFamily,
                    fontSize = 16.sp
                )
            }

        }
    }
}


