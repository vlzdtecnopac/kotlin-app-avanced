package com.momocoffee.app.ui.components

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.momocoffee.app.ui.components.cart.Cart
import com.momocoffee.app.ui.theme.stacionFamily
import com.momocoffee.app.viewmodel.CartViewModel
import com.momocoffee.app.viewmodel.CategoryViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.momocoffee.app.ui.category.sections.SubCategory
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.theme.WhiteBone
import org.json.JSONArray
import java.util.Locale

@Composable
fun Category(
    navController: NavController,
    selectCategory: String? = "",
    showCart: Boolean = false,
    cartViewModel: CartViewModel,
    categoryViewModel: CategoryViewModel = viewModel()
) {

    var showDialog by remember { mutableStateOf(false) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination: String? = currentBackStackEntry?.destination?.route

    LaunchedEffect(Unit) {
        categoryViewModel.categorys();

    }

    if (categoryViewModel.subCategorySelected.value.length() > 0) {
        SubCategory(
            navController,
            cartViewModel,
            categoryViewModel.selectCategory.value,
            list = categoryViewModel.subCategorySelected.value,
            onCloseDialog = { showDialog = false })
    }


    if (!categoryViewModel.loadingState.value) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            categoryViewModel.categoriesResultState.value?.let { result ->
                result.onSuccess { categoriesResponse ->
                    LazyRow(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(categoriesResponse.size) { index ->
                            val jsonSubCategoryArray = JSONArray(categoriesResponse[index].subCategory)
                            Spacer(modifier = Modifier.width(6.dp))
                            BtnOutlineCategory(
                                text = categoriesResponse[index].labelCategory.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.ROOT
                                    ) else it.toString()
                                },
                                backgroundColor = if (categoriesResponse[index].nameCategory == selectCategory) {
                                    WhiteBone
                                } else {
                                    BlueDark
                                },
                                textColor = if (categoriesResponse[index].nameCategory == selectCategory) {
                                    BlueDark
                                } else {
                                    WhiteBone
                                },
                                onclick = {
                                    Log.d("Result.ShoppingModel", currentDestination.toString())
                                    categoryViewModel.selectCategory.value =
                                        categoriesResponse[index].nameCategory
                                    if (jsonSubCategoryArray.length() > 0) {
                                        categoryViewModel.subCategorySelected.value = jsonSubCategoryArray
                                        categoryViewModel.selectCategory.value =
                                            categoriesResponse[index].nameCategory

                                    } else {
                                        navController.navigate("products/${categoriesResponse[index].nameCategory}")
                                    }
                                })

                            Spacer(modifier = Modifier.width(6.dp))
                        }
                    }
                }
            }
            if (showCart) {
                Cart(navController, cartViewModel, btnStyleOne = false)
            }
        }
    }
}


@Composable
fun BtnOutlineCategory(
    text: String,
    onclick: () -> Unit,
    backgroundColor: Color,
    textColor: Color
) {

    val modifierCard = Modifier
        .width(110.dp)
        .height(60.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(color = backgroundColor)
        .border(
            width = 1.2.dp,
            color = Color.White,
            shape = RoundedCornerShape(14.dp)
        )
        .padding(vertical = 8.dp, horizontal = 14.dp)

    Column(
        modifier = modifierCard.clickable { onclick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text,
            color = textColor,
            fontSize = 14.sp,
            fontFamily = stacionFamily,
            fontWeight = FontWeight.W700,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}