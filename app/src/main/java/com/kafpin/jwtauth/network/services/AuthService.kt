package com.kafpin.jwtauth.network.services

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val role: String, val userId: Int)

interface AuthService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}