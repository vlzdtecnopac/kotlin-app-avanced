package com.momocoffee.app.network.dto

import com.google.gson.annotations.SerializedName

data class UpdateBilingRequest (
    val name: String? = null,
    @SerializedName("shopping_id")
    val shoppingID: String? = null,
    @SerializedName("kiosko_id")
    val kioskoID: String? = null,
    @SerializedName("turno_id")
    val turnoID: String? = null,
    @SerializedName("type_payment")
    val typePayment: String? = null,
    @SerializedName("mount_receive")
    val mountReceive: String? = null,
    val total: String? = null,
    val subtotal: String? = null,
    val propina: String? = null,
    val iva: String? = null,
    @SerializedName("email_payment")
    val emailPayment: String? = null,
    val cupon: String? = null,
    val state: String? = null,
    val comment: String? = "",
    @SerializedName("toteat_check")
    val toteatCheck: Boolean? = null,
    val status: String? = null,
    val type: String? = null,
    val channel: String? = null,
    val vendorName: String? = null
)