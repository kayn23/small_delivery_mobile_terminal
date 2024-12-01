package com.kafpin.jwtauth.ui.screens.StockSearchScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun AcceptCargoResultDialog(
    endInvoice: Int? = null,
    endShipping: Int? = null,
    text: String? = null,
    onClose: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Accept result") },
        text = {
            Column {
                if (text != null) Text(text)
                else {
                    if (endInvoice != null) Text("Invoice delivered ${endInvoice}")
                    if (endShipping != null) Text("Shipping ended ${endShipping}")
                    Text("Cargo accept")
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