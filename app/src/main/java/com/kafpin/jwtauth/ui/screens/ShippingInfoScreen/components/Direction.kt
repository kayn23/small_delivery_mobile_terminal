package com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.models.shippings.ShippingOne


@Composable
fun Direction(shipping: ShippingOne, modifier: Modifier = Modifier) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Направление",
                Modifier
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 2.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Hide direction",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )

        }
        if (expanded) {
            // Информация о пункте отправления
            shipping.startPoint?.let {
                InfoCard(
                    title = "Начальная точка",
                    content = "${it.name}\nУлица: ${it.address}\nГород: ${it.city?.name}"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Информация о пункте назначения
            shipping.endPoint?.let {
                InfoCard(
                    title = "Конечная точка",
                    content = "${it.name}\nУлица: ${it.address}\nГород: ${it.city?.name}"
                )
            }
        }
    }
}
