package com.kafpin.jwtauth.utils

import com.google.gson.Gson

inline fun <reified T> parseQRCode(qrCodeData: String): T? {
    val gson = Gson()
    return try {
        gson.fromJson(qrCodeData, T::class.java)
    } catch (e: Exception) {
        null // Обработка ошибок парсинга
    }
}

