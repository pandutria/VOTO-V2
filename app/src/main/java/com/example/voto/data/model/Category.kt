package com.example.voto.data.model

data class Category(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return name
    }
}
