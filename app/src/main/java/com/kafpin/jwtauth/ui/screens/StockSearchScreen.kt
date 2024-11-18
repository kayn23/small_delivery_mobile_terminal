package com.kafpin.jwtauth.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kafpin.jwtauth.data.StockInfoManager
import com.kafpin.jwtauth.models.stocks.Stock
import com.kafpin.jwtauth.ui.viewmodels.StockListViewModel
import com.kafpin.jwtauth.ui.viewmodels.StocksResult

@Composable
fun StockSearchScreen(
    viewModel: StockListViewModel = hiltViewModel(),
    stockInfoManager: StockInfoManager,
    backNav: () -> Unit = {}
) {
    val stocksResult by viewModel.stocksData.observeAsState(StocksResult.Loading)
    val stockSave by viewModel.stockSave.observeAsState(false)
    val stockInfo by stockInfoManager.stockInfoFlow.collectAsState(null)
    var searchText by remember { mutableStateOf("") }
    val stockList = remember { mutableStateOf<List<Stock>>(emptyList()) }

    // Обработчик изменения текста
    LaunchedEffect(searchText) {
        if (searchText.isNotEmpty()) {
            viewModel.searchStocks(searchText)
        } else {
            stockList.value = emptyList()
            viewModel.searchStocks("")
        }
    }

    LaunchedEffect(stockSave) {
        if (stockSave) {
            viewModel.setSaveStockStatus(false)
            backNav()
        }
    }

    // Отображение UI
    Column(modifier = Modifier.padding(16.dp)) {
        stockInfo?.let {
            Box(modifier = Modifier.padding(vertical = 4.dp)) {
                Text("acctual select stock ${stockInfo!!.name}, ${stockInfo!!.city?.name}, ${stockInfo!!.address}")
            }
        }
        // Поле ввода с подсказками
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Stocks") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        when (stocksResult) {
            is StocksResult.Loading -> {
                CircularProgressIndicator()
            }
            is StocksResult.Success -> {
                stockList.value = (stocksResult as StocksResult.Success).data.items
            }
            is StocksResult.Error -> {
                Text("Error: ${(stocksResult as StocksResult.Error).message}")
            }
            is StocksResult.NetworkError -> {
                Text("Network Error: ${(stocksResult as StocksResult.NetworkError).error}")
            }
        }

        // Список складов с возможностью выбора
        LazyColumn {
            items(stockList.value) { stock ->
                StockItem(
                    stock = stock,
                    onClick = {
                        viewModel.saveStockInfo(stock) // Сохранить выбранный склад
                    }
                )
            }
        }
    }
}

@Composable
fun StockItem(stock: Stock, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Text(stock.name ?: "No name", fontWeight = FontWeight.Bold)
        Text(stock.address ?: "No address")
        Text(stock.city?.name ?: "No city")
    }
}