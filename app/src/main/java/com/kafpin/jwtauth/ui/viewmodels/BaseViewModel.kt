package com.kafpin.jwtauth.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kafpin.jwtauth.network.ErrorHandler
import com.kafpin.jwtauth.models.shippings.ShippingList
import okhttp3.ResponseBody
import retrofit2.Response

sealed class RequestResult<out T> {
    data object Loading : RequestResult<Nothing>()
    data class Success<T>(val result: T) : RequestResult<T>()
    data class Error(val message: String) : RequestResult<Nothing>()
    data class NetworkError(val error: String) : RequestResult<Nothing>()
}

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

    // Метод для обработки кода ошибки
    protected fun handleErrorResponse(response: Response<ShippingList>): String {
        val errorMessage = when (response.code()) {
            400 -> "Bad Request - Invalid input"
            401 -> "Unauthorized - You need to log in"
            403 -> "Forbidden - You don't have permission"
            404 -> "Not Found - The requested resource was not found"
            500 -> "Internal Server Error - Something went wrong on the server"
            502 -> "Bad Gateway - The server is down or unreachable"
            503 -> "Service Unavailable - Try again later"
            else -> "Unknown Error - ${response.message()}"
        }
        return errorMessage
    }

    // Метод для извлечения деталей ошибки из ResponseBody
    protected fun parseError(responseBody: ResponseBody?): String {
        return try {
            val gson = Gson()
            val errorJson = gson.fromJson(responseBody?.charStream(), JsonObject::class.java)
            // Вы можете адаптировать это под формат ошибки, который возвращает ваше API
            errorJson.get("message")?.asString ?: "Unknown error"
        } catch (e: Exception) {
            "Failed to parse error: ${e.localizedMessage}"
        }
    }
}