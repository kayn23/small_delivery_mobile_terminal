package com.kafpin.jwtauth.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.TokenManager
import com.kafpin.jwtauth.network.AuthService
import com.kafpin.jwtauth.network.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager,
) :ViewModel(){

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> get() = _token

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val response = authService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                response.body()?.token?.let { token ->
                    tokenManager.saveToken(token)
                    _token.value = token
                    // somebody
                }
            }
        }
    }
}