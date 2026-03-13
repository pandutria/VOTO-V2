package com.example.voto.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import java.text.NumberFormat
import java.util.Locale

object Helper {
    val url = "http://100.100.181.124:5000/api/"
    val imageUrl = url.replace("api/", "images/")

    fun toast(context: Context, string: String) {
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }

    fun formatPrice(price: Int): String {
        val format = NumberFormat.getInstance(Locale("id", "ID"))
        return format.format(price)
    }

    suspend fun loadImage(image: String): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val conn = URL(imageUrl + image).openConnection()
                conn.connect()
                val input = conn.getInputStream()
                BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                null
            }
        }
    }
}