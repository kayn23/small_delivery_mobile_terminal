package com.kafpin.jwtauth.ui.screens.ShippingInfoScreen

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(dateString: String?): String? {
    if (dateString == null) return dateString
    // Парсим строку в Instant (время в UTC)
    val instant = Instant.parse(dateString)

    // Форматируем в человекочитаемый вид
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        .withZone(ZoneId.systemDefault())  // Преобразуем в локальное время

    return formatter.format(instant)
}
