package com.momocoffee.app.network.dto

data class ClientEmailInvoiceRequest(
    val from: String,
    val to: String,
    val subject: String,
    val bilding_id: String
)



