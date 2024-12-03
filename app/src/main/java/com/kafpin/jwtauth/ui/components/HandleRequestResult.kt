package com.kafpin.jwtauth.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kafpin.jwtauth.ui.viewmodels.BaseViewModel
import com.kafpin.jwtauth.ui.viewmodels.RequestResult

@Composable
fun <T> HandleRequestResult(
    viewModel: BaseViewModel,
    result: RequestResult<T>,
    success: @Composable (RequestResult.Success<T>) -> Unit = {},
    error: @Composable ((errorMessage: String?) -> Unit)? = null,
    init: @Composable () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    when (val data = result) {
        is RequestResult.Error -> {
            Column {
                if (error != null) {
                    error(data.message)
                } else
                    Text("Error: ${data.message}")
            }
        }

        RequestResult.Init -> {
            init()
        }

        RequestResult.Loading -> {
            Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                CircularProgressIndicator()
            }
        }

        is RequestResult.NetworkError -> {
            Column {
                if (error != null) {
                    error(data.message)
                } else
                    Text("Error: ${data.message}")
            }
        }

        RequestResult.ServerNotAvailable -> {
            IpInputDialog(
                onConfirm = { newIp ->
                    viewModel.saveServerIp(newIp)
                },
                ipServerManager = viewModel.ipServerManager,
                onDismiss = {
                    onDismiss()
                }
            )
        }

        is RequestResult.Success -> success(data)
    }
}