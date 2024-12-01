package com.kafpin.jwtauth.ui.screens.StockSearchScreen.components

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.journeyapps.barcodescanner.CaptureActivity
import com.kafpin.jwtauth.R
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.components.CargoQrData
import com.kafpin.jwtauth.utils.parseQRCode

@Composable
fun AdminBottomBar(onScanned: (cargo: CargoQrData) -> Unit = {}) {
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
                    onScanned(data)
                }
            }
        }
    }

    fun scanCargo() {
        val intent = Intent(context, CaptureActivity::class.java)
        scannerLauncher.launch(intent)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.onSecondaryContainer)) {
            Image(
                modifier = Modifier
                    .width(40.dp)
                    .clickable {
                        scanCargo()
                    },
                painter = painterResource(id = R.drawable.qr_code), // Используем изображение QR-кода
                contentDescription = "QR Code"
            )
        }
    }
}