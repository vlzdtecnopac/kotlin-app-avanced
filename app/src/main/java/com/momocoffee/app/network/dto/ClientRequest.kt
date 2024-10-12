package com.momocoffee.app.network.dto

import com.google.gson.annotations.SerializedName

data class ClientRequest (
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val phone: String,
    val code: String,
    val country: String,
    val email: String
)
