package com.kafpin.jwtauth.ui.screens.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kafpin.jwtauth.models.InvoiceStatus

@Composable
fun InvoiceFilter(
    initValue: Map<String, String> = emptyMap(),
    onClick: (value: Map<String, String>) -> Unit,
    onSearch: (value: Map<String, String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var filtersVisible by remember { mutableStateOf(false) }
    val statusOption = InvoiceStatus.entries.map { i ->
        FilterOption(i.ruName, i.id.toString())
    }

    var selectedStatus by remember { mutableStateOf<FilterOption?>(null) }

    fun clearFilter() {
        selectedStatus = null
        onClick(emptyMap())
    }

    fun selectFilter() {
        // Формируем параметры запроса
        val options = mutableMapOf<String, String>()
        selectedStatus?.let { options["status_eq"] = it.value }
        onClick(options)
    }

    if (initValue["status_eq"] != null) selectedStatus =
        statusOption.find { i -> i.value == initValue["status_eq"] };

    Column(modifier) {
        // Кнопка для открытия/закрытия фильтров
        TextButton (onClick = { filtersVisible = !filtersVisible }) {
            Text(if (filtersVisible) "Скрыть фильтры" else "Показать фильтры")
        }

        AnimatedVisibility(filtersVisible) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterDropdown(
                        options = statusOption,
                        selectedOption = selectedStatus,
                        onOptionSelected = {
                            selectedStatus = it
                            selectFilter()
                        },
                        modifier = Modifier.weight(1f)
                    )
                    if (selectedStatus != null) {
                        IconButton(onClick = {
                            clearFilter()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete, // Используйте иконку мусорного ведра
                                contentDescription = "Удалить"
                            )
                        }
                    }
                }


                SearchInvoices {
                    if (it.isEmpty()) {
                        selectFilter()
                    } else {
                        onSearch(it)
                    }
                }
            }
        }
    }

}