package com.kafpin.jwtauth.ui.screens.ShippingListScreen.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.models.shippings.Shipping
import com.kafpin.jwtauth.utils.formatDateTime

// Composable для отображения информации о каждом Shipping
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShippingItem(shipping: Shipping, onClickMoreInfo: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Shipping ID: ${shipping.id ?: "Unknown"}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Capacity: ${shipping.cargoes.sumOf { it.size ?: 0 }}/${shipping.capacity ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column {
                    Text(
                        text = "Start Point: ${shipping.startPoint?.name ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    shipping.startPoint?.city?.let {
                        Text(
                            text = "Start City: ${it.name ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Column {
                    Text(
                        text = "End Point: ${shipping.endPoint?.name ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "End address: ${shipping.endPoint?.address ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    shipping.endPoint?.city?.let {
                        Text(
                            text = "End City: ${it.name ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

            }

            shipping?.createdAt?.let {
                Text("created time: ${formatDateTime(it)}")
            }

            shipping?.endAt?.let {
                Text("end time: ${formatDateTime(it)}")
            }

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = onClickMoreInfo,
                ) {
                    Text("Подробней")
                }
            }

        }
    }
}
