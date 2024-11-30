package com.kafpin.jwtauth.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kafpin.jwtauth.data.IpServerManager
import com.kafpin.jwtauth.models.shippings.ShippingList
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownServiceException
import javax.inject.Inject


open class BaseViewModel @Inject constructor(
    private val ipServerManager: IpServerManager  // Инжектируем IpServerManager
) : ViewModel() {
    private val TAG = "ViewModel"

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

    protected fun httpErrorHandling(e: HttpException): RequestResult<Nothing> {
        return failedToConnectHandling(e)
    }

    protected fun failedToConnectHandling(e: Exception): RequestResult<Nothing> {
        if (e.message?.contains("failed to connect") == true) {
            return RequestResult.ServerNotAvailable
        } else {
            return RequestResult.NetworkError("Ошибка соединения: ${e.message}")
        }
    }

    protected fun UnknownServiceExceptionHandling(e: UnknownServiceException): RequestResult<Nothing> {
        val errorMessage = e.message ?: "Unknown error"
        if (errorMessage.contains("CLEARTEXT communication")) {
            return RequestResult.ServerNotAvailable
        }
        return RequestResult.Error(errorMessage)
    }

    fun handleException(e: Exception): RequestResult<Nothing> {
        Log.d(TAG, "handleException: ${e}")
        return when (e) {
            is SocketTimeoutException -> failedToConnectHandling(e)
            is UnknownServiceException -> UnknownServiceExceptionHandling(e)
            is ConnectException -> RequestResult.ServerNotAvailable
            is HttpException -> httpErrorHandling(e)
            else -> RequestResult.Error(e.message ?: "Неизвестная ошибка")
        }
    }

    fun <T> safeApiCall(
        call: suspend () -> Response<T>,
        _result: MutableLiveData<RequestResult<T>>,
        successCallback: suspend (value: T) -> Unit = {}
    ) {
        _result.value = RequestResult.Loading
        viewModelScope.launch {
            try {
                val response = call()
                if (response.isSuccessful) {
                    response.body()?.let { res ->
                        response.body()?.let { res ->
                            _result.value = response.body()?.let { RequestResult.Success(it) }
                            successCallback(res)
                        }
                    } ?: run {
                        _result.value = RequestResult.Error("Пустой ответ")
                    }
                } else {
                    val errorResponse = parseError(response.errorBody())
                    _result.value = RequestResult.Error(errorResponse)
                }

            } catch (e: Exception) {
                _result.value = handleException(e)
            }
        }
    }

    // Добавляем функцию для сохранения IP
    fun saveServerIp(ip: String) {
        viewModelScope.launch {
            try {
                ipServerManager.saveIpServer(ip)
                Log.d(TAG, "IP сохранен: $ip")
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при сохранении IP: ${e.localizedMessage}")
            }
        }
    }
}