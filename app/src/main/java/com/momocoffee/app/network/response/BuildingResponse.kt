package com.momocoffee.app.network.response

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*

data class BuildingResponse (
    val data: List<Datum>,
    val toteat: Toteat
)

data class Datum (
    @SerializedName("bilding_id")
    val bildingID: String,

    @SerializedName("shopping_id")
    val shoppingID: String,

    @SerializedName("kiosko_id")
    val kioskoID: String,

    @SerializedName("truno_id")
    val turnoID: String,

    @SerializedName("type_payment")
    val typePayment: String,

    val state: String
)


data class Toteat (
    @SerializedName("orderId")
    val orderID: Long,

    val status: String,

    @SerializedName("restaurantId")
    val restaurantID: Long,

    val document: Document,
    val orderReference: String,
    val operationDate: String,

    @SerializedName("tableId")
    val tableID: List<Long>,

    val localNumber: Long,
    val modificationDate: String,
    val creationDate: String,
    val type: String,
    val vendorName: String,
    val channel: String,
    val orderStatus: Long
)


data class Document (
    val customer: Customer,
    val line: List<Line>,
    val payments: List<Payment>,
    val dispatcher: Dispatcher
)


data class Customer (
    val phoneNumber1: String,
    val phoneNumber2: String,
    val name: String,
    val delivery: Delivery,
    val phoneNumber: String,
    val email: String
)


data class Delivery (
    val city: String,
    val deliveryZone: String,
    val country: String,
    val officeOrApt: String,
    val location: String,
    val address: String,
    val postalCode: String
)


class Dispatcher()


data class Line (
    val category: String,
    val productCode: String,
    val unitPriceBeforeTax: Double,
    val tax: List<Tax>,
    val productName: String,
    val unitPriceAfterTax: Long,
    val amountAfterTax: Long,
    val productCodeToteat: Long,
    val lineNumber: Long,
    val amountBeforeTax: Double,
    val quantity: Long
)


data class Tax (
    val name: String,
    val value: Double
)


data class Payment (
    val commission: List<Tax>,
    val amountPaid: Long,
    val paymentType: Long,
    val amount: Long,
    val ref: String,
    val id: Long
)
