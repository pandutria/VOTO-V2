package com.example.voto.data.model

data class History(
    val id: String,
    val status: String,
    val totalPrice: Int,
    val transactions: MutableList<HistoryOrder>
)