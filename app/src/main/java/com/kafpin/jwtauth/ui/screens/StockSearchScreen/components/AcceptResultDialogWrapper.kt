package com.kafpin.jwtauth.ui.screens.StockSearchScreen.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.kafpin.jwtauth.ui.components.ErrorMessageDialog
import com.kafpin.jwtauth.ui.components.HandleRequestResult
import com.kafpin.jwtauth.ui.screens.StockSearchScreen.AcceptCargoViewModel
import com.kafpin.jwtauth.ui.viewmodels.RequestResult


@Composable
fun AcceptResultDialogWrapper(cargoViewModel: AcceptCargoViewModel) {

    val acceptResult by cargoViewModel.acceptResult.observeAsState(RequestResult.Init)

    fun onClose() {
        cargoViewModel.clearState()
    }

    HandleRequestResult(
        viewModel = cargoViewModel,
        result = acceptResult,
        success = { result ->
            AcceptCargoResultDialog(
                endInvoice = result.result.endInvoice,
                endShipping = result.result.endInvoice,
                onClose = { onClose() }
            )
        },
        error = { error ->
            ErrorMessageDialog(error) {
                cargoViewModel.clearState()
            }
        },
        onDismiss = {
            cargoViewModel.clearState()
        }
    )
}