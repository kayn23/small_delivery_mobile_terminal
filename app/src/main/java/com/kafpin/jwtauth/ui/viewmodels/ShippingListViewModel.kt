package com.kafpin.jwtauth.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kafpin.jwtauth.network.ShippingService
import com.kafpin.jwtauth.models.shippings.Shipping
import com.kafpin.jwtauth.models.shippings.ShippingList
import com.kafpin.jwtauth.models.shippings.dto.AcceptCargoResponseDto
import com.kafpin.jwtauth.models.shippings.dto.AddCargoToShippingDto
import com.kafpin.jwtauth.models.shippings.dto.ApplyCargoDto
import com.kafpin.jwtauth.models.stocks.StockList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

// Результаты, которые могут быть возвращены
sealed class ShippingResult {
    data object Loading : ShippingResult()
    data class Success(val data: ShippingList) : ShippingResult()
    data class Error(val message: String) : ShippingResult()
    data class NetworkError(val error: String) : ShippingResult()
}



sealed class ApplyCargoResult {
    data object None : ApplyCargoResult()
    data object Loading : ApplyCargoResult()
    data class Success(val data: AcceptCargoResponseDto) : ApplyCargoResult()
    data class Error(val message: String) : ApplyCargoResult()
    data class NetworkError(val error: String) : ApplyCargoResult()
}

@HiltViewModel
class ShippingViewModel @Inject constructor(private val shippingService: ShippingService) :
    ViewModel() {

    // LiveData для отслеживания состояния
    val shippingListLiveData = liveData(Dispatchers.IO) {
        emit(ShippingResult.Loading) // Статус загрузки
        try {
            val response = shippingService.getShippings(mapOf("page" to "1"))
            if (response.isSuccessful) {
                response.body()?.let {
                    emit(ShippingResult.Success(it)) // Успешная загрузка
                } ?: emit(ShippingResult.Error("Empty response body"))
            } else {
                emit(ShippingResult.Error("Error: ${response.code()}")) // Ошибка от сервера
            }
        } catch (e: Exception) {
            emit(ShippingResult.Error("Exception: ${e.localizedMessage}")) // Ошибка в запросе
        }
    }

    private val _shippingsResult = MutableLiveData<ShippingResult>()
    val shippingResult: LiveData<ShippingResult> get() = _shippingsResult

    private val _shippingLiveData =
        MutableLiveData<ShippingInfoResult>(ShippingInfoResult.Loading)
    val shippingLiveData: LiveData<ShippingInfoResult> get() = _shippingLiveData

    val filters: Map<String, String> = emptyMap()

    fun getShippings(query: Map<String, String> = emptyMap()) {
        _shippingsResult.value = ShippingResult.Loading
        viewModelScope.launch {
            try {
                val response = shippingService.getShippings(query)
                if (response.isSuccessful) {
                    _shippingsResult.value = response.body()?.let { ShippingResult.Success(it) }
                } else {
                    val errorResponse = parseError(response.errorBody())
                    _shippingsResult.value = ShippingResult.Error(errorResponse)
                }
            } catch (e: HttpException) {
                val errorMessage = e.message ?: "Unknown HTTP error"
                _shippingsResult.value = ShippingResult.NetworkError(errorMessage)
            } catch (e: Exception) {
                _shippingsResult.value = ShippingResult.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    private val _cargoApplyData = MutableLiveData<ApplyCargoResult>(ApplyCargoResult.None)
    val cargoApplyData: LiveData<ApplyCargoResult> get() = _cargoApplyData

    fun applyCargo(body: ApplyCargoDto) {
        _cargoApplyData.value = ApplyCargoResult.Loading
        viewModelScope.launch {
            try {
                val response = shippingService.applyCargo(body)
                if (response.isSuccessful) {
                    _cargoApplyData.value = response.body()?.let { ApplyCargoResult.Success(it) }
                } else {
                    val errorResponse = parseError(response.errorBody())
                    _cargoApplyData.value = ApplyCargoResult.Error(errorResponse)
                }
            } catch (e: HttpException) {
                val errorMessage = e.message ?: "Unknown HTTP error"
                _cargoApplyData.value = ApplyCargoResult.NetworkError(errorMessage)
            } catch (e: Exception) {
                _cargoApplyData.value = ApplyCargoResult.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }



    // Метод для обработки кода ошибки
    private fun handleErrorResponse(response: Response<ShippingList>) {
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
        _shippingsResult.value =
            ShippingResult.Error(errorMessage) // Отправляем сообщение об ошибке на UI
    }

    // Метод для извлечения деталей ошибки из ResponseBody
    private fun parseError(responseBody: ResponseBody?): String {
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

