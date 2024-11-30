package com.kafpin.jwtauth.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.IpServerManager
import com.kafpin.jwtauth.data.RoleManager
import com.kafpin.jwtauth.data.TokenManager
import com.kafpin.jwtauth.network.AuthService
import com.kafpin.jwtauth.network.LoginRequest
import com.kafpin.jwtauth.network.LoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager,
    private val roleManager: RoleManager,
    public val ipServerManager: IpServerManager,
) : BaseViewModel(ipServerManager) {
    val TAG = "AuthViewModel"

    private val _result = MutableLiveData<RequestResult<LoginResponse>>()
    val requestResult: LiveData<RequestResult<LoginResponse>> get() = _result

    fun login(email: String, password: String, okCallback: () -> Unit = {}) {
        viewModelScope.launch {
            safeApiCall(
                call = { authService.login(LoginRequest(email, password)) },  // Вызов API
                _result = _result,  // Передаем MutableLiveData для обновления результата
                successCallback = { res ->  // Обрабатываем успешный результат
                    roleManager.saveRole(res.role)
                    roleManager.saveUserId(res.userId)
                    tokenManager.saveToken(res.token)
                    okCallback()  // Вызываем callback после успешной аутентификации
                }
            )
        }
    }

    fun clearState() {
        _result.value = RequestResult.Init
    }
}