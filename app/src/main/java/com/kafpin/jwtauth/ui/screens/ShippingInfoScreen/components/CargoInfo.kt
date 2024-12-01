package com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.components

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
import com.kafpin.jwtauth.models.shippings.ShippingOne
import com.kafpin.jwtauth.utils.parseQRCode

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
