package com.kafpin.jwtauth.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class FilterOption(val label: String, val value: String)

@Composable
fun FilterDropdown(
    options: List<FilterOption>,
    selectedOption: FilterOption?,
    onOptionSelected: (FilterOption) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier) {
        Text(
            text = selectedOption?.label ?: "Выберите фильтр",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }, text = {
                    Text(option.label)
                })
            }
        }
    }
}