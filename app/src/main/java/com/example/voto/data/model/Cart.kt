package com.example.voto.data.model

data class Cart(
    val camera: Camera,
    var qty: Int,
    var isSelected: Boolean? = true
)
