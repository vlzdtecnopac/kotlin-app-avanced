package com.momocoffee.app.network.dto

import com.google.gson.annotations.SerializedName

data class KioskoRequest (
    val state: Boolean,
    @SerializedName("shopping_id")
    val shoppingID: String
)
