package com.kafpin.jwtauth.ui.screens.CreateShippingScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kafpin.jwtauth.data.dataStore.Role
import com.kafpin.jwtauth.data.dataStore.RoleManager
import com.kafpin.jwtauth.models.shippings.ShortShippingOne
import com.kafpin.jwtauth.models.shippings.dto.CreateShippingDto
import com.kafpin.jwtauth.models.stocks.Stock
import com.kafpin.jwtauth.ui.components.HandleRequestResult
import com.kafpin.jwtauth.ui.components.StockSelectorModal
import com.kafpin.jwtauth.ui.viewmodels.RequestResult

@Composable
fun CreateShippingScreen(
    roleManager: RoleManager,
    viewModel: CreateShippingViewModel = hiltViewModel(),
    onCreated: (shortShippingOne: ShortShippingOne) -> Unit = {}
) {
    // Define state variables
    var startPoint by remember { mutableStateOf<Stock?>(null) }
    var endPoint by remember { mutableStateOf<Stock?>(null) }
    var capacity by remember { mutableStateOf<String?>(null) }
    var userIdInput by remember { mutableStateOf<String?>("") }

    val userId by roleManager.userIdFlow.collectAsState(null)
    val role by roleManager.roleFlow.collectAsState(null)
    val createShippingResult by viewModel.createShippingResult.observeAsState(RequestResult.Init)

    // State for showing the StockSelectorModal
    var showStartPointModal by remember { mutableStateOf(false) }
    var showEndPointModal by remember { mutableStateOf(false) }

    fun createShipping() {
        if (capacity != null && startPoint != null && endPoint != null && userIdInput != null) {
            viewModel.createShipping(
                CreateShippingDto(
                    capacity!!.trim().toInt(),
                    startPoint!!.id!!,
                    endPoint!!.id!!,
                    userIdInput!!.trim().toInt(),
                )
            )
        }
    }

    LaunchedEffect(createShippingResult) {
        if (createShippingResult is RequestResult.Success) {
            val data = (createShippingResult as RequestResult.Success<ShortShippingOne>).result
            onCreated(data)
        }
    }

    LaunchedEffect(role) {
        if (role != Role.Admin) {
            userIdInput = userId.toString()
        } else {
            userIdInput = ""
        }
    }

    // Handle showing StockSelectorModal for start and end points
    if (showStartPointModal) {
        StockSelectorModal(
            onClose = { showStartPointModal = false },
            onSuccess = { selectedStock ->
                startPoint = selectedStock
                showStartPointModal = false
            }
        )
    }

    if (showEndPointModal) {
        StockSelectorModal(
            onClose = { showEndPointModal = false },
            onSuccess = { selectedStock ->
                endPoint = selectedStock
                showEndPointModal = false
            }
        )
    }


    // Form to enter data
    Column(modifier = Modifier.padding(16.dp)) {
        if (role === Role.Admin) {
            OutlinedTextField(
                value = userIdInput ?: "",
                onValueChange = { value ->
                    userIdInput = value
                },
                label = { Text("UserId") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        OutlinedTextField(
            value = capacity ?: "",
            onValueChange = { value ->
                capacity = value
            },
            label = { Text("Capacity") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Text("м³")
            },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Start Point Field
        OutlinedTextField(
            value = if (startPoint != null) "${startPoint?.city?.name} ${startPoint?.address}" else "",
            onValueChange = {},
            label = { Text("Start Point") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showStartPointModal = true },
            enabled = false,
            trailingIcon = {
                IconButton(onClick = { showStartPointModal = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Search Start Point")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // End Point Field
        OutlinedTextField(
            value = if (endPoint != null) "${endPoint?.city?.name} ${endPoint?.address}" else "",
            onValueChange = {},
            label = { Text("End Point") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showEndPointModal = true },
            enabled = false,
            trailingIcon = {
                IconButton(onClick = { showEndPointModal = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Search End Point")
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Confirm Button to submit or save the data
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
            onClick = {
                createShipping()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit Shipping", style = MaterialTheme.typography.titleMedium)
        }

        HandleRequestResult(
            viewModel = viewModel,
            result = createShippingResult,
            onDismiss = {
                viewModel.clearState()
            }
        )
    }
}