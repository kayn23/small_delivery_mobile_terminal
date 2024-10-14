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
import com.kafpin.jwtauth.ui.screens.components.InvoicePreviewCard
import com.kafpin.jwtauth.ui.viewmodels.HomeViewModel
import coil.compose.rememberAsyncImagePainter
import com.kafpin.jwtauth.R
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
    fun refreshData() {
        viewModel.getInvoices()
    }

    LaunchedEffect(Unit) {
        viewModel.getInvoices()
    }

    LaunchedEffect(showSwipeImage) {
        if (showSwipeImage) {
            delay(600) // Задержка перед скрытием (например, 1 секунда)
            showSwipeImage = false
        }
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    if (dragAmount > 0 && !loading) { // Свайп вниз
                        showSwipeImage = true // Показываем всплывающее изображение
                        refreshData() // Обновляем данные
                    }
                    change.consume()
                }
            }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                HorizontalDivider()
            }
            // Всплывающее изображение для свайпа
            if (showSwipeImage) {
                Image(
                    painter = painterResource(R.drawable.images), // Замените на URL вашей картинки
                    contentDescription = "Swipe down to refresh",
                    modifier = Modifier
                        .size(100.dp) // Размер изображения
                        .padding(16.dp) // Отступы
                        .align(Alignment.CenterHorizontally) // Центрируем изображение по горизонтали
                )
            }

            when {
                loading -> {
                    Text(
                        text = "История заказов загружается...",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "Неизвестная ошибка",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                invoices.isNotEmpty() -> {
                    LazyColumn {
                        items(invoices) { invoice ->
                            InvoicePreviewCard(
                                invoicePreview = invoice,
                                modifier = Modifier.fillMaxWidth(),
                                onClickMoreInfo = {
                                    onClick(invoice.id.toString())
                                }
                            )
                        }
                    }
                }

                else -> {
                    Text("Нет данных")
                }
            }
        }
    }
}