package com.kafpin.jwtauth.ui.screens.StockSearchScreen.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.kafpin.jwtauth.models.stocks.Stock

@Composable
fun ApproveCargoDialog(
    cargoId: Int,
    placement: Stock,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Approve cargo ${cargoId}") },
        text = { Text("Do you want to approve cargo to the placement \n${placement.name} ${placement.city!!.name}${placement.address}?") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer)
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer)
            ) {
                Text("Cancel")
            }
        }
    )
}

