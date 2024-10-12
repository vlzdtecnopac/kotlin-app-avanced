package com.momocoffee.app.network.response

import com.google.gson.annotations.SerializedName

data class CuponesResponse (
    val items: List<CuponesItem>,
    val page: Long,
    val limit: Long,
    val total: Long
)

data class CuponesItem (
    val id: Long,
    @SerializedName("cupon_id")
    val cuponID: String,
    val shopping: String,
    @SerializedName("name_cupon")
    val nameCupon: String,
    @SerializedName("type_discount")
    val typeDiscount: String,
    val discount: String,
    @SerializedName("cupon_consume")
    val cuponConsume: Int,
    @SerializedName("type_vigente")
    val typeVigente: String,
    val vigencia: String,
    @SerializedName("count_cupon")
    val countCupon: Int,
    @SerializedName("create_at")
    val createAt: String,
    @SerializedName("update_at")
    val updateAt: Any? = null
)