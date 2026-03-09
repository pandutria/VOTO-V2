package com.example.voto.data

import android.content.Context

class TokenManager(context: Context) {
    val pref = "pref"
    val key = "key"

    val shared = context.getSharedPreferences(pref, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        shared.edit().putString(key, token).apply()
    }

    fun getToken(): String? {
        return shared.getString(key, null)
    }

    fun remoeToken() {
        shared.edit().remove(key).apply()
    }
}