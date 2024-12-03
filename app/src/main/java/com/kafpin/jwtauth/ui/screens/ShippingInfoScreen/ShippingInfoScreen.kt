package com.kafpin.jwtauth.ui.screens.ShippingInfoScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kafpin.jwtauth.ui.components.HandleRequestResult
import com.kafpin.jwtauth.ui.components.StatusDialog
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.components.ShippingInfoCard
import com.kafpin.jwtauth.ui.viewmodels.RequestResult

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShippingInfoScreen(
    navController: NavController,
    viewModel: ShippingInfoViewModel = hiltViewModel(),
    onDeleted: () -> Unit = {}
) {
    val shippingId =
        navController.currentBackStackEntry?.arguments?.getString("id")?.toInt() ?: 1

    LaunchedEffect(Unit) {
        viewModel.reloadShippingData(shippingId)
    }

    val deleteResult by viewModel.deleteResult.observeAsState(RequestResult.Init)
    var deleteText by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(deleteResult) {
        when (val data = deleteResult) {
            is RequestResult.Error -> {
                deleteText = "Error: ${data.message}"
            }

            RequestResult.Loading ->
                deleteText = "Delete..."

            is RequestResult.NetworkError ->
                deleteText = "Network Error: ${data.message}"

            is RequestResult.Success -> {
                onDeleted()
            }

            is RequestResult.ServerNotAvailable -> {
                deleteText = "Error: Server connection error"
            }

            is RequestResult.Init -> {}
        }
    }


    // Загрузка данных с использованием ViewModel
    val shippingResult by viewModel.shippingListLiveData.observeAsState(RequestResult.Init)

    HandleRequestResult(
        viewModel = viewModel,
        result = shippingResult,
        success = { result ->
            ShippingInfoCard(shipping = result.result,
                onReload = { viewModel.reloadShippingData(shippingId) },
                onAddCargo = { cargoId ->
                    viewModel.addCargoToShipping(shippingId = result.result.id!!, cargoId = cargoId)
                },
                onDelete = { id ->
                    viewModel.deleteShipping(id)
                }
            )
        },
        error = {
            TextButton(onClick = { viewModel.reloadShippingData(shippingId) }) {
                Text("Back")
            }
        },
        onDismiss = {
            viewModel.reloadShippingData(shippingId)
        }
    )

    if (deleteText != null) {
        StatusDialog(
            deleteText!!,
            onClose = { deleteText = null })
    }
}


