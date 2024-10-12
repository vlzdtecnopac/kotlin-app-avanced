package com.momocoffee.app.network.response

import com.google.gson.annotations.SerializedName

data class ShoppingResponse (
    val items: List<ItemShopping>,
    val page: Long,
    val limit: Long,
    val total: Long
)

data class ItemShopping (
    val id: Long,
    @SerializedName("shopping_id")
    val shoppingID: String,
    @SerializedName("name_shopping")
    val nameShopping: String,
    @SerializedName("no_shopping")
    val noShopping: String,
    val address: String,
    val email: String,
    val idenfication: String,
    val phone: String,
    val closing: String,
    val open: String,
    val effecty: Boolean,
    val card: Boolean,
    val xir: String,
    val xil: String,
    val xiu: String,
    val xapitoken: String,
    @SerializedName("create_at")
    val createAt: String,
    @SerializedName("update_at")
    val updateAt: String
)
