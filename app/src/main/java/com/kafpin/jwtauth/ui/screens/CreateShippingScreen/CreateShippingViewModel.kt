package com.kafpin.jwtauth.ui.screens.CreateShippingScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.dataStore.IpServerManager
import com.kafpin.jwtauth.models.shippings.ShortShippingOne
import com.kafpin.jwtauth.models.shippings.dto.CreateShippingDto
import com.kafpin.jwtauth.network.services.ShippingService
import com.kafpin.jwtauth.ui.viewmodels.BaseViewModel
import com.kafpin.jwtauth.ui.viewmodels.RequestResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateShippingViewModel @Inject constructor(
    private val shippingService: ShippingService,
    public override val ipServerManager: IpServerManager,
) :
    BaseViewModel(ipServerManager) {
    private val TAG = "CreateShippingViewModel"

    private val _result = MutableLiveData<RequestResult<ShortShippingOne>>()
    val createShippingResult: LiveData<RequestResult<ShortShippingOne>> get() = _result

    fun createShipping(createShippingDto: CreateShippingDto) {
        viewModelScope.launch {
            safeApiCall(
                call = { shippingService.createShipping(createShippingDto) },
                _result
            )
        }
    }

    fun clearState() {
        _result.postValue(RequestResult.Init)
    }

}