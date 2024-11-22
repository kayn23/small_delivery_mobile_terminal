package com.kafpin.jwtauth.ui.screens.CreateShippingScreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.models.shippings.ShortShippingOne
import com.kafpin.jwtauth.models.shippings.dto.CreateShippingDto
import com.kafpin.jwtauth.network.AcceptCargoBody
import com.kafpin.jwtauth.network.AcceptCargoResponse
import com.kafpin.jwtauth.network.ShippingService
import com.kafpin.jwtauth.ui.viewmodels.AcceptCargoResult
import com.kafpin.jwtauth.ui.viewmodels.BaseViewModel
import com.kafpin.jwtauth.ui.viewmodels.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class CreateShippingViewModel @Inject constructor(private val shippingService: ShippingService) :
    BaseViewModel() {
    private val TAG = "CreateShippingViewModel"

    private val _result = MutableLiveData<RequestResult<ShortShippingOne>>()
    val createShippingResult: LiveData<RequestResult<ShortShippingOne>> get() = _result

    private suspend fun _createShipping(createShippingDto: CreateShippingDto) {
        _result.postValue(RequestResult.Loading)
        try {
            val response = shippingService.createShipping(createShippingDto)
            if (response.isSuccessful) {
                response.body()?.let {
                    _result.value = RequestResult.Success(it)
                }
            } else {
                val errorResponse = parseError(response.errorBody())
                Log.d(TAG, "_acceptCargo: network error ${errorResponse}")
                _result.value = RequestResult.Error(errorResponse)
            }
        } catch (e: HttpException) {
            val errorMessage = e.message ?: "Unknown HTTP error"
            _result.value = RequestResult.NetworkError(errorMessage)
        } catch (e: Exception) {
            _result.value = RequestResult.Error(e.message ?: "Неизвестная ошибка")
        }
    }

    fun createShipping(createShippingDto: CreateShippingDto) {
        viewModelScope.launch {
            _createShipping(createShippingDto)
        }
    }

}