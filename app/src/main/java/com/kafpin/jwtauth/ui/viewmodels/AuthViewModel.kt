package com.kafpin.jwtauth.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.RoleManager
import com.kafpin.jwtauth.data.TokenManager
import com.kafpin.jwtauth.network.AuthService
import com.kafpin.jwtauth.network.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager,
    private val roleManager: RoleManager
) : ViewModel() {
    val TAG = "AuthViewModel"

    fun login(email: String, password: String, okCallback: () -> Unit = {}) {
        try {
            viewModelScope.launch {
                val response = authService.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    response.body()?.let { res ->

                        roleManager.saveRole(res.role)
                        tokenManager.saveToken(res.token)
                        Log.d(TAG, "token: ${res.token}")
                        Log.d(TAG, "Role: ${res.role}")
                        okCallback()
                    }
                } else {
                    Log.d("error", "error")
                }
            }
        } catch (e: IOException) {
            // Обработка сетевых ошибок
            Log.e("AuthViewModel", "Network error: ${e.message}")
        } catch (e: Exception) {
            // Обработка других исключений
            Log.e("AuthViewModel", "Unexpected error: ${e.message}")
        }
    }
}