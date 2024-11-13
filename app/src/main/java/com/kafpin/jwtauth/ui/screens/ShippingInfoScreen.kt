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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kafpin.jwtauth.network.shippings.Cargo
import com.kafpin.jwtauth.network.shippings.ShippingOne
import com.kafpin.jwtauth.ui.screens.components.FloatingQrButton
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
        navController.currentBackStackEntry?.arguments?.getString("shippingId")?.toInt() ?: 1

    // Загрузка данных с использованием ViewModel
    val shippingResult by viewModel.shippingListLiveData.observeAsState()

    when (val result = shippingResult) {
        is ShippingInfoResult.Loading -> {
            // Показываем экран загрузки
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }

        is ShippingInfoResult.Success -> {
            // Показываем данные
            ShippingInfoCard(shipping = result.data, onReload = { viewModel.reloadShippingData() })
        }

        is ShippingInfoResult.Error -> {
            // Показываем ошибку
            Text("Error: ${result.message}")
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
) {

    Box() {
        Column(modifier = Modifier
            .padding(16.dp)
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    if (dragAmount > 0) {
                        onReload()
                    }
                    change.consume()
                }
            })
        {
            // Информация о доставке
            ShippingInfo(shipping, onClick = {})

            Spacer(modifier = Modifier.height(16.dp))

            Direction(shipping = shipping)

            Spacer(modifier = Modifier.height(16.dp))

            CargoInfo(shipping = shipping)
        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateTime(dateString: String?): String? {
    if (dateString == null) return dateString
    // Парсим строку в Instant (время в UTC)
    val instant = Instant.parse(dateString)

    // Форматируем в человекочитаемый вид
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        .withZone(ZoneId.systemDefault())  // Преобразуем в локальное время

    return formatter.format(instant)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShippingInfo(shipping: ShippingOne, onClick: () -> Unit = {}) {
    // Состояние для открытия/закрытия меню
    var expanded by remember { mutableStateOf(false) }


    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Column(
            modifier = Modifier.weight(6f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Данные о доставке
            Text("ID: ${shipping.id ?: "N/A"}", style = MaterialTheme.typography.titleLarge)
            Text(
                "Created At: ${formatDateTime(shipping.createdAt) ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "End At: ${formatDateTime(shipping.endAt) ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Capacity: ${shipping.cargoes.sumOf { it.size ?: 0 }}/${shipping.capacity ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )

        }

        // Иконка для открытия выпадающего меню
//        IconButton(onClick = { expanded = !expanded; onClick }, modifier = Modifier.weight(1f)) {
//            Icon(
//                imageVector = Icons.Default.MoreVert, // Иконка "три точки"
//                contentDescription = "More options"
//            )
//            /*// Выпадающее меню
//            DropdownMenu(
//                expanded = expanded,
//                onDismissRequest = { expanded = false }, // Закрытие меню при клике вне
//            ) {
//                DropdownMenuItem(onClick = { *//* Действие при выборе первого пункта *//* }, text = {
//                    Text("Добавить груз")
//                })
//                DropdownMenuItem(onClick = { *//* Действие при выборе второго пункта *//* }, text = {
//                    Text("Option 2")
//                })
//            }*/
//        }


    }

}

@Composable
fun InfoCard(title: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun CargoItem(cargo: Cargo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
//        elevation = 2.dp,
        shape = MaterialTheme.shapes.small
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Cargo ID: ${cargo.id ?: "N/A"}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 4.dp))
            Text("Size: ${cargo.size ?: "N/A"}", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(bottom = 4.dp))
            Text(
                "Description: ${cargo.description ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )
//            Text("QR Code: ${cargo.qrcode ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            /*Text(
                "Invoice ID: ${cargo.invoiceId ?: "N/A"}",
                style = MaterialTheme.typography.bodyMedium
            )*/
//            Text("Stock ID: ${cargo.stockId ?: "N/A"}", style = MaterialTheme.typography.bodyMedium)
            /*cargo.placement?.let {
                Text(
                    "Placement: ${it.name + it.city?.name}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }*/
        }
    }
}

@Composable
fun CargoInfo(shipping: ShippingOne) {
    var expanded by remember { mutableStateOf(false) }

    // Список грузов
    if (shipping.cargoes.isNotEmpty()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cargoes (${shipping.cargoes.size}) ${shipping.cargoes.sumOf { it.size ?: 0 }}/${shipping.capacity}",
                style = MaterialTheme.typography.titleSmall
            )
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add cargo"
                )
            }
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(vertical = 4.dp), horizontalArrangement = Arrangement.Center) {
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Hide direction",
                modifier = Modifier
                    .size(24.dp)
                    .fillMaxWidth(),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        if (expanded) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(shipping.cargoes) { cargo ->
                    CargoItem(cargo)
                }
            }
        }
    }
}

@Composable
fun Direction(shipping: ShippingOne, modifier: Modifier = Modifier) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Направление",
                Modifier
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 2.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Hide direction",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )

        }
        if (expanded) {
            // Информация о пункте отправления
            shipping.startPoint?.let {
                InfoCard(
                    title = "Начальная точка",
                    content = "${it.name}\nУлица: ${it.address}\nГород: ${it.city?.name}"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Информация о пункте назначения
            shipping.endPoint?.let {
                InfoCard(
                    title = "Конечная точка",
                    content = "${it.name}\nУлица: ${it.address}\nГород: ${it.city?.name}"
                )
            }
        }
    }
}