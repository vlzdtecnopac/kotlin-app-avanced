package com.momocoffee.app.network.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class Cart(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "product_id") val productId: String,
    @ColumnInfo(name = "title_product") val titleProduct: String,
    @ColumnInfo(name = "image_product") val imageProduct: String,
    @ColumnInfo(name = "price_product") val priceProduct: String,
    @ColumnInfo(name = "price_product_mod") val priceProductMod: String,
    @ColumnInfo(name = "price_extras") val priceExtras: String,
    @ColumnInfo(name = "count_product") val countProduct: Int,
    @ColumnInfo(name = "modifiers_options") val modifiersOptions: String,
    @ColumnInfo(name = "modifiers_list") val modifiersList: String,
)
