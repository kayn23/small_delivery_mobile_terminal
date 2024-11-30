package com.kafpin.jwtauth.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.IpServerManager
import com.kafpin.jwtauth.models.shippings.ShippingList
import com.kafpin.jwtauth.network.ShippingService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShippingListViewModel @Inject constructor(
    private val shippingService: ShippingService,
    public val ipServerManager: IpServerManager
) :
    BaseViewModel(ipServerManager) {

    private val _shippingLiveData =
        MutableLiveData<RequestResult<ShippingList>>(RequestResult.Init)
    val shippingResult: LiveData<RequestResult<ShippingList>> get() = _shippingLiveData

    fun getShippings(query: Map<String, String> = emptyMap()) {
        viewModelScope.launch {
            safeApiCall(
                call = { shippingService.getShippings(query) },
                _shippingLiveData
            )
        }
    }

    fun clearState() {
        _shippingLiveData.value = RequestResult.Init
    }
}

