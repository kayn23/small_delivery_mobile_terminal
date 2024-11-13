package com.kafpin.jwtauth.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.R
import com.kafpin.jwtauth.models.InvoicePreview
import com.kafpin.jwtauth.ui.screens.components.InvoiceFilter
import com.kafpin.jwtauth.ui.screens.components.InvoicePreviewCard
import com.kafpin.jwtauth.ui.viewmodels.HomeViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    onClick: (id: String) -> Unit
) {
    val invoices by viewModel.invoices.observeAsState(emptyList())
    val loading by viewModel.loading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState(null)
    var showSwipeImage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        refreshData(viewModel)
    }

    val navigateToDetail by viewModel.navigateToDetail.observeAsState()
    navigateToDetail?.let { invoiceId ->
        HandleNavigation(invoiceId, viewModel, onClick)
    }

    LaunchedEffect(showSwipeImage) {
        if (showSwipeImage) {
            delay(600)
            showSwipeImage = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()

    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    if (dragAmount > 0 && !loading) {
                        showSwipeImage = true
                        refreshData(viewModel)
                    }
                    change.consume()
                }
            }) {
            InvoiceFilter(
                modifier = Modifier.fillMaxWidth(),
                onClick = { filter ->
                    viewModel.getInvoices(filter)
                    viewModel.saveFilter(filter)
                },
                initValue = viewModel.filters,
                onSearch = { query -> viewModel.getInvoices(query) }
            )
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                HorizontalDivider()
            }
            if (showSwipeImage) {
                SwipeRefreshImage(Modifier.align(Alignment.CenterHorizontally))
            }
            when {
                loading -> LoadingIndicator()
                errorMessage != null -> ErrorMessage(errorMessage)
                invoices.isNotEmpty() -> InvoicesList(
                    invoices,
                    onClick,
                    modifier = Modifier.pointerInput(Unit) {
                        detectVerticalDragGestures { change, dragAmount ->
                            if (dragAmount > 0 && !loading) {
                                showSwipeImage = true
                                refreshData(viewModel)
                            }
                            change.consume()
                        }
                    })

                else -> NoDataMessage()
            }
        }
    }
}

private fun refreshData(viewModel: HomeViewModel) {
    viewModel.getInvoices(filters = viewModel.filters)
}

@Composable
private fun HandleNavigation(
    invoiceId: String,
    viewModel: HomeViewModel,
    onClick: (String) -> Unit
) {
    LaunchedEffect(invoiceId) {
        onClick(invoiceId)
        viewModel.clearNavigoteToDetail()
    }
}

@Composable
fun SwipeRefreshImage(modifier: Modifier) {
    Image(
        painter = painterResource(R.drawable.images),
        contentDescription = "Swipe down to refresh",
        modifier = Modifier
            .size(100.dp)
            .padding(16.dp)
            .then(modifier)
    )
}

@Composable
fun LoadingIndicator() {
    Text(
        text = "История заказов загружается...",
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun ErrorMessage(message: String?) {
    Text(
        text = message ?: "Неизвестная ошибка",
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
}

@Composable
fun InvoicesList(
    invoices: List<InvoicePreview>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(invoices) { invoice ->
            InvoicePreviewCard(
                invoicePreview = invoice,
                modifier = Modifier
                    .fillMaxWidth()
                    .then(modifier),
                onClickMoreInfo = { onClick(invoice.id.toString()) }
            )
        }
    }
}

@Composable
fun NoDataMessage() {
    Text("Нет данных", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
}