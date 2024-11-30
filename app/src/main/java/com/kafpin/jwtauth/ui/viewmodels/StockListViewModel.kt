package com.kafpin.jwtauth.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.IpServerManager
import com.kafpin.jwtauth.data.StockInfoManager
import com.kafpin.jwtauth.models.stocks.Stock
import com.kafpin.jwtauth.models.stocks.StockList
import com.kafpin.jwtauth.network.StockService
import com.kafpin.jwtauth.network.createPaginationQueryMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockListViewModel @Inject constructor(
    private val stockService: StockService,
    private val stockInfoManager: StockInfoManager,
    public val ipServerManager: IpServerManager
) : BaseViewModel(ipServerManager) {

    private val _stocksData = MutableLiveData<RequestResult<StockList>>(RequestResult.Init)
    val stocksData: LiveData<RequestResult<StockList>> get() = _stocksData

    fun clearSockData() {
        _stocksData.value = RequestResult.Init
    }

    private var currentPage = 1
    private val perPage = 50  // или другой размер страницы

    private val _stockSave = MutableLiveData(false)
    val stockSave: LiveData<Boolean> get() = _stockSave

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

    fun saveStockInfo(stock: Stock) {
        viewModelScope.launch {
            stockInfoManager.saveStockInfo(stock)
            _stockSave.value = true
        }
    }

    fun setSaveStockStatus(value: Boolean = false) {
        _stockSave.value = value
    }
}