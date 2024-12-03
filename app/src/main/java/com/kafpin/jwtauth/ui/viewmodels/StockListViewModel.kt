package com.kafpin.jwtauth.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.dataStore.IpServerManager
import com.kafpin.jwtauth.data.dataStore.StockInfoManager
import com.kafpin.jwtauth.models.stocks.Stock
import com.kafpin.jwtauth.models.stocks.StockList
import com.kafpin.jwtauth.network.services.StockService
import com.kafpin.jwtauth.network.services.createPaginationQueryMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockListViewModel @Inject constructor(
    private val stockService: StockService,
    private val stockInfoManager: StockInfoManager,
    override val ipServerManager: IpServerManager
) : BaseViewModel(ipServerManager) {

    private val _stocksData = MutableLiveData<RequestResult<StockList>>(RequestResult.Init)
    val stocksData: LiveData<RequestResult<StockList>> get() = _stocksData

    fun clearSockData() {
        _stocksData.value = RequestResult.Init
    }

    private var currentPage = 1
    private val perPage = 50  // или другой размер страницы

    private val _stockSave = MutableLiveData(false)

    fun searchStocks(query: String) {
        val paginationQuery = createPaginationQueryMap(
            page = currentPage,
            perPage = perPage,
        )
        viewModelScope.launch {
            safeApiCall(
                call = {
                    stockService.getStocks(
                        paginationQuery,
                        query,
                    )
                },
                _stocksData
            )
        }
    }
}