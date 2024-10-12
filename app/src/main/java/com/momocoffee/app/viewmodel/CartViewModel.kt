package com.momocoffee.app.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momocoffee.app.network.data.CartProduct
import com.momocoffee.app.network.data.CartProductEdit
import com.momocoffee.app.network.data.CartState
import com.momocoffee.app.network.database.Cart
import com.momocoffee.app.network.database.CartDao
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CartViewModel(
    private val dao: CartDao
) : ViewModel() {
    var valorTotal = mutableStateOf(0f)

    val loadingCartState = mutableStateOf(false)

    val countCartState = mutableStateOf(0)

    var stateTotalSub = mutableStateOf(0f)
    var state by mutableStateOf(CartState())
        private set

    init {
        viewModelScope.launch {
            dao.getAllCart().collectLatest {
                state = state.copy(carts = it)
                loadingCartState.value = false
            }
        }
    }

    fun deleteProduct(product: Cart) {
        loadingCartState.value = true
        viewModelScope.launch {
            dao.deleteProduct(product)
            stateTotalSub.value =  stateTotalSub.value - product.priceProductMod.toInt()
            loadingCartState.value = false
        }
    }

    fun createProduct(product: CartProduct) {
        loadingCartState.value = true
        val cart = Cart(
            0,
            product.productId,
            product.titleProduct,
            product.imageProduct,
            product.priceProduct,
            product.priceProductMod,
            product.priceExtras,
            product.countProduct,
            product.modifiersOptions.toString(),
            product.modifiersList.toString()
        )

        viewModelScope.launch {
            dao.insertCart(cart)
            state = state.copy(carts = state.carts)
            loadingCartState.value = false
        }
    }

    fun editCart(product: CartProductEdit) {
        loadingCartState.value = true
        viewModelScope.launch {
            val cart = state.carts.find { it.id == product.id }
            if (cart != null) {
                dao.updateProductCountById(cart.id, product.countProduct, product.priceProduct)
            }
            loadingCartState.value = false
        }
    }

    fun priceSubTotal() {
        try {
            if (state.carts.isNullOrEmpty()) {
                loadingCartState.value = false
            } else {
                loadingCartState.value = true
                viewModelScope.launch {
                    dao.getAllSumTotal().collectLatest { sumTotal ->
                        stateTotalSub.value = sumTotal ?: 0f
                        loadingCartState.value = false
                    }
                }
            }
        }catch (e:Exception){
            Log.e("Result.CartViewModel", e.message.toString())
        }
    }
    fun countTotal() {
        try {
            if (state.carts.isNullOrEmpty()) {
                loadingCartState.value = false
            } else {
                loadingCartState.value = true
                viewModelScope.launch {
                    dao.getAllCountTotal().collectLatest { countTotal ->
                        countCartState.value = countTotal ?: 0
                        loadingCartState.value = false
                    }
                }
            }
        }catch (e:Exception){
            Log.e("Result.CartViewModel", e.message.toString())
        }
    }

    fun clearAllCart() {
        // Obtén una instancia de un hilo de ejecución en segundo plano
        viewModelScope.launch {
            dao.deleteAllCart()
        }
    }
}
