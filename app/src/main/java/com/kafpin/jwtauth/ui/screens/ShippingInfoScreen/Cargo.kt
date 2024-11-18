package com.kafpin.jwtauth.ui.screens.ShippingInfoScreen

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.CaptureActivity
import com.kafpin.jwtauth.models.shippings.Cargo
import com.kafpin.jwtauth.models.shippings.ShippingOne
import com.kafpin.jwtauth.utils.parseQRCode

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
            Text(
                "Cargo ID: ${cargo.id ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                "Size: ${cargo.size ?: "N/A"}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
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

data class CargoQrData(val cargoId: Int, val InvoiceId: Int)

@Composable
fun CargoInfo(shipping: ShippingOne, addCargoClick: (id: Int) -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }

    var isDialogOpen by remember { mutableStateOf(false) }

    var cargoId by remember { mutableStateOf(0) }

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
                    cargoId = data.cargoId
                    isDialogOpen = true
                }
            }
        }
    }

    fun scan() {
        val intent = Intent(context, CaptureActivity::class.java)
        scannerLauncher.launch(intent)
    }

    // Список грузов
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cargoes (${shipping.cargoes.size}) ${shipping.cargoes.sumOf { it.size ?: 0 }}/${shipping.capacity}",
                style = MaterialTheme.typography.titleSmall
            )
            IconButton(onClick = { scan() }) {
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


        // Диалоговое окно
        if (isDialogOpen) {
            AddCargoDialog(
                cargoId = cargoId,
                onDismiss = { isDialogOpen = false }, // Закрыть диалог
                onConfirm = {
                    // Действия при нажатии OK
                    addCargoClick(cargoId)
                    isDialogOpen = false
                }
            )
        }
    }
}


@Composable
fun AddCargoDialog(
    cargoId: Int?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, // Закрыть диалог при клике вне окна
        title = { Text("Add Cargo ${cargoId}") },
        text = { Text("Do you want to add cargo to the shipment?") },
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
