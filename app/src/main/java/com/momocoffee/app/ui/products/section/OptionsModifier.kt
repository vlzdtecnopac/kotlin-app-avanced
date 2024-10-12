package com.momocoffee.app.ui.products.section

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.momocoffee.app.R
import com.momocoffee.app.network.data.SelectedOptions
import com.momocoffee.app.network.response.ProductOptionsResponse
import com.momocoffee.app.network.response.ProductOptionsSizeResponse

import com.momocoffee.app.network.response.ProductsItem
import com.momocoffee.app.ui.products.components.BoxOptions
import com.momocoffee.app.ui.products.components.ItemBox
import com.momocoffee.app.ui.products.components.ItemList
import com.momocoffee.app.ui.products.components.ListOptions
import com.momocoffee.app.viewmodel.ItemModifier
import com.momocoffee.app.viewmodel.ProductsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun OptionsModifier(productsItem: ProductsItem,
                    onSelect: (ItemModifier) -> Unit,
                    onClick: (String) -> Unit,
                    productsViewModel: ProductsViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var optionsItems: ProductOptionsResponse? by remember { mutableStateOf(null) }
    var optionsSizeItems: ProductOptionsSizeResponse? by remember { mutableStateOf(null) }
    var selectedOptions by remember { mutableStateOf(SelectedOptions()) }

    LaunchedEffect(productsItem.productID) {
        productsViewModel.loadingState.value = true
        delay(1000)
        productsViewModel.productOptions(product_id = productsItem.productID)
        productsViewModel.productOptionsSize(nameProduct = productsItem.nameProduct)
        onSelect(ItemModifier(productsItem.productID,  productsItem.nameProduct,  productsItem.price.toString()))
    }

    LaunchedEffect(productsViewModel.productsOptionsResultState.value){
        productsViewModel.productsOptionsResultState.value?.let { result ->
            when{
                result.isSuccess ->{
                    val optionsResponse = result.getOrThrow()
                    optionsItems = optionsResponse
                    productsViewModel.loadingState.value = false
                }
            }
        }
    }

    LaunchedEffect(selectedOptions) {
        val totalPrice = selectedOptions.calculatePrice()
        if(totalPrice > 0){
            productsViewModel.calculateExtraResult.value = (totalPrice - productsItem.price).toInt()
        }

        if(optionsSizeItems?.sizes?.size != null){
            productsViewModel.calculatePriceResult.value =  totalPrice
        }else{
            productsViewModel.calculatePriceResult.value = (totalPrice  +  productsItem.price).toInt()
        }
        
    }

    LaunchedEffect(productsViewModel.productsOptionsSizeResultState.value){
        productsViewModel.productsOptionsSizeResultState.value?.let {result ->
            when{
                result.isSuccess ->{
                    val optionsResponse = result.getOrThrow()
                    optionsSizeItems = optionsResponse
                    productsViewModel.loadingState.value = false
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(horizontal = 8.dp)
            .verticalScroll(scrollState)
    ) {
        if(optionsSizeItems?.sizes?.isNotEmpty() == true){
            optionsSizeItems?.let { options ->
                var newsItems = mutableListOf<ItemBox>()
                if(options.sizes.isNotEmpty()){
                    options.sizes.mapIndexed { index, product ->
                        if(newsItems.none {it.name == product}){
                            newsItems.add(
                                ItemBox(
                                    options.ids[index],
                                    product,
                                    options.price[index].toInt()
                                )
                            )
                        }
                    }
                }

                if (newsItems.size > 0) {
                    BoxOptions(
                        iconResource = R.drawable.tamano_icon,
                        textResource = R.string.size,
                        onScrolling = {
                            coroutineScope.launch {
                                scrollState.animateScrollTo(80)
                            }
                        },
                        onSelectPrice = { id, price, name ->
                            selectedOptions = selectedOptions.copy(size = price)
                            val updatedMap = productsViewModel.selectModifiersOptions.toMutableMap()
                            updatedMap["size"] = ItemModifier(id, name, price.toString())
                            productsViewModel.selectModifiersOptions = updatedMap
                            onSelect(ItemModifier(id, name, price.toString()))
                            onClick(id)
                        },
                        items = newsItems.map { product ->
                            ItemBox(product.id, product.name, product.price)
                        },

                    )
                }
            }
        }


        if(optionsItems?.temperatura?.isNotEmpty() == true) {
            optionsItems?.let { options ->
                var newsItems = mutableListOf<ItemBox>()
                if (options.temperatura.isNotEmpty()) {
                    options.temperatura.map { product ->
                        val price = product.price
                        val parsedPrice = price?.takeIf { it.isNotEmpty() }?.toInt() ?: 0
                        if (newsItems.none { it.name == product.name }) {
                            newsItems.add(
                                ItemBox(
                                    product.localCode,
                                    product.name,
                                    parsedPrice
                                )
                            )
                        }
                    }
                }

                if (newsItems.size > 0) {
                    BoxOptions(
                        iconResource = R.drawable.temp_icon,
                        textResource = R.string.txt_temp,
                        onScrolling = {
                            coroutineScope.launch {
                                scrollState.animateScrollTo(80)
                            }
                        },
                        onSelectPrice = { id, price, name ->
                            selectedOptions = selectedOptions.copy(temp = price)
                            val updatedMap = productsViewModel.selectModifiersOptions.toMutableMap()
                            updatedMap["temp"] = ItemModifier(id, name, price.toString())
                            productsViewModel.selectModifiersOptions = updatedMap
                        },
                        items = newsItems.map { product ->
                            ItemBox(product.id, product.name, product.price)
                        },

                        )

                }
            }
        }


        if(optionsItems?.leche?.isNotEmpty() == true) {

            optionsItems?.let { options ->

                var newsItems = mutableListOf<ItemBox>()

                if (options.leche.isNotEmpty()) {
                    options.leche.map { product ->

                        if (newsItems.none { it.name == product.name }) {

                            newsItems.add(
                                ItemBox(
                                    product.localCode,
                                    product.name,
                                    product.price.toInt()
                                )
                            )
                        }
                    }
                }
                if (newsItems.size > 0) {
                    BoxOptions(
                        iconResource = R.drawable.milk_icon,
                        textResource = R.string.txt_milk,
                        onScrolling = {
                            coroutineScope.launch {
                                scrollState.animateScrollTo(50)
                            }
                        },
                        onSelectPrice = { id, price, name ->
                            selectedOptions = selectedOptions.copy(milk = price)
                            val updatedMap = productsViewModel.selectModifiersOptions.toMutableMap()
                            updatedMap["milk"] = ItemModifier(id, name, price.toString())
                            productsViewModel.selectModifiersOptions = updatedMap
                        },
                        items = newsItems.map { product ->
                            ItemBox(product.id, product.name, product.price)
                        },
                        )
                }
            }
        }

        if(optionsItems?.azúcar?.isNotEmpty() == true) {
            optionsItems?.let { options ->
                var newsItems = mutableListOf<ItemBox>()
                if (options.azúcar.isNotEmpty()) {
                    options.azúcar.map { product ->
                        if (newsItems.none { it.name == product.name }) {
                            newsItems.add(
                                ItemBox(
                                    product.localCode,
                                    product.name,
                                    product.price.toInt()
                                )
                            )
                        }
                    }
                }

                if (newsItems.size > 0) {
                    BoxOptions(
                        iconResource = R.drawable.sugar_icon,
                        textResource = R.string.txt_sugar,
                        onScrolling = {
                            coroutineScope.launch {
                                scrollState.animateScrollTo(150)
                            }
                        },
                        onSelectPrice = { id, price, name ->
                            selectedOptions = selectedOptions.copy(sugar = price)
                            val updatedMap = productsViewModel.selectModifiersOptions.toMutableMap()
                            updatedMap["sugar"] = ItemModifier(id, name, price.toString())
                            productsViewModel.selectModifiersOptions = updatedMap
                        },
                        items = newsItems.map { product ->
                            ItemBox(product.id, product.name, product.price)
                        },
                      )
                }
            }
        }

        if(optionsItems?.endulzante?.isNotEmpty() == true) {
            optionsItems?.let { options ->
                var newsItems = mutableListOf<ItemBox>()
                if (options.endulzante.isNotEmpty()) {
                    options.endulzante.map { product ->
                        if (newsItems.none { it.name == product.name }) {
                            newsItems.add(
                                ItemBox(
                                    product.localCode,
                                    product.name,
                                    product.price.toInt()
                                )
                            )
                        }
                    }
                }

                if (newsItems.size > 0) {
                    BoxOptions(
                        iconResource = R.drawable.sugar_icon,
                        textResource = R.string.txt_endulzante,
                        onScrolling = {
                            coroutineScope.launch {
                                scrollState.animateScrollTo(150)
                            }
                        },
                        onSelectPrice = { id, price, name ->
                            selectedOptions = selectedOptions.copy(endulzante = price)
                            val updatedMap = productsViewModel.selectModifiersOptions.toMutableMap()
                            updatedMap["endulzante"] = ItemModifier(id, name, price.toString())
                            productsViewModel.selectModifiersOptions = updatedMap
                        },
                        items = newsItems.map { product ->
                            ItemBox(product.id, product.name, product.price)
                        },
                       )
                }
            }
        }

        if(optionsItems?.extra?.isNotEmpty() == true) {
            optionsItems?.let { options ->
                var newsItems = mutableListOf<ItemList>()
                if (options.extra.isNotEmpty()) {
                    options.extra.map { product ->
                        if (newsItems.none { it.name == product.name }) {
                            newsItems.add(
                                ItemList(
                                    product.localCode,
                                    product.name,
                                    product.price.toInt()
                                )
                            )
                        }
                    }
                }
                if (newsItems.size > 0) {
                    ListOptions(
                        iconResource = R.drawable.extra_icon,
                        onScrolling = {
                            coroutineScope.launch {
                                scrollState.animateScrollTo(350)
                            }
                        },
                        onSelectPrice = {id, price, name ->
                            selectedOptions = selectedOptions.copy(extra = price)
                            val updatedMap = productsViewModel.selectModifiersList.toMutableMap()
                            updatedMap["extra"] =  ItemModifier(id, name, price.toString())
                            productsViewModel.selectModifiersList = updatedMap
                        },
                        items = newsItems.map { product ->
                            ItemList(product.id, product.name, product.price)
                        },
                        defaultSelect = ""
                        )
                }
            }
        }


        if(optionsItems?.tapa?.isNotEmpty() == true) {
            optionsItems?.let { options ->
                var newsItems = mutableListOf<ItemBox>()
                if (options.tapa.isNotEmpty()) {
                    options.tapa.map { product ->
                        if (newsItems.none { it.name == product.name }) {
                            newsItems.add(
                                ItemBox(
                                    product.localCode,
                                    product.name,
                                    product.price.toInt()
                                )
                            )
                        }
                    }
                }

                if (newsItems.size > 0) {
                    BoxOptions(
                        iconResource =  R.drawable.tapa_icon,
                        textResource = R.string.lib_text,
                        onScrolling = {
                            coroutineScope.launch {
                                scrollState.animateScrollTo(150)
                            }
                        },
                        onSelectPrice = { id, price, name ->
                            selectedOptions = selectedOptions.copy(endulzante = price)
                            val updatedMap = productsViewModel.selectModifiersOptions.toMutableMap()
                            updatedMap["libTapa"] =  ItemModifier(id, name, price.toString())
                            productsViewModel.selectModifiersOptions = updatedMap
                        },
                        items = newsItems.map { product ->
                            ItemBox(product.id, product.name, product.price)
                        },
                        )
                }
            }
        }



        if(optionsItems?.salsas?.isNotEmpty() == true) {
            optionsItems?.let { options ->

                var newsItems = mutableListOf<ItemBox>()

                if (options.salsas.isNotEmpty()) {
                    options.salsas.map { product ->

                        if (newsItems.none { it.name == product.name }) {
                            newsItems.add(
                                ItemBox(
                                    product.localCode,
                                    product.name,
                                    product.price.toInt()
                                )
                            )
                        }
                    }
                }
                if (newsItems.size > 0) {
                    ListOptions(
                        iconResource = R.drawable.salsa_icon,
                        onScrolling = {
                            coroutineScope.launch {
                                scrollState.animateScrollTo(350)
                            }
                        },
                        onSelectPrice = { id, price, name ->
                            selectedOptions = selectedOptions.copy(sauce = price)
                            val updatedMap = productsViewModel.selectModifiersList.toMutableMap()
                            updatedMap["sauce"] =  ItemModifier(id, name, price.toString())
                            productsViewModel.selectModifiersList = updatedMap
                        },
                        items = newsItems.map { product ->
                            ItemList(product.id, product.name, product.price)
                        },
                        defaultSelect = "Sin salsa"
                    )

                }
            }
        }


    }
}