package com.momocoffee.app.network.response

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class ProductOptionsResponse (

    @SerializedName("azúcar")
    val azúcar: List<ItemProductModifier>,

    @SerializedName("endulzante")
    val endulzante: List<ItemProductModifier>,

    @SerializedName("leche")
    val leche: List<ItemProductModifier>,

    @SerializedName("tamaño")
    val tamaño: List<ItemProductModifier>,

    @SerializedName("extra")
    val extra: List<ItemProductModifier>,

    @SerializedName("tapa")
    val tapa: List<ItemProductModifier>,

    @SerializedName("temperatura")
    val temperatura: List<ItemProductModifier>,

    @SerializedName("salsas")
    val salsas: List<ItemProductModifier>
)


data class ItemProductModifier (
    val id: Long,

    @SerializedName("product_id")
    val productID: String,

    @SerializedName("categoryId")
    val categoryID: String,

    val minQuantity: String,
    val multi: Boolean,
    val sorting: String,
    val name: String,
    val maxQuantity: String,

    @SerializedName("create_at")
    val createAt: String,

    @SerializedName("update_at")
    val updateAt: String,

    @SerializedName("sub_modifier")
    val subModifier: JsonElement? = null,

    @SerializedName("modifiers_id")
    val modifiersID: String,

    val price: String,
    val localCode: String,
    val modificationDate: String,
    val alcohol: Boolean,
    val category: String,
    val modifiers: String,
    val isModifier: Boolean,
    val tamano: JsonElement? = null,
    val type: JsonElement? = null,

    @SerializedName("sub_category")
    val subCategory: JsonElement? = null
)


data class ProductOptionsSizeResponse (
    val name: String,
    val sizes: List<String>,
    val ids: List<String>,
    val price: List<String>
)

