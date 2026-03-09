package com.example.voto.data.model

data class Camera (
    val id: Int,
    val name: String,
    val resolution: String,
    val price: Int,
    val photo: String,
    val sellerShop: String? = null,
    val sensor: String? = null,
    val autoFocusSystem: String? = null,
    val isoRange: String? = null,
    val shuterSpeedRange: String? = null,
    val dimensions: String? = null,
    val weight: Int? = null,
    val wiFi: Boolean? = null,
    val touchScreen: Boolean? = null,
    val flash: Boolean? = null,
    val bluetooth: Boolean? = null,
)