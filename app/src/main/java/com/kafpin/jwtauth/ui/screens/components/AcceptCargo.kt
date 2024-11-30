package com.kafpin.jwtauth.ui.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.kafpin.jwtauth.models.stocks.Stock
import com.kafpin.jwtauth.ui.viewmodels.AcceptCargoViewModel
import com.kafpin.jwtauth.ui.viewmodels.RequestResult


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

@Composable
fun AcceptResultDialogWrapper(cargoViewModel: AcceptCargoViewModel) {

    val acceptResult by cargoViewModel.acceptResult.observeAsState(RequestResult.Init)

    fun onClose() {
        cargoViewModel.clearState()
    }

    when (val data = acceptResult) {
        is RequestResult.Error -> {
            AcceptCargoResultDialog(text = "Error: ${data.message}", onClose = { onClose() })
        }

        is RequestResult.Loading -> {}

        is RequestResult.NetworkError -> {
            AcceptCargoResultDialog(text = "Error: ${data.error}", onClose = { onClose() })
        }

        is RequestResult.Success -> {
            AcceptCargoResultDialog(
                endInvoice = data.result.endInvoice,
                endShipping = data.result.endInvoice,
                onClose = { onClose() }
            )
        }
        RequestResult.Init -> {}
        RequestResult.ServerNotAvailable -> {
            IpInputDialog(
                onConfirm = { newIp ->
                    cargoViewModel.saveServerIp(newIp)
                },
                ipServerManager = cargoViewModel.ipServerManager,
                onDismiss = {
                    cargoViewModel.clearState()
                }
            )
        }
    }
}

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