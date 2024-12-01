package com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun AddCargoDialog(
    cargoId: Int?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, // Закрыть диалог при клике вне окна
        title = { Text("Add Cargo ${cargoId}") },
        text = { Text("Do you want to add cargo to the shipment?") },
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
