package com.momocoffee.app.network.data

import com.google.gson.annotations.SerializedName

data class ExchangeRateResponse(
    @SerializedName("rates")
    val rates: Map<String, Double>
)