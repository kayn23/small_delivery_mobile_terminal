package com.kafpin.jwtauth.ui.screens

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.network.shippings.Shipping
import com.kafpin.jwtauth.network.shippings.ShippingList
import com.kafpin.jwtauth.ui.viewmodels.ShippingResult
import com.kafpin.jwtauth.ui.viewmodels.ShippingViewModel

// Composable для отображения информации о каждом Shipping
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

                    shipping.endPoint?.city?.let {
                        Text(
                            text = "End City: ${it.name ?: "Unknown"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

            }

            shipping?.endAt?.let {
                Text("end time: ${it}")
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

@Composable
fun ShippingListScreen(
    viewModel: ShippingViewModel,
    modifier: Modifier = Modifier,
    onSelectShipping: (id: String) -> Unit
) {
    val shippingResult by viewModel.shippingResult.observeAsState(ShippingResult.Loading)

    LaunchedEffect(Unit) {
        refreshData(viewModel)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
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
                    Text(text = "Ошибка: ${data.error}", style = MaterialTheme.typography.bodyLarge)
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

private fun refreshData(viewModel: ShippingViewModel) {
    viewModel.getShippings(query = viewModel.filters)
}


//// Основная Activity
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MyApplicationTheme {
//                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    // Здесь передаем наш список Shipping
//                    val shippingList = ShippingList(items = generateMockData())
//                    ShippingListScreen(shippingList = shippingList)
//                }
//            }
//        }
//    }
//
//    // Пример данных для отображения
//    private fun generateMockData(): ArrayList<Shipping> {
//        val mockShipping = ArrayList<Shipping>()
//        for (i in 1..10) {
//            mockShipping.add(
//                Shipping(
//                    id = i,
//                    capacity = (100..500).random(),
//                    startPoint = Stock(name = "Warehouse $i", city = City(name = "City $i")),
//                    endPoint = Stock(name = "Store $i", city = City(name = "City ${i + 1}"))
//                )
//            )
//        }
//        return mockShipping
//    }
//}