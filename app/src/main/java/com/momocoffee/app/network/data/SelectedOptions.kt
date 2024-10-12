package com.momocoffee.app.network.data

data class SelectedOptions(
    var temp: Int = 0,
    var size: Int = 0,
    var milk: Int = 0,
    var sugar: Int = 0,
    var endulzante: Int = 0,
    var extra: Int = 0,
    var tapLib: Int = 0,
    var temperatureDrink: Int = 0,
    var sauce: Int = 0
){
    fun calculatePrice(): Int {
        return listOf(temp, size, milk, sugar, extra, tapLib, temperatureDrink, sauce, endulzante).sum()
    }
}