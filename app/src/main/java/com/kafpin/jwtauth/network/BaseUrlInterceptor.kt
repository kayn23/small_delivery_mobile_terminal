package com.kafpin.jwtauth.network

import com.kafpin.jwtauth.data.IpServerManager
import okhttp3.Interceptor
import okhttp3.Response

class BaseUrlInterceptor(private val ipServerManager: IpServerManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Получаем актуальный IP адрес сервера
        val ip = ipServerManager.ipStateFlow.value ?: "10.0.2.2" // IP по умолчанию если нет в данных

        // Формируем новый базовый URL
        val newBaseUrl = "http://$ip:3000/"

        // Создаем новый Request с обновленным URL
        val originalRequest = chain.request()
        val newUrl = originalRequest.url.newBuilder()
            .scheme("http") // Если нужно, можете заменить на https
            .host(ip)  // Здесь используем полученный IP
            .port(3000) // Если порт всегда один и тот же
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        // Выполняем запрос с обновленным URL
        return chain.proceed(newRequest)
    }
}