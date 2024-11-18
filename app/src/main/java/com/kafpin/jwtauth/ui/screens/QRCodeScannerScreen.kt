package com.kafpin.jwtauth.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import com.journeyapps.barcodescanner.CaptureActivity
import com.kafpin.jwtauth.utils.parseQRCode

@Composable
fun QRCodeScannerScreen(onQRCodeScanned: (String) -> Unit) {
    val context = LocalContext.current

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val qrCodeResult = data?.getStringExtra("SCAN_RESULT")
            qrCodeResult?.let(onQRCodeScanned)
        }
    }

    Button(onClick = {
        val intent = Intent(context, CaptureActivity::class.java)
        scannerLauncher.launch(intent)
    }) {
        Text("Сканировать QR-код")
    }
}

data class YourDataClass(val cargoId: Int, val invoiceId: Int)

@Composable
fun MainScreen() {
    val TAG = "mainScreen"
    QRCodeScannerScreen { qrCodeData ->
        val parsedData = parseQRCode<YourDataClass>(qrCodeData)
        if (parsedData != null) {
            Log.d(TAG, "MainScreen: cargoId: ${parsedData.cargoId}, invoiceId: ${parsedData.invoiceId}")
            // Используйте распарсенные данные
        } else {
            Log.d(TAG, "MainScreen: parsing error")
            // Обработка ошибки парсинга
        }
    }
}