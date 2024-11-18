package com.kafpin.jwtauth.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.StockInfoManager
import com.kafpin.jwtauth.models.stocks.Stock
import com.kafpin.jwtauth.models.stocks.StockList
import com.kafpin.jwtauth.network.StockService
import com.kafpin.jwtauth.network.createPaginationQueryMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

sealed class StocksResult {
    data object Loading : StocksResult()
    data class Success(val data: StockList) : StocksResult()
    data class Error(val message: String) : StocksResult()
    data class NetworkError(val error: String) : StocksResult()
}

@HiltViewModel
class StockListViewModel @Inject constructor(
    private val stockService: StockService,
    private val stockInfoManager: StockInfoManager
) : BaseViewModel() {

    private val _stocksData = MutableLiveData<StocksResult>()
    val stocksData: LiveData<StocksResult> get() = _stocksData

    private var currentPage = 1
    private val perPage = 50  // или другой размер страницы

    private val _stockSave = MutableLiveData(false)
    val stockSave: LiveData<Boolean> get() = _stockSave

    fun searchStocks(query: String) {
        _stocksData.value = StocksResult.Loading
        viewModelScope.launch {
            try {
                val paginationQuery = createPaginationQueryMap(
                    page = currentPage,
                    perPage = perPage,
                )
                val response = stockService.getStocks(
                    paginationQuery,
                    query,
                )

                if (response.isSuccessful) {
                    _stocksData.value = response.body()?.let { StocksResult.Success(it) }
                } else {
                    val errorResponse = parseError(response.errorBody())
                    _stocksData.value = StocksResult.Error(errorResponse)
                }
            } catch (e: HttpException) {
                val errorMessage = e.message ?: "Unknown HTTP error"
                _stocksData.value = StocksResult.NetworkError(errorMessage)
            } catch (e: Exception) {
                _stocksData.value = StocksResult.Error(e.message ?: "Неизвестная ошибка")
            }
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