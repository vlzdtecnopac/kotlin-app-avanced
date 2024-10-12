package com.momocoffee.app.ui.products.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.momocoffee.app.R
import com.momocoffee.app.network.response.ProductsItem
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.redhatFamily

@Composable
fun DescriptionProduct(product: ProductsItem) {

    Column(
        verticalArrangement = Arrangement.Top
    ){
        if(product.image.isNullOrEmpty()){
            Image(
                painter = painterResource(id = R.drawable.no_found),
                contentDescription = stringResource(id = R.string.momo_coffe),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(8.dp)
                    )
                    .width(230.dp)
                    .height(230.dp)
            )
        }else{
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(product.image)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.no_found),
                contentDescription = stringResource(R.string.momo_coffe),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(8.dp)
                    )
                    .width(230.dp)
                    .height(230.dp)
            )

        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(product.nameProduct, fontSize = 22.sp, fontFamily = redhatFamily, fontWeight = FontWeight(700), color = BlueDark
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(product.description, fontSize = 14.sp, fontFamily = redhatFamily, fontWeight = FontWeight(400)
        )
    }
}