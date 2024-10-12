package com.momocoffee.app.network.dto

import com.google.gson.annotations.SerializedName

data class  VerifyKioskoRequest (
    @SerializedName("kiosko_id")
    val kioskoId: String
)