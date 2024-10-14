package com.kafpin.jwtauth.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("signin")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}