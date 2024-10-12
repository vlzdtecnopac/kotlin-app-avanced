package com.momocoffee.app.network.response

import com.google.gson.annotations.SerializedName

data class KioskoResponse (
    @SerializedName("name_shopping")
    val nameShopping: String,
    val data: DataKiosko
)

data class DataKiosko (
    val id: Long,
    @SerializedName("kiosko_id")
    val kioskoID: String,
    @SerializedName("shopping_id")
    val shoppingID: String,
    val state: Boolean,
    val nombre: String,
    @SerializedName("create_at")
    val createAt: String,
    @SerializedName("update_at")
    val updateAt: String
)