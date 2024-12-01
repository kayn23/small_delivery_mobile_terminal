package com.kafpin.jwtauth.ui.screens.StockSearchScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.dataStore.IpServerManager
import com.kafpin.jwtauth.network.services.AcceptCargoBody
import com.kafpin.jwtauth.network.services.AcceptCargoResponse
import com.kafpin.jwtauth.network.services.CargoService
import com.kafpin.jwtauth.ui.viewmodels.BaseViewModel
import com.kafpin.jwtauth.ui.viewmodels.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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