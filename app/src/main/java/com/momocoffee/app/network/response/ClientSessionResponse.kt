package com.momocoffee.app.network.response

import com.google.gson.annotations.SerializedName

data class ClientSessionResponse (
    @SerializedName("client_id")
    val clientID: String,
    val avatar: Any? = null,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String
)

