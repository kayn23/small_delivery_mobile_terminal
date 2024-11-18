package com.kafpin.jwtauth.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kafpin.jwtauth.models.shippings.Cargo
import com.kafpin.jwtauth.models.shippings.ShippingOne
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.AddCargoDialog
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.CargoInfo
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.Direction
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.InfoCard
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.ShippingInfo
import com.kafpin.jwtauth.ui.viewmodels.ShippingInfoResult
import com.kafpin.jwtauth.ui.viewmodels.ShippingInfoViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShippingInfoScreen(
    navController: NavController,
    viewModel: ShippingInfoViewModel = hiltViewModel()
) {
    val shippingId =
        navController.currentBackStackEntry?.arguments?.getString("id")?.toInt() ?: 1

    LaunchedEffect(Unit) {
        viewModel.reloadShippingData(shippingId)
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
            ShippingInfoCard(shipping = result.data, onReload = { viewModel.reloadShippingData(shippingId) }, onAddCargo = {cargoId ->
                    viewModel.addCargoToShipping(shippingId = result.data.id!!, cargoId = cargoId)
            })
        }

        is ShippingInfoResult.Error -> {
            // Показываем ошибку
            Column {
                Text("Error: ${result.message}")
                Button(onClick = {viewModel.reloadShippingData(shippingId)}) {
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


}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShippingInfoCard(
    shipping: ShippingOne,
    onReload: () -> Unit = {},
    onAddCargo: (id: Int) -> Unit = {},
) {


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        )
        {
            // Информация о доставке
            ShippingInfo(shipping, onClick = {}, modifier = Modifier.pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    if (dragAmount > 0) {
                        onReload()
                    }
                    change.consume()
                }
            })

            Spacer(modifier = Modifier.height(16.dp))

            Direction(shipping = shipping)

            Spacer(modifier = Modifier.height(16.dp))

            CargoInfo(shipping = shipping, addCargoClick = { cargoId ->
                onAddCargo(cargoId)
                // TODO
            }
            )

        }

    }

}


