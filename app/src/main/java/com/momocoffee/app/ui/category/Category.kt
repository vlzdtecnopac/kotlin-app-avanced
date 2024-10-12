package com.momocoffee.app.ui.category

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.momocoffee.app.ui.category.components.BtnOutlineCategory
import com.momocoffee.app.ui.components.headers.Header
import com.momocoffee.app.ui.theme.BlueDark
import com.momocoffee.app.ui.category.sections.SubCategory
import com.momocoffee.app.viewmodel.CategoryViewModel
import com.momocoffee.app.ui.theme.BlueDarkTransparent
import com.momocoffee.app.viewmodel.CartViewModel
import com.spr.jetpack_loading.components.indicators.BallClipRotatePulseIndicator
import kotlinx.coroutines.delay
import org.json.JSONArray
import java.util.Locale

data class ListItem(val iconResId: Int, val name: String)
data class SubCategory(val label: String, val image: String)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Category(
    navController: NavController,
    viewModelCart: CartViewModel,
    categoryViewModel: CategoryViewModel = viewModel(),
    addShowModalConfirmCart: Boolean? = false
) {
    val loading = categoryViewModel.loadingState.value
    var openAlertDialog by remember { mutableStateOf(addShowModalConfirmCart) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        categoryViewModel.categorys();
    }

    LaunchedEffect(categoryViewModel.subCategorySelected.value) {
        if (categoryViewModel.subCategorySelected.value.length() > 0) {
            showDialog = true
        }
    }

    if (categoryViewModel.subCategorySelected.value.length() > 0 && showDialog) {
        SubCategory(
            navController,
            viewModelCart,
            categoryViewModel.selectCategory.value,
            list = categoryViewModel.subCategorySelected.value,
            onCloseDialog = { showDialog = false })
    }

    if (openAlertDialog == true) {
        LaunchedEffect(Unit) {
            delay(2000)
            openAlertDialog = false
        }

    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BlueDark),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Header(
                    navController,
                    cartViewModel = viewModelCart,
                    buttonExit = true,
                    visibleCart = true
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BlueDark),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {

                categoryViewModel.categoriesResultState.value?.let { result ->
                    result.onSuccess { categoriesResponse ->

                        FlowRow(
                            modifier = Modifier.widthIn(0.dp, 900.dp),
                            maxItemsInEachRow = 4,
                            horizontalArrangement = Arrangement.Center) {

                            categoriesResponse.forEachIndexed { index, it ->
                                val jsonSubcategoryArray = JSONArray(it.subCategory)
                                val painter =  if(it.image.isNullOrEmpty()) "" else it.image ;
                                if(index >= 4 && categoriesResponse.size < 8){
                                    Box(
                                        modifier = Modifier
                                            .width(220.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                            .padding(10.dp)
                                    ) {
                                        BtnOutlineCategory(
                                            icon = painter,
                                            text = it.labelCategory.replaceFirstChar {
                                                if (it.isLowerCase()) it.titlecase(
                                                    Locale.ROOT
                                                ) else it.toString()
                                            },
                                            onclick = {
                                                if (jsonSubcategoryArray.length() > 0) {
                                                    categoryViewModel.subCategorySelected.value = jsonSubcategoryArray
                                                    categoryViewModel.selectCategory.value = it.nameCategory
                                                    showDialog = true
                                                } else {
                                                    navController.navigate("products/${it.nameCategory}")
                                                }

                                            }
                                        )
                                    }
                                }else {
                                    Box(
                                        modifier = Modifier
                                            .weight(1f, true)
                                            .clip(RoundedCornerShape(14.dp))
                                            .padding(10.dp)
                                    ) {
                                        BtnOutlineCategory(
                                            icon = painter,
                                            text = it.labelCategory.replaceFirstChar {
                                                if (it.isLowerCase()) it.titlecase(
                                                    Locale.ROOT
                                                ) else it.toString()
                                            },
                                            onclick = {
                                                if (jsonSubcategoryArray.length() > 0) {
                                                    categoryViewModel.subCategorySelected.value = jsonSubcategoryArray
                                                    categoryViewModel.selectCategory.value = it.nameCategory
                                                    showDialog = true
                                                } else {
                                                    navController.navigate("products/${it.nameCategory}")
                                                }

                                            }
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        if (loading) {
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