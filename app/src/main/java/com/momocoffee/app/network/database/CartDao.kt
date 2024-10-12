package com.momocoffee.app.network.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(product: Cart)

    @Query("SELECT * FROM cart")
    fun getAllCart(): Flow<List<Cart>>

    @Query("SELECT SUM(c.price_product_mod) FROM cart c")
    fun getAllSumTotal(): Flow<Float?>

    @Query("SELECT SUM(c.count_product) FROM cart c")
    fun getAllCountTotal(): Flow<Int?>

    @Delete
    suspend fun deleteProduct(product: Cart)

    @Query("UPDATE cart SET count_product = :count, price_product_mod = :price WHERE id = :id")
    suspend fun updateProductCountById(id: Int, count: Int, price: Int)

    @Query("DELETE FROM cart")
    suspend fun deleteAllCart()

}