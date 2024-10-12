package com.momocoffee.app.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momocoffee.app.BuildConfig
import com.momocoffee.app.network.repository.ApiService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CheckoutViewModel  : ViewModel() {
    var convertMoneyState = mutableStateOf(0f)

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL_CURRENCY)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ApiService::class.java)
    fun convertAmount(valor: Float) {
        viewModelScope.launch {
            val fromCurrency = "MXN"
            val toCurrency = "USD"

            try {
                val response = service.getExchangeRate(fromCurrency, toCurrency)
                val conversionRate = response.rates[toCurrency]
                if (conversionRate != null) {
                    convertMoneyState.value = (conversionRate * valor).toFloat()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Result.ConvertMoney", e.message.toString())
            }
        }
    }


}