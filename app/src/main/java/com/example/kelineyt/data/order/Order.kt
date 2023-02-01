package com.example.kelineyt.data.order

import com.example.kelineyt.data.Address
import com.example.kelineyt.data.CartProduct

data class Order(
    val orderStatus: String,
    val totalPrice: Float,
    val products: List<CartProduct>,
    val address: Address
)