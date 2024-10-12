package com.momocoffee.app.ui.category.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.momocoffee.app.App
import com.momocoffee.app.BuildConfig
import com.momocoffee.app.R
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.redhatFamily


@Composable
fun BtnOutlineCategory(
    onclick: () -> Unit,
    icon: String,
    text: String
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BlueDark)
            .border(
                width = 2.0.dp,
                color = Color.White,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clickable { onclick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        if(icon.isNullOrEmpty()){
            Image(
                painter = painterResource(id = R.drawable.mug_icon),
                contentDescription = stringResource(id = R.string.momo_coffe),
                modifier = Modifier
                    .width(70.dp)
                    .height(75.dp)
            )
        }else{
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(App.context)
                    .data("${BuildConfig.API_BASE_URL}/uploads/$icon")
                    .error(R.drawable.mug_icon) // Imagen por defecto en caso de error
                    .build()
            )

            Image(
                painter = painter,
                contentDescription = stringResource(id = R.string.momo_coffe),
                modifier = Modifier
                    .width(70.dp)
                    .height(75.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(text, color = Color.White, fontSize = 22.sp, fontFamily = redhatFamily, fontWeight = FontWeight(700), textAlign = TextAlign.Center )
    }
}