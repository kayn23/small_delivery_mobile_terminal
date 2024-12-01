package com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.models.shippings.Cargo

@Composable
fun CargoItem(cargo: Cargo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
//        elevation = 2.dp,
        shape = MaterialTheme.shapes.small
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Cargo ID: ${cargo.id ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                "Size: ${cargo.size ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                "Description: ${cargo.description ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}


