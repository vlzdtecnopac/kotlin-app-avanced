package com.momocoffee.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.momocoffee.app.R
import com.momocoffee.app.network.response.ProductsItem
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.redhatFamily
import com.momocoffee.app.ui.theme.stacionFamily

@Composable
fun CardProduct(navController: NavController, selectCategory: String? = "", product: ProductsItem) {

    Column(
        modifier = Modifier
            .padding(5.dp)
            .clickable {
                navController.navigate("product/${selectCategory}/${product.productID}")
            }
    ) {

        if (product.image.isNullOrEmpty()) {
            Image(
                painter = painterResource(id = R.drawable.no_found),
                contentDescription = stringResource(id = R.string.momo_coffe),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp
                        )
                    )
                    .fillMaxWidth()
            )

        } else {
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
                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp
                        )
                    )
                    .fillMaxWidth(),
                error = painterResource(R.drawable.no_found),
            )

        }

        Box(
            modifier = Modifier
                .background(BlueDark)
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            if (product.nameProduct.isNullOrEmpty()) {
                Text("\$ 0", color = Color.White, fontFamily = stacionFamily)
            }else{
                Text("\$ ${product.price}", color = Color.White, fontFamily = stacionFamily)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (product.nameProduct.isNullOrEmpty()) {
            Text("Product", color = BlueDark, fontFamily = redhatFamily, fontSize = 18.sp, fontWeight = FontWeight(700))
        }else{
            Text(product.nameProduct, color = BlueDark, fontFamily = redhatFamily, fontSize = 18.sp, fontWeight = FontWeight(700))
        }

        Spacer(modifier = Modifier.height(8.dp))
    }


}