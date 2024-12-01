package com.kafpin.jwtauth.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.models.stocks.Stock


@Composable
fun StockItem(stock: Stock, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Text(stock.name ?: "No name", fontWeight = FontWeight.Bold)
        Text(stock.address ?: "No address")
        Text(stock.city?.name ?: "No city")
    }
}