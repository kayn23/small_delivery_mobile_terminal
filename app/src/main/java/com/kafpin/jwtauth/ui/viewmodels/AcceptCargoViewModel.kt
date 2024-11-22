package com.kafpin.jwtauth.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.network.AcceptCargoBody
import com.kafpin.jwtauth.network.AcceptCargoResponse
import com.kafpin.jwtauth.network.CargoService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

sealed class AcceptCargoResult {
    data object Loading : AcceptCargoResult()
    data class Success(val result: AcceptCargoResponse) : AcceptCargoResult()
    data class Error(val message: String) : AcceptCargoResult()
    data class NetworkError(val error: String) : AcceptCargoResult()
}

@HiltViewModel
class AcceptCargoViewModel @Inject constructor(private val cargoService: CargoService) :
    BaseViewModel() {
    private val TAG = "AcceptCargoViewModel"
    private val _result = MutableLiveData<AcceptCargoResult?>()
    val acceptResult: LiveData<AcceptCargoResult?> get() = _result

    private suspend fun _acceptCargo(cargoId: Int, stockId: Int) {
        _result.postValue(AcceptCargoResult.Loading)
        try {
            val response = cargoService.acceptCargo(AcceptCargoBody(cargoId, stockId))
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d(TAG, "_acceptCargo: ${it.endShipping}")
                    _result.value = AcceptCargoResult.Success(it)
                }
            } else {
                val errorResponse = parseError(response.errorBody())
                Log.d(TAG, "_acceptCargo: network error ${errorResponse}")
                _result.value = AcceptCargoResult.Error(errorResponse)
            }
        } catch (e: HttpException) {
            val errorMessage = e.message ?: "Unknown HTTP error"
            _result.value = AcceptCargoResult.NetworkError(errorMessage)
        } catch (e: Exception) {
            _result.value = AcceptCargoResult.Error(e.message ?: "Неизвестная ошибка")
        }
    }

    fun clearState() {
        _result.value = null
    }

    fun acceptCargo(cargoId: Int, stockId: Int) {
        viewModelScope.launch {
            _acceptCargo(cargoId, stockId)
        }
    }
}