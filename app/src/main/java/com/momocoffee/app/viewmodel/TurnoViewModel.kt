package com.momocoffee.app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momocoffee.app.network.data.GeneralResponse
import com.momocoffee.app.network.dto.TurnoRequest
import com.momocoffee.app.network.repository.ApiService
import com.momocoffee.app.network.repository.RetrofitHelper
import com.momocoffee.app.network.response.TurnoResponse
import kotlinx.coroutines.launch

class TurnoViewModel : ViewModel() {
    private val apiService: ApiService = RetrofitHelper.apiService()
    val loadingState = mutableStateOf(false)
    val turnoPedidogState = mutableStateOf<Result<GeneralResponse>?>(null)
    val turnoBildingState = mutableStateOf<Result<TurnoResponse>?>(null)


    fun updateTurnoPedido(turnoId: String, nameClient: String) {
        loadingState.value = true
        viewModelScope.launch {
            try {
                val response = apiService.updateTurnoPedido(turnoId, TurnoRequest(nameClient))
                if (response.isSuccessful) {
                    val responseTurnoPedido: GeneralResponse? = response.body()
                    if (responseTurnoPedido != null) {
                        turnoPedidogState.value = Result.success(responseTurnoPedido)
                    } else {
                        turnoPedidogState.value = Result.failure(Exception("Empty response body"))
                    }
                } else {
                    turnoPedidogState.value = Result.failure(Exception("Turno Pedido failed"))
                }
            } catch (e: Exception) {
                Log.e("Result.TurnoViewModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.TurnoViewModel", "Finally")
            }

        }

    }

    fun getTurnByBilding(bildingId: String, state: String) {
        loadingState.value = true
        viewModelScope.launch {
            try {
                val response = apiService.getVerifyBildings(bildingId, state)
                if (response.isSuccessful) {
                    val responseTurnoPedido: TurnoResponse? = response.body()
                    if (responseTurnoPedido != null) {
                        turnoBildingState.value = Result.success(responseTurnoPedido)
                    } else {
                        turnoBildingState.value = Result.failure(Exception("Empty response body"))
                    }
                } else {
                    turnoPedidogState.value = Result.failure(Exception("Turno Pedido failed"))
                }
            } catch (e: Exception) {
                Log.e("Result.TurnoViewModel", e.message.toString())
            } finally {
                loadingState.value = false
                Log.d("Result.TurnoViewModel", "Finally")
            }
        }
    }
}