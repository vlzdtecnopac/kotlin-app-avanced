package com.momocoffee.app.network.response

import com.google.gson.annotations.SerializedName

data class ConfigShoppingResponse (
    @SerializedName("type_text")
    val typeText: String,
    @SerializedName("type_column")
    val typeColumn: String,
    @SerializedName("create_at")
    val createAt: String,
    @SerializedName("update_at")
    val updateAt: String
)