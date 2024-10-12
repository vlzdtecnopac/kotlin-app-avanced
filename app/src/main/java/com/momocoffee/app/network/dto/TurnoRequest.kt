package com.momocoffee.app.network.dto

import com.google.gson.annotations.SerializedName

data class TurnoRequest(
    @SerializedName("name_client")
    val nameClient: String
)