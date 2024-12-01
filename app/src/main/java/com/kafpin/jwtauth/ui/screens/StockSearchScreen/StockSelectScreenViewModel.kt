package com.kafpin.jwtauth.ui.screens.StockSearchScreen

import androidx.lifecycle.viewModelScope
import com.kafpin.jwtauth.data.dataStore.IpServerManager
import com.kafpin.jwtauth.data.dataStore.StockInfoManager
import com.kafpin.jwtauth.models.stocks.Stock
import com.kafpin.jwtauth.ui.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockSelectScreenViewModel @Inject constructor(
    override val ipServerManager: IpServerManager,
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