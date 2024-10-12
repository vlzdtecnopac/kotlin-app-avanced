package com.momocoffee.app.network.response

import com.google.gson.annotations.SerializedName

data class TurnoResponse (
    val items: List<Item>,
    val page: Long,
    val limit: Long,
    val total: Long
)

data class Item (
    val id: Long,
    @SerializedName("bilding_id")
    val bildingID: String,
    @SerializedName("shopping_id")
    val shoppingID: String,
    @SerializedName("kiosko_id")
    val kioskoID: String,
    @SerializedName("turno_id")
    val turnoID: String,
    val name: String,
    val product: String,
    @SerializedName("type_payment")
    val typePayment: String,
    @SerializedName("mount_receive")
    val mountReceive: String,
    @SerializedName("mount_discount")
    val mountDiscount: String,
    val propina: String,
    val cupon: String,
    val iva: String,
    val subtotal: String,
    val total: String,
    val state: String,
    val kiosko: Kiosko,
    val shopping: Shopping,
    @SerializedName("create_at")
    val createAt: String,
    @SerializedName("update_at")
    val updateAt: String
)

data class Kiosko (
    val data: KioskoData
)

data class KioskoData (
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

data class Shopping (
    val data: ShoppingData
)

data class ShoppingData (
    val id: Long,
    @SerializedName("shpping_id")
    val shoppingID: String,
    @SerializedName("name_shopping")
    val nameShopping: String,
    @SerializedName("no_shopping")
    val noShopping: String,
    val address: String,
    val email: String,
    val idenfication: String,
    val phone: String,
    val closing: Any? = null,
    val open: String,
    val state: Boolean,
    @SerializedName("create_at")
    val createAt: String,
    @SerializedName("update_at")
    val updateAt: String
)
