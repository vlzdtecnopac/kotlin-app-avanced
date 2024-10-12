package com.momocoffee.app.network.dto

data class ClientReceptorEmailRequest(
    val from: String,
    val to: String,
    val subject: String
)

data class ClientReceptorSMSRequest(
  val topic: String,
  val phone: String,
  val subject: String,
  val message: String
)
