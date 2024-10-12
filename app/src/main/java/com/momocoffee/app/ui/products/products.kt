package com.momocoffee.app.ui.products

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.momocoffee.app.R
import com.momocoffee.app.network.response.ProductsItem
import com.momocoffee.app.ui.components.CardProduct
import com.momocoffee.app.ui.components.Category
import com.momocoffee.app.ui.components.headers.HeaderCategoryProduct
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.BlueLight
import com.momocoffee.app.viewmodel.CartViewModel
import com.momocoffee.app.viewmodel.ProductsViewModel

@Composable
fun Products(
    navController: NavHostController,
    category: String? = "",
    subcategory: String? = "",
    viewModelCart: CartViewModel,
    productsViewModel: ProductsViewModel = viewModel()
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("momo_prefs", Context.MODE_PRIVATE)
    val preference_shopping_id = sharedPreferences.getString("shoppingId", null) ?: ""
    var productsItems: List<ProductsItem>? by remember { mutableStateOf(null) }
    var loading = productsViewModel.loadingState.value;


    LaunchedEffect(Unit) {
        if (category != null && subcategory != null) {
            productsViewModel.products(
                shopping_id = preference_shopping_id,
                categorys = category,
                subcategory = subcategory,
                state = true

            )
        } else {
            if (category != null) {
                productsViewModel.products(
                    shopping_id = preference_shopping_id,
                    categorys = category,
                    state = true
                )
            }
        }
    }

    LaunchedEffect(productsViewModel.productsResultState.value) {
        productsViewModel.productsResultState.value?.let { result ->
            when {
                result.isSuccess -> {
                    val response = result.getOrThrow()
                    productsItems = response.items.toList()
                }

                result.isFailure -> {
                    val exception = result.exceptionOrNull()
                    Log.e("Result.ProductsModelView", exception.toString())
                }
            }
        }
    }

        Column(
            modifier = Modifier
                .background(BlueLight),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BlueDark),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderCategoryProduct(navController, cartViewModel = viewModelCart)
                Category(navController, selectCategory = category, true, viewModelCart)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(10.dp))
            Box(modifier =  Modifier.widthIn(0.dp, 950.dp)) {
                if (!loading) {
                    if (productsItems.isNullOrEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ){

                            Image(
                                painter = painterResource(id = R.drawable.search_not_icon),
                                contentDescription = stringResource(id = R.string.momo_coffe),
                                modifier = Modifier.width(135.dp)
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = stringResource(id = R.string.not_found_products),
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                color = BlueDark,
                                fontSize = 20.sp
                            )
                        }

                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(200.dp),
                            content = {
                                items(productsItems?.size ?: 0) { index ->
                                    productsItems?.get(index)?.let { product ->
                                        CardProduct(navController, selectCategory = category, product)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
}