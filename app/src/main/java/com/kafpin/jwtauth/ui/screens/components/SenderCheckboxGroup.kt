package com.kafpin.jwtauth.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SenderCheckboxGroup(
    options: List<FilterOption>,
    selectedOptions: List<FilterOption>,
    onOptionSelected: (FilterOption) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                val isSelected = selectedOptions.contains(option)
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = {
                        if (isSelected) {
                            // Убираем опцию из выбранных
                            onOptionSelected(option.copy(label = "", value = ""))
                        } else {
                            // Добавляем опцию в выбранные
                            onOptionSelected(option)
                        }
                    }
                )
                Text(option.label, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}
