package com.example.voto.data.model

data class StoreCart(
    val storeName: String,
    val cart: MutableList<Cart>,
    val isSelected: Boolean? = true
)
