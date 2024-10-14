package com.kafpin.jwtauth.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kafpin.jwtauth.network.ErrorHandler
import retrofit2.Response

open class BaseViewModel(): ViewModel() {
    val errorHandler = ErrorHandler()

    protected val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    protected val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    protected fun errorHandle(response: Response<*>) {
        val errorMessage = errorHandler.handleError(response)
        _errorMessage.value = errorMessage
    }

    var isRefreshing = MutableLiveData<Boolean>()

    protected fun handleException(exception: Exception) {
        _errorMessage.value = exception.message ?: "Неизвестная ошибка"
    }
}