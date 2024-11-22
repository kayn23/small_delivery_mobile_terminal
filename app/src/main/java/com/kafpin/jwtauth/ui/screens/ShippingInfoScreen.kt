package com.kafpin.jwtauth.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kafpin.jwtauth.models.shippings.ShippingOne
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.CargoInfo
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.Direction
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.ShippingInfo
import com.kafpin.jwtauth.ui.screens.components.StatusDialog
import com.kafpin.jwtauth.ui.viewmodels.RequestResult
import com.kafpin.jwtauth.ui.viewmodels.ShippingInfoResult
import com.kafpin.jwtauth.ui.viewmodels.ShippingInfoViewModel

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

    val deleteResult by viewModel.deleteResult.observeAsState()
    var deleteText by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(deleteResult) {
        when (val data = deleteResult) {
            is RequestResult.Error -> {
                deleteText = "Error: ${data.message}"
            }

            RequestResult.Loading ->
                deleteText = "Delete..."

            is RequestResult.NetworkError ->
                deleteText = "Network Error: ${data.error}"

            is RequestResult.Success -> {
                onDeleted()
            }

            null -> {}
        }
    }


    // Загрузка данных с использованием ViewModel
    val shippingResult by viewModel.shippingListLiveData.observeAsState()

    when (val result = shippingResult) {
        is ShippingInfoResult.Loading -> {
            // Показываем экран загрузки
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }

        is ShippingInfoResult.Success -> {
            // Показываем данные
            ShippingInfoCard(shipping = result.data,
                onReload = { viewModel.reloadShippingData(shippingId) },
                onAddCargo = { cargoId ->
                    viewModel.addCargoToShipping(shippingId = result.data.id!!, cargoId = cargoId)
                },
                onDelete = { id ->
                    viewModel.deleteShipping(id)
                }
            )
        }

        is ShippingInfoResult.Error -> {
            // Показываем ошибку
            Column {
                Text("Error: ${result.message}")
                Button(onClick = { viewModel.reloadShippingData(shippingId) }) {
                    Text("Reload")
                }
            }
        }

        is ShippingInfoResult.NetworkError -> {
            // Показываем ошибку сети
            Text("Network Error: ${result.error}")
        }

        null -> {
            Text("No data!")
        }
    }

    if (deleteText != null) {
        StatusDialog(
            deleteText!!,
            onClose = { deleteText = null })
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShippingInfoCard(
    modifier: Modifier = Modifier,
    shipping: ShippingOne,
    onReload: () -> Unit = {},
    onAddCargo: (id: Int) -> Unit = {},
    onDelete: (id: Int) -> Unit = {}
) {


    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        )
        {
            // Информация о доставке
            ShippingInfo(shipping,
                onClickDelete = {
                    onDelete(shipping.id!!)
                },
                modifier = Modifier.pointerInput(Unit) {
                    detectVerticalDragGestures { change, dragAmount ->
                        if (dragAmount > 0) {
                            onReload()
                        }
                        change.consume()
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Direction(shipping = shipping)

            Spacer(modifier = Modifier.height(16.dp))

            CargoInfo(
                shipping = shipping,
                addCargoClick = { cargoId ->
                    onAddCargo(cargoId)
                }
            )

        }

    }

}



