package com.kafpin.jwtauth.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.network.ShippingService
import com.kafpin.jwtauth.network.shippings.Shipping
import com.kafpin.jwtauth.network.shippings.ShippingOne
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
    private val _shippingListLiveData = MutableLiveData<ShippingInfoResult>(ShippingInfoResult.Loading)
    val shippingListLiveData: LiveData<ShippingInfoResult> get() = _shippingListLiveData

    // Метод для загрузки данных
    private suspend fun loadShippingData() {
        _shippingListLiveData.postValue(ShippingInfoResult.Loading) // Статус загрузки
        try {
            val response = shippingService.getShipping(shippingId)
            if (response.isSuccessful) {
                response.body()?.let {
                    _shippingListLiveData.postValue(ShippingInfoResult.Success(it)) // Успешная загрузка
                } ?: _shippingListLiveData.postValue(ShippingInfoResult.Error("Empty response body"))
            } else {
                val errorMessage = parseError(response.errorBody())
                _shippingListLiveData.postValue(ShippingInfoResult.Error(errorMessage)) // Ошибка от сервера
            }
        } catch (e: HttpException) {
            val errorMessage = e.message ?: "Unknown HTTP error"
            _shippingListLiveData.postValue(ShippingInfoResult.NetworkError(errorMessage))
        } catch (e: Exception) {
            _shippingListLiveData.postValue(ShippingInfoResult.Error(e.message ?: "Неизвестная ошибка"))
        }
    }

    // Функция для ручной загрузки данных (повторный вызов)
    fun reloadShippingData() {
        viewModelScope.launch {
            loadShippingData()
        }
    }

    // Инициализация загрузки данных при старте
    init {
        viewModelScope.launch {
            loadShippingData()
        }
    }
}