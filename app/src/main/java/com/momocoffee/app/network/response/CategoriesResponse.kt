package com.momocoffee.app.network.response

import com.google.gson.annotations.SerializedName

data class CategoriesResponse (
    val id: Long,
    @SerializedName("category_id")
    val categoryID: String,
    @SerializedName("name_category")
    val nameCategory: String,
    @SerializedName("label_name")
    val labelCategory: String,
    val image: String,
    @SerializedName("class_icon")
    val classIcon: String,
    @SerializedName("category_response_class")
    val categoryResponseClass: String,
    @SerializedName("sub_category")
    val subCategory: String,
    @SerializedName("create_at")
    val createAt: String,
    @SerializedName("update_at")
    val updateAt: Any? = null
)

