package com.kafpin.jwtauth.ui.screens.StockSearchScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kafpin.jwtauth.ui.components.StockItem
import com.kafpin.jwtauth.ui.components.StockSelectorModal
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.components.CargoQrData
import com.kafpin.jwtauth.ui.screens.StockSearchScreen.components.AcceptResultDialogWrapper
import com.kafpin.jwtauth.ui.screens.StockSearchScreen.components.AdminBottomBar
import com.kafpin.jwtauth.ui.screens.StockSearchScreen.components.ApproveCargoDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun StockSearchScreen(
    cargoViewModel: AcceptCargoViewModel = hiltViewModel(),
    viewModel: StockSelectScreenViewModel = hiltViewModel()
) {
    val stockInfo by viewModel.stockInfoManager.stockInfoFlow.collectAsState(null)

    var isScanDialogOpen by remember { mutableStateOf(false) }
    var cargoInfo by remember { mutableStateOf<CargoQrData?>(null) }

    var showSelectStockModal by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                AdminBottomBar(
                    onScanned = { stock ->
                        cargoInfo = stock
                        isScanDialogOpen = true
                    }
                )
            }
        }
    ) { padding ->
        // Отображение UI
        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = padding.calculateBottomPadding())
        ) {
            stockInfo?.let {
                Box(modifier = Modifier.padding(vertical = 4.dp)) {
                    StockItem(
                        stock = stockInfo!!,
                    )
                }
            }
            if (showSelectStockModal) {
                StockSelectorModal(
                    onSuccess = { stock ->
                        viewModel.saveStock(stock)
                        showSelectStockModal = false
                    },
                    onClose = {
                        showSelectStockModal = false
                    }
                )
            } else {
                TextButton(onClick = {
                    showSelectStockModal = true
                }) {
                    Text("Изменить пункт приема товаров")
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
