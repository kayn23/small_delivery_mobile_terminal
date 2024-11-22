package com.kafpin.jwtauth.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.data.RoleManager
import com.kafpin.jwtauth.data.StockInfoManager
import com.kafpin.jwtauth.models.shippings.Shipping
import com.kafpin.jwtauth.models.shippings.ShippingList
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.formatDateTime
import com.kafpin.jwtauth.ui.viewmodels.ShippingResult
import com.kafpin.jwtauth.ui.viewmodels.ShippingViewModel

// Composable для отображения информации о каждом Shipping
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShippingItem(shipping: Shipping, onClickMoreInfo: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Shipping ID: ${shipping.id ?: "Unknown"}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Text(
                text = "Capacity: ${shipping.cargoes.sumOf { it.size ?: 0 }}/${shipping.capacity ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Column {
                    Text(
                        text = "Start Point: ${shipping.startPoint?.name ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    shipping.startPoint?.city?.let {
                        Text(
                            text = "Start City: ${it.name ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Column {
                    Text(
                        text = "End Point: ${shipping.endPoint?.name ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "End address: ${shipping.endPoint?.address ?: "Unknown"}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    shipping.endPoint?.city?.let {
                        Text(
                            text = "End City: ${it.name ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

            }

            shipping?.createdAt?.let {
                Text("created time: ${formatDateTime(it)}")
            }

            shipping?.endAt?.let {
                Text("end time: ${formatDateTime(it)}")
            }

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = onClickMoreInfo,
                ) {
                    Text("Подробней")
                }
            }

        }
    }
}

// Composable для отображения списка
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShippingListPreview(
    shippingList: ShippingList,
    modifier: Modifier = Modifier,
    onSelectShipping: (id: String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .then(modifier)
    ) {
        items(shippingList.items) { shipping ->
            ShippingItem(shipping, onClickMoreInfo = {
                onSelectShipping(shipping.id.toString())
            })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShippingListScreen(
    viewModel: ShippingViewModel,
    onSelectShipping: (id: String) -> Unit,
    roleManager: RoleManager,
    stockInfoManager: StockInfoManager,
    onCreateShippingClick: () -> Unit = {},
) {
    val shippingResult by viewModel.shippingResult.observeAsState(ShippingResult.Loading)
    val stockInfo by stockInfoManager.stockInfoFlow.collectAsState(null)

    val role by roleManager.roleFlow.collectAsState(null)

    LaunchedEffect(Unit) {
        refreshData(viewModel)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onCreateShippingClick() },
                containerColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Shipping",
                    tint = Color.Black,
                    modifier = Modifier
                        .padding(10.dp)
                        .width(40.dp)
                )
            }
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures { change, dragAmount ->
                            if (dragAmount > 0) {
                                refreshData(viewModel)
                            }
                            change.consume()
                        }
                    }
            ) {
                when (val data = shippingResult) {
                    is ShippingResult.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                    }

                    is ShippingResult.Error -> {
                        Text(
                            text = "Ошибка: ${data.message}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    is ShippingResult.NetworkError -> {
                        Text(
                            text = "Ошибка: ${data.error}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    is ShippingResult.Success -> {
                        ShippingListPreview(
                            shippingList = data.data,
                            onSelectShipping = onSelectShipping,
                        )
                    }
                }
            }
        }
    }
}

private fun refreshData(viewModel: ShippingViewModel) {
    val map = LinkedHashMap<String, String>()
    map["show_end"] = "true"
    viewModel.getShippings(query = map)
}

