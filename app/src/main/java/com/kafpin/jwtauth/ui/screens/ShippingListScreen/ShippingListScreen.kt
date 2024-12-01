package com.kafpin.jwtauth.ui.screens.ShippingListScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.ui.components.IpInputDialog
import com.kafpin.jwtauth.ui.screens.ShippingListScreen.components.ShippingListPreview
import com.kafpin.jwtauth.ui.viewmodels.RequestResult

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ShippingListScreen(
    viewModel: ShippingListViewModel,
    onSelectShipping: (id: String) -> Unit,
    onCreateShippingClick: () -> Unit = {},
) {
    val shippingResult by viewModel.shippingResult.observeAsState(RequestResult.Init)

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
                    is RequestResult.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
                    }

                    is RequestResult.Error -> {
                        Text(
                            text = "Ошибка: ${data.message}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    is RequestResult.NetworkError -> {
                        Text(
                            text = "Ошибка: ${data.error}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    is RequestResult.Success -> {
                        ShippingListPreview(
                            shippingList = data.result,
                            onSelectShipping = onSelectShipping,
                        )
                    }

                    RequestResult.Init -> {}
                    RequestResult.ServerNotAvailable -> {
                        IpInputDialog(
                            onConfirm = { newIp ->
                                viewModel.saveServerIp(newIp)
                            },
                            ipServerManager = viewModel.ipServerManager,
                            onDismiss = {
                                viewModel.clearState()
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun refreshData(viewModel: ShippingListViewModel) {
    val map = LinkedHashMap<String, String>()
    map["show_end"] = "true"
    viewModel.getShippings(query = map)
}
