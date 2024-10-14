package com.kafpin.jwtauth.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
) : ViewModel() {

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> get() = _token

    fun login(email: String, password: String, okCallback: () -> Unit = {}) {
        try {
            viewModelScope.launch {
                val response = authService.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    response.body()?.token?.let { token ->
                        Log.d("success", token)
                        tokenManager.saveToken(token)
                        _token.value = token
                        okCallback()
                        // somebody
                    }
                } else {
                    Log.d("error", "error")
                }
            }
        } catch (e: IOException) {
            // Обработка сетевых ошибок
            _token.value = null
            Log.e("AuthViewModel", "Network error: ${e.message}")
        } catch (e: Exception) {
            // Обработка других исключений
            _token.value = null
            Log.e("AuthViewModel", "Unexpected error: ${e.message}")
        }
    }
}