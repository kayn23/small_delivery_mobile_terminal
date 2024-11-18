package com.kafpin.jwtauth.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.RoleManager
import com.kafpin.jwtauth.data.TokenManager
import com.kafpin.jwtauth.network.AuthService
import com.kafpin.jwtauth.network.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed class LoginResult {
    data object Loading : LoginResult()
    data object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
    data class NetworkError(val error: String) : LoginResult()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager,
    private val roleManager: RoleManager
) : BaseViewModel() {
    val TAG = "AuthViewModel"

    private val _result = MutableLiveData<LoginResult>()
    val requestResult: LiveData<LoginResult> get() = _result

    fun login(email: String, password: String, okCallback: () -> Unit = {}) {
        _result.value = LoginResult.Loading
        try {
            viewModelScope.launch {
                val response = authService.login(LoginRequest(email, password))
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        roleManager.saveRole(res.role)
                        tokenManager.saveToken(res.token)
                        Log.d(TAG, "token: ${res.token}")
                        Log.d(TAG, "Role: ${res.role}")
                        _result.value = LoginResult.Success
                        okCallback()
                    }
                } else {
                    val errorResponse = parseError(response.errorBody())
                    _result.value = LoginResult.Error(errorResponse)
                }

            }
        } catch (e: HttpException) {
            val errorMessage = e.message ?: "Unknown HTTP error"
            _result.value = LoginResult.NetworkError(errorMessage)
        } catch (e: Exception) {
            _result.value = LoginResult.Error(e.message ?: "Неизвестная ошибка")
        }
    }
}