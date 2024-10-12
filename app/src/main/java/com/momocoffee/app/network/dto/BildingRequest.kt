package com.momocoffee.app.network.dto


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

data class BuildingRequest (
    val name: String,

    @SerializedName("email_payment")
    val emailPayment: String,

    @SerializedName("shopping_id")
    val shoppingID: String,

    @SerializedName("kiosko_id")
    val kioskoID: String,

    @SerializedName("type_payment")
    val typePayment: String,

    val propina: String,

    @SerializedName("mount_receive")
    val mountReceive: String,

    @SerializedName("mount_discount")
    val mountDiscount: String,

    val cupon: String,
    val iva: String,
    val subtotal: String,
    val total: String,
    val state: String,
    val product: String
)
