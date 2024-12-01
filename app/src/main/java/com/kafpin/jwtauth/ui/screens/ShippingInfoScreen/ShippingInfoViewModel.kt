package com.kafpin.jwtauth.ui.screens.ShippingInfoScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.dataStore.IpServerManager
import com.kafpin.jwtauth.network.services.ShippingService
import com.kafpin.jwtauth.models.shippings.ShippingOne
import com.kafpin.jwtauth.models.shippings.ShortShippingOne
import com.kafpin.jwtauth.models.shippings.dto.AddCargoToShippingDto
import com.kafpin.jwtauth.ui.viewmodels.BaseViewModel
import com.kafpin.jwtauth.ui.viewmodels.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShippingInfoViewModel @Inject constructor(
    private val shippingService: ShippingService,
    override val ipServerManager: IpServerManager
) :
    BaseViewModel(ipServerManager) {


    // LiveData для отслеживания состояния загрузки
    private val _shippingListLiveData =
        MutableLiveData<RequestResult<ShippingOne>>(RequestResult.Init)
    val shippingListLiveData: LiveData<RequestResult<ShippingOne>> get() = _shippingListLiveData
    fun clearShippingState() {
        _shippingListLiveData.value = RequestResult.Init
    }

    fun addCargoToShipping(shippingId: Int, cargoId: Int) {
        viewModelScope.launch {
            safeApiCall(
                call = {
                    shippingService.addCargoToShipping(
                        shippingId,
                        AddCargoToShippingDto(cargoId)
                    )
                },
                _shippingListLiveData
            )
        }
    }

    // Метод для загрузки данных
    fun reloadShippingData(id: Int) {
        viewModelScope.launch {
            safeApiCall(
                call = { shippingService.getShipping(id) },
                _shippingListLiveData,
            )
        }
    }

    private val _deleteResult = MutableLiveData<RequestResult<ShortShippingOne>>(RequestResult.Init)
    val deleteResult: LiveData<RequestResult<ShortShippingOne>> get() = _deleteResult

    fun deleteShipping(id: Int) {
        viewModelScope.launch {
            safeApiCall(
                call = { shippingService.removeShipping(id) },
                _deleteResult
            )
        }
    }

}