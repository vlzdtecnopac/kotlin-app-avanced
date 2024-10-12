package com.momocoffee.app.network.response

import com.google.gson.annotations.SerializedName

data class EmployeeResponse (
    val items: List<ItemEmployee>,
    val page: Long,
    val limit: Long,
    val total: Long
)

data class ItemEmployee (
    val id: Long,
    @SerializedName("employee_id")
    val employeeID: String,
    @SerializedName("shopping_id")
    val shoppingID: String,
    @SerializedName("name_shopping")
    val nameShopping: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val phone: String,
    val email: String,
    val password: String,
    val address: String,
    val state: Boolean,
    val role: String,
    @SerializedName("create_at")
    val createAt: String,
    @SerializedName("update_at")
    val updateAt: String
)
