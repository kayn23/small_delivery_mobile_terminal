package com.kafpin.jwtauth.ui.screens.LoginScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kafpin.jwtauth.ui.components.IpInputDialog
import com.kafpin.jwtauth.ui.viewmodels.RequestResult

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onSigned: () -> Unit,
    modifier: Modifier = Modifier.fillMaxSize()
) {
    var username by remember { mutableStateOf("admin@gmail.com") }
    var password by remember { mutableStateOf("232111") }

    val requestResult by viewModel.requestResult.observeAsState(RequestResult.Init)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .then(modifier)
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp, top = 30.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp)
        )
        Button(
            onClick = {
                viewModel.login(username, password, okCallback = onSigned)
            },
            enabled = requestResult !is RequestResult.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer)
        ) {
            Text("Login", fontSize = 20.sp)
        }

        when (val data = requestResult) {
            is RequestResult.Error -> {
                Text("Error: ${data.message}")
            }

            RequestResult.Loading -> {
                Row(horizontalArrangement = Arrangement.Center) {
                    CircularProgressIndicator()
                }
            }

            is RequestResult.NetworkError -> {
                Text(text = "Ошибка: ${data.error}", style = MaterialTheme.typography.bodyLarge)
            }

            is RequestResult.Success -> {}
            is RequestResult.Init -> {}
            is RequestResult.ServerNotAvailable -> {
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