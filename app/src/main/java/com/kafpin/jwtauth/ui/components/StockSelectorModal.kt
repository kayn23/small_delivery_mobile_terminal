package com.kafpin.jwtauth.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kafpin.jwtauth.models.stocks.Stock
import com.kafpin.jwtauth.ui.viewmodels.RequestResult
import com.kafpin.jwtauth.ui.viewmodels.StockListViewModel

@Composable
fun StockSelectorModal(viewModel: StockListViewModel = hiltViewModel(), onClose: () -> Unit = {}, onSuccess: (stock: Stock) ->  Unit = {}) {
    val stocksResult by viewModel.stocksData.observeAsState(RequestResult.Init)
    val stockList = remember { mutableStateOf<List<Stock>>(emptyList()) }
    var searchText by remember { mutableStateOf("") }

    LaunchedEffect(searchText) {
        if (searchText.isNotEmpty()) {
            viewModel.searchStocks(searchText)
        } else {
            stockList.value = emptyList()
            viewModel.searchStocks("")
        }
    }

    AlertDialog(
        onDismissRequest = {},
        title = { Text("Accept result") },
        text = {

            // Отображение UI
            Column(modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth()) {
                // Поле ввода с подсказками
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Search Stocks") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                HandleRequestResult(
                    viewModel = viewModel,
                    result = stocksResult,
                    success = {
                        stockList.value = (stocksResult as RequestResult.Success).result.items
                    },
                    onDismiss = {
                        viewModel.searchStocks(searchText)
                    }
                )

                // Список складов с возможностью выбора
                LazyColumn {
                    items(stockList.value) { stock ->
                        StockItem(
                            stock = stock,
                            onClick = {
                                onSuccess(stock)
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onClose,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer)
            ) {
                Text("Close")
            }
        }
    )
}