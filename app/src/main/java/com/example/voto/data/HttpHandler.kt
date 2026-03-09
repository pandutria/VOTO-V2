package com.example.voto.data

import android.util.Log
import com.example.voto.util.Helper
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class HttpHandler {
    fun request(
        endpoint: String,
        method: String = "GET",
        token: String? = null,
        rBody: String? = null
    ): String {
        val response = JSONObject()
        return try {
            val conn = URL(Helper.url + endpoint).openConnection() as HttpURLConnection
            conn.requestMethod = method
            conn.setRequestProperty("Content-Type", "application/json")

            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer $token")
            }

            if (rBody != null && method != "GET") {
                conn.doOutput = true
                conn.outputStream.use { it.write(rBody.toByteArray()) }
            }

            val code = conn.responseCode
            val body = try {
                conn.inputStream.bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                conn.errorStream.bufferedReader().use { it.readText() }
            }

            response.put("code", code)
            response.put("body", body)

            response.toString()
        } catch (e: Exception) {
            response.put("code", 500)
            response.put("body", e.message)
            Log.d("httpError", e.message.toString())

            response.toString()
        }
    }
}