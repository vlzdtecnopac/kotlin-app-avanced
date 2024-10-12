package com.momocoffee.app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momocoffee.app.network.data.GeneralResponse
import com.momocoffee.app.network.dto.PedidoRequest
import com.momocoffee.app.network.repository.ApiService
import com.momocoffee.app.network.repository.RetrofitHelper
import kotlinx.coroutines.launch

class PedidoViewModel: ViewModel(){
    private val apiService: ApiService = RetrofitHelper.apiService()
    val loadingState = mutableStateOf(false)
    val pedidoResultState = mutableStateOf<Result<GeneralResponse>?>(null)

    fun create(pedidoData: PedidoRequest){
        loadingState.value = true
        viewModelScope.launch {
            try{
                val response = apiService.createPedido(pedidoData)
                if(response.isSuccessful){
                    val pedidoResponse: GeneralResponse? = response.body()
                    if(pedidoResponse != null){
                        pedidoResultState.value = Result.success(pedidoResponse)
                    }else{
                       pedidoResultState.value = Result.failure(Exception("Empty response body"))
                    }
                }else{
                    pedidoResultState.value = Result.failure(Exception("Pedido failed"))
                }
            }catch (e: Exception){
                pedidoResultState.value = Result.failure(e)
                Log.e("Result.PedidoViewModel",  e.message.toString())
            }finally {
                loadingState.value = false
                Log.d("Result.PedidoViewModel", "Finally")
            }
        }
    }
}