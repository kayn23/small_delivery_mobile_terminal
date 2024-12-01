package com.kafpin.jwtauth.ui.screens.StockSearchScreen.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.kafpin.jwtauth.ui.components.IpInputDialog
import com.kafpin.jwtauth.ui.screens.StockSearchScreen.AcceptCargoViewModel
import com.kafpin.jwtauth.ui.viewmodels.RequestResult


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