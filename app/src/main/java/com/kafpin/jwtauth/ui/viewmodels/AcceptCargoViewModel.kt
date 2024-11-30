package com.kafpin.jwtauth.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.IpServerManager
import com.kafpin.jwtauth.network.AcceptCargoBody
import com.kafpin.jwtauth.network.AcceptCargoResponse
import com.kafpin.jwtauth.network.CargoService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AcceptCargoViewModel @Inject constructor(
    private val cargoService: CargoService,
    public val ipServerManager: IpServerManager
) :
    BaseViewModel(ipServerManager) {
    private val TAG = "AcceptCargoViewModel"
    private val _result = MutableLiveData<RequestResult<AcceptCargoResponse>>(RequestResult.Init)
    val acceptResult: LiveData<RequestResult<AcceptCargoResponse>> get() = _result

    fun clearState() {
        _result.value = RequestResult.Init
    }

    fun acceptCargo(cargoId: Int, stockId: Int) {
        viewModelScope.launch {
            safeApiCall(
                call = { cargoService.acceptCargo(AcceptCargoBody(cargoId, stockId)) },
                _result,
            )
        }
    }
}