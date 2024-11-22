package com.kafpin.jwtauth.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.network.ShippingService
import com.kafpin.jwtauth.models.shippings.Shipping
import com.kafpin.jwtauth.models.shippings.ShippingOne
import com.kafpin.jwtauth.models.shippings.dto.AddCargoToShippingDto
import com.kafpin.jwtauth.models.shippings.dto.CreateShippingDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

sealed class ShippingInfoResult {
    data object Loading : ShippingInfoResult()
    data class Success(val data: ShippingOne) : ShippingInfoResult()
    data class Error(val message: String) : ShippingInfoResult()
    data class NetworkError(val error: String) : ShippingInfoResult()
}

@HiltViewModel
class ShippingInfoViewModel @Inject constructor(
    private val shippingService: ShippingService,
    savedStateHandle: SavedStateHandle
) :
    BaseViewModel() {

    // Получаем shippingId из параметров навигации
    private val shippingId: Int = savedStateHandle.get<String>("shippingId")?.toInt() ?: 1

    // LiveData для отслеживания состояния загрузки
    private val _shippingListLiveData =
        MutableLiveData<ShippingInfoResult>(ShippingInfoResult.Loading)
    val shippingListLiveData: LiveData<ShippingInfoResult> get() = _shippingListLiveData

    // Метод для загрузки данных
    private suspend fun loadShippingData(id: Int) {
        _shippingListLiveData.postValue(ShippingInfoResult.Loading) // Статус загрузки
        try {
            val response = shippingService.getShipping(id)
            if (response.isSuccessful) {
                response.body()?.let {
                    _shippingListLiveData.postValue(ShippingInfoResult.Success(it)) // Успешная загрузка
                }
                    ?: _shippingListLiveData.postValue(ShippingInfoResult.Error("Empty response body"))
            } else {
                val errorMessage = parseError(response.errorBody())
                _shippingListLiveData.postValue(ShippingInfoResult.Error(errorMessage)) // Ошибка от сервера
            }
        } catch (e: HttpException) {
            val errorMessage = e.message ?: "Unknown HTTP error"
            _shippingListLiveData.postValue(ShippingInfoResult.NetworkError(errorMessage))
        } catch (e: Exception) {
            _shippingListLiveData.postValue(
                ShippingInfoResult.Error(
                    e.message ?: "Неизвестная ошибка"
                )
            )
        }
    }

    private suspend fun addCargo(shippingId: Int, cargoId: Int) {
        _shippingListLiveData.postValue(ShippingInfoResult.Loading)
        try {
            val response = shippingService.addCargoToShipping(
                shippingId,
                AddCargoToShippingDto(cargoId = cargoId)
            )
            if (response.isSuccessful) {
                response.body()?.let {
                    _shippingListLiveData.postValue(ShippingInfoResult.Success(it)) // Успешная загрузка
                }
                    ?: _shippingListLiveData.postValue(ShippingInfoResult.Error("Empty response body"))
            } else {
                val errorMessage = parseError(response.errorBody())
                _shippingListLiveData.postValue(ShippingInfoResult.Error(errorMessage)) // Ошибка от сервера
            }
        } catch (e: HttpException) {
            val errorMessage = e.message ?: "Unknown HTTP error"
            _shippingListLiveData.postValue(ShippingInfoResult.NetworkError(errorMessage))
        } catch (e: Exception) {
            _shippingListLiveData.postValue(
                ShippingInfoResult.Error(
                    e.message ?: "Неизвестная ошибка"
                )
            )
        }
    }

    fun addCargoToShipping(shippingId: Int, cargoId: Int) {
        viewModelScope.launch {
            addCargo(shippingId, cargoId)
        }
    }

    // Функция для ручной загрузки данных (повторный вызов)
    fun reloadShippingData(id: Int) {
        viewModelScope.launch {
            loadShippingData(id)
        }
    }

    private val _deleteResult = MutableLiveData<RequestResult<Boolean>?>()
    val deleteResult: LiveData<RequestResult<Boolean>?> get() = _deleteResult

    private suspend fun _deleteShipping(id: Int) {
        _deleteResult.postValue(RequestResult.Loading)
        try {
            val response = shippingService.removeShipping(id)
            if (response.isSuccessful) {
                _deleteResult.value = RequestResult.Success(true)
            } else {
                val errorResponse = parseError(response.errorBody())
                _deleteResult.value = RequestResult.Error(errorResponse)
            }
        } catch (e: HttpException) {
            val errorMessage = e.message ?: "Unknown HTTP error"
            _deleteResult.value = RequestResult.NetworkError(errorMessage)
        } catch (e: Exception) {
            _deleteResult.value = RequestResult.Error(e.message ?: "Неизвестная ошибка")
        }
    }

    fun clearDeleteResult() {
        _deleteResult.value = null
    }

    fun deleteShipping(id: Int) {
        viewModelScope.launch {
            _deleteShipping(id)
        }
    }
}