package com.kafpin.jwtauth.ui.screens.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun StatusDialog(text: String, onClose: () -> Unit = {}) {

    AlertDialog(
        onDismissRequest = { onClose() },
        title = { Text("OperationStatus") },
        text = {
            Text(text, style = MaterialTheme.typography.titleMedium)
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