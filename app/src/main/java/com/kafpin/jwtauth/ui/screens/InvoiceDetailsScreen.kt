package com.kafpin.jwtauth.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kafpin.jwtauth.models.InvoiceStatus
import com.kafpin.jwtauth.models.invoice.Cargoes
import com.kafpin.jwtauth.models.invoice.EndPointInfo
import com.kafpin.jwtauth.models.invoice.Invoice
import com.kafpin.jwtauth.models.invoice.RecipientInfo
import com.kafpin.jwtauth.models.invoice.SenderInfo
import com.kafpin.jwtauth.ui.viewmodels.InvoiceDetailsViewModel

@Composable
fun InvoiceDetailsScreen(
    navController: NavController,
    viewModel: InvoiceDetailsViewModel = hiltViewModel()
) {
    val navBackStackEntry = navController.currentBackStackEntry
    val id = navBackStackEntry?.arguments?.getString("id")

    val invoice by viewModel.invoice.observeAsState()
    val loading by viewModel.loading.observeAsState(false)

    LaunchedEffect(Unit) {
        viewModel.setInvoiceId(id);
    }

    if (loading) {
        Text("Loading invoice $id")
    } else {
        invoice?.let { invoiceDetail ->
            val invoice = requireNotNull(invoiceDetail.invoice)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                InvoiceDetails(invoice)
            }
        }
    }
}

@Composable
fun InvoiceDetails(invoice: Invoice, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text("id заказа ${invoice.id}", style = MaterialTheme.typography.titleLarge)
        Text(
            text = "Статус: ${InvoiceStatus.fromId(invoice.status ?: 0)?.ruName}",
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(10.dp))
        SenderPanel(senderInfo = invoice.senderInfo)
        Spacer(modifier = Modifier.height(8.dp))
        RecipientPanel(recipientInfo = invoice.recipientInfo)
        Spacer(modifier = Modifier.height(8.dp))
        EndPointPanel(endPointInfo = invoice.endPointInfo)
        Spacer(modifier = Modifier.height(8.dp))
        val price = if (invoice.price != null) {
            "${invoice.price}"
        } else {
            "Не указана"
        }
        Text(text = "Цена: ${price}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        CargoList(cargoes = invoice.cargoes)
    }
}

@Composable
fun RecipientPanel(recipientInfo: RecipientInfo?) {
    InfoPanel(
        label = "Получатель",
        info = "${recipientInfo?.name} ${recipientInfo?.surname} ${recipientInfo?.lastname}",
        email = recipientInfo?.email,
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Стрелка вправо"
            )
        })
}

@Composable
fun SenderPanel(senderInfo: SenderInfo?) {
    InfoPanel(
        label = "Отправитель",
        info = "${senderInfo?.name} ${senderInfo?.surname} ${senderInfo?.lastname}",
        email = senderInfo?.email,
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Стрелка влево"
            )
        })
}

@Composable
fun EndPointPanel(endPointInfo: EndPointInfo?) {
    InfoPanel(label = "Конечная точка", info = endPointInfo?.name ?: "Не указано")
}

@Composable
fun InfoPanel(
    label: String,
    info: String?,
    email: String? = null,
    icon: @Composable (() -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.weight(1f)) // Для выравнивания текста и иконки
            icon?.invoke() // Вызов иконки, если она передана
        }
        Text(
            text = info ?: "Не указано",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        if (email != null) Text(
            text = email ?: "Email не указан",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun CargoList(cargoes: List<Cargoes>) {
    Column {
        Text(text = "Грузы:", style = MaterialTheme.typography.titleMedium)
        cargoes.forEach { cargo ->
            Text(
                text = "id: ${cargo.id}, Вес: ${cargo.weight} кг",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (cargoes.isEmpty()) {
            Text(text = "Нет грузов", style = MaterialTheme.typography.bodyMedium)
        }
    }
}