package com.kafpin.jwtauth.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.ui.viewmodels.HomeViewModel

@Composable
fun SearchInvoices(onClick: (value: Map<String, String>) -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Введите ID заказа") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(onClick = {
            if (searchQuery.isNotBlank()) {
                onClick(mapOf("id_eq" to searchQuery))
            }
            if (searchQuery.isBlank()) {
                onClick(emptyMap())
            }
        }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Поиск"
            )
        }
        if (searchQuery.isNotBlank()) {
            IconButton(onClick = {
                searchQuery = ""
                onClick(emptyMap())
            }) {
                Icon(
                    imageVector = Icons.Default.Delete, // Используйте иконку мусорного ведра
                    contentDescription = "Удалить"
                )
            }
        }
    }
}