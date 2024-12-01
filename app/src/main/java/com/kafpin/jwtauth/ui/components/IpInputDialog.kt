package com.kafpin.jwtauth.ui.components


import android.annotation.SuppressLint
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import com.kafpin.jwtauth.data.dataStore.IpServerManager

@SuppressLint("RememberReturnType")
@Composable
fun IpInputDialog(
    onDismiss: () -> Unit = {},
    onConfirm: (String) -> Unit,
    ipServerManager: IpServerManager
) {
    val ipAddressState = remember { mutableStateOf(TextFieldValue("")) }
    val serverIp = ipServerManager.ipFlow.collectAsState(null)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Сервер ${serverIp.value ?: ""} не доступен.\nВведите IP-адрес сервера") },
        text = {
            TextField(
                value = ipAddressState.value,
                onValueChange = { ipAddressState.value = it },
                label = { Text("IP-адрес") }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(ipAddressState.value.text)
                    onDismiss()
                }
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}