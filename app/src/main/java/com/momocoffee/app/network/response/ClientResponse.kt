package com.momocoffee.app.network.response

import com.google.gson.annotations.SerializedName

data class ClientResponse (
    @SerializedName("client_id")
    val clientID: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val phone: String,
    val code: String,
    val email: String
)

data class ClientGeneralResponse (
    val items: List<ClientItem>,
    val page: Long,
    val limit: Long,
    val total: Long
)

data class ClientItem (
    val id: Long,
    @SerializedName("client_id")
    val clientID: String,
    @SerializedName("shopping_id")
    val shoppingID: Any? = null,
    val avatar: Any? = null,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val phone: String,
    val email: String,
    val address: Any? = null,
    val code: String,
    val country: String,
    val state: Boolean,
    @SerializedName("create_at")
    val createAt: String,
    @SerializedName("update_at")
    val updateAt: Any? = null
)
