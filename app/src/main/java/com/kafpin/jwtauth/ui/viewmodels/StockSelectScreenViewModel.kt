package com.kafpin.jwtauth.ui.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.IpServerManager
import com.kafpin.jwtauth.data.StockInfoManager
import com.kafpin.jwtauth.models.stocks.Stock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockSelectScreenViewModel @Inject constructor(
    private val ipServerManager: IpServerManager,
    public val stockInfoManager: StockInfoManager
) : BaseViewModel(
    ipServerManager
) {
    fun saveStock(stock: Stock) {
        viewModelScope.launch {
            stockInfoManager.saveStockInfo(stock)
        }
    }
}