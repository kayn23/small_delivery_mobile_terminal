package com.kafpin.jwtauth.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.journeyapps.barcodescanner.CaptureActivity
import com.kafpin.jwtauth.R
import com.kafpin.jwtauth.data.Role
import com.kafpin.jwtauth.data.RoleManager
import com.kafpin.jwtauth.data.StockInfoManager
import com.kafpin.jwtauth.models.shippings.Shipping
import com.kafpin.jwtauth.models.shippings.ShippingList
import com.kafpin.jwtauth.models.stocks.Stock
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.CargoQrData
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.formatDateTime
import com.kafpin.jwtauth.ui.viewmodels.AcceptCargoResult
import com.kafpin.jwtauth.ui.viewmodels.AcceptCargoViewModel
import com.kafpin.jwtauth.ui.viewmodels.ShippingResult
import com.kafpin.jwtauth.ui.viewmodels.ShippingViewModel
import com.kafpin.jwtauth.utils.parseQRCode

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
    onStockNav: () -> Unit = {},
    roleManager: RoleManager,
    stockInfoManager: StockInfoManager,
    cargoViewModel: AcceptCargoViewModel = hiltViewModel(),
) {
    val shippingResult by viewModel.shippingResult.observeAsState(ShippingResult.Loading)
    val stockInfo by stockInfoManager.stockInfoFlow.collectAsState(null)

    val role by roleManager.roleFlow.collectAsState(null)

    LaunchedEffect(Unit) {
        refreshData(viewModel)
    }

    var isScanDialogOpen by remember { mutableStateOf(false) }
    var cargoInfo by remember { mutableStateOf<CargoQrData?>(null) }

    val context = LocalContext.current

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val qrCodeResult = data?.getStringExtra("SCAN_RESULT")
            qrCodeResult?.let {
                val data = parseQRCode<CargoQrData>(it)
                if (data != null) {
                    cargoInfo = data
                    isScanDialogOpen = true
                }
            }
        }
    }

    fun scanCargo() {
        val intent = Intent(context, CaptureActivity::class.java)
        scannerLauncher.launch(intent)
    }

    Scaffold(
        floatingActionButton = {
            /*        if (role == Role.Admin) {
                        FloatingActionButton(
                            onClick = { *//* Действие при нажатии *//* },
                    containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Image(
                        modifier = Modifier.width(40.dp),
                        painter = painterResource(id = R.drawable.qr_code), // Используем изображение QR-кода
                        contentDescription = "QR Code"
                    )
                }
            }*/
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create Shipping",
                        modifier = Modifier
                            .padding(10.dp)
                            .width(40.dp)
                    )
                    if (role === Role.Admin) {
                        Icon(
                            imageVector = Icons.Default.Place,
                            modifier = Modifier
                                .padding(10.dp)
                                .width(40.dp)
                                .clickable { onStockNav() },
                            contentDescription = "Select stock"
                        )
                        Box(modifier = Modifier.background(MaterialTheme.colorScheme.onSecondaryContainer)) {
                            Image(
                                modifier = Modifier
                                    .width(40.dp)
                                    .clickable { scanCargo() },
                                painter = painterResource(id = R.drawable.qr_code), // Используем изображение QR-кода
                                contentDescription = "QR Code"
                            )
                        }
                    }
                }
            }
        }
    ) {
        Box {
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
            if (isScanDialogOpen) {
                cargoInfo?.let { it1 ->
                    stockInfo?.let { it2 ->
                        if (it2.id != null) {
                            ApproveCargoDialog(
                                cargoId = it1.cargoId,
                                placement = it2,
                                onDismiss = {
                                    isScanDialogOpen = false
                                },
                                onConfirm = {
                                    cargoViewModel.acceptCargo(it1.cargoId, it2.id!!)
                                    isScanDialogOpen = false
                                }
                            )
                        }

                    }
                }
            }
            AcceptResultDialogWrapper(cargoViewModel)
        }
    }
}

private fun refreshData(viewModel: ShippingViewModel) {
    viewModel.getShippings(query = viewModel.filters)
}


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

    val acceptResult by cargoViewModel.acceptResult.observeAsState()

    fun onClose() {
        cargoViewModel.clearState()
    }

    when (val data = acceptResult) {
        is AcceptCargoResult.Error -> {
            AcceptCargoResultDialog(text = "Error: ${data.message}", onClose = { onClose() })
        }

        AcceptCargoResult.Loading -> {}

        is AcceptCargoResult.NetworkError -> {
            AcceptCargoResultDialog(text = "Error: ${data.error}", onClose = { onClose() })
        }

        is AcceptCargoResult.Success -> {
            AcceptCargoResultDialog(
                endInvoice = data.result.endInvoice,
                endShipping = data.result.endInvoice,
                onClose = { onClose() }
            )
        }

        null -> {}
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