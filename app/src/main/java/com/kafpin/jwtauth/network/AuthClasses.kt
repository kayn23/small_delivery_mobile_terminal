package com.kafpin.jwtauth.network

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val token: String, val role: String, val userId: Int)