package com.kafpin.jwtauth.network
import com.kafpin.jwtauth.data.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.kotlinFunction

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AuthRequired

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val method = request.tag(Invocation::class.java)?.method()

        // Проверяем, есть ли у метода аннотация AuthRequired
        val requiresAuth = method?.kotlinFunction?.findAnnotation<AuthRequired>() != null

        // Если токен нужен, добавляем его в заголовок
        if (requiresAuth) {
            val token = runBlocking { tokenManager.getToken() }
            val requestBuilder = request.newBuilder()
            if (token != null) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            return chain.proceed(requestBuilder.build())
        }

        // Если токен не нужен, просто продолжаем без изменений
        return chain.proceed(request)
    }
}
