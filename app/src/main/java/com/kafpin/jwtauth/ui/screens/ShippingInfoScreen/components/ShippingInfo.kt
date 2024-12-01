package com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.models.shippings.ShippingOne
import com.kafpin.jwtauth.utils.formatDateTime


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShippingInfo(shipping: ShippingOne, onClickDelete: () -> Unit = {}, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier), verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(6f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Данные о доставке
            Text("ID: ${shipping.id ?: "N/A"}", style = MaterialTheme.typography.titleLarge)
            Text(
                "Created At: ${formatDateTime(shipping.createdAt) ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "End At: ${formatDateTime(shipping.endAt) ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Capacity: ${shipping.cargoes.sumOf { it.size ?: 0 }}/${shipping.capacity ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )

        }

        // Иконка для удаления
        if (shipping.cargoes.size == 0) {
            IconButton(onClick = { onClickDelete() }, modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = Icons.Default.Delete, // Иконка "три точки"
                    contentDescription = "Delete"
                )
            }
        }


    }

}
