package com.kafpin.jwtauth.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ErrorMessageDialog(errorMessage: String?, onClose: () -> Unit = {}) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text("Error") },
        text = {
            Column {
                if (errorMessage != null) Text(errorMessage)
                else {
                    Text("Unknown error")
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