package com.momocoffee.app.network.database
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Cart::class], version = 1)
abstract class CartDataBase: RoomDatabase() {
    abstract val dao: CartDao
}

