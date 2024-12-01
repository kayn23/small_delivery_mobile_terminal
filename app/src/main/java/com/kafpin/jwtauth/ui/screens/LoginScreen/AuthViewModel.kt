package com.kafpin.jwtauth.ui.screens.LoginScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.dataStore.IpServerManager
import com.kafpin.jwtauth.data.dataStore.RoleManager
import com.kafpin.jwtauth.data.dataStore.TokenManager
import com.kafpin.jwtauth.network.services.AuthService
import com.kafpin.jwtauth.network.services.LoginRequest
import com.kafpin.jwtauth.network.services.LoginResponse
import com.kafpin.jwtauth.ui.viewmodels.BaseViewModel
import com.kafpin.jwtauth.ui.viewmodels.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager,
    private val roleManager: RoleManager,
    public override val ipServerManager: IpServerManager,
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