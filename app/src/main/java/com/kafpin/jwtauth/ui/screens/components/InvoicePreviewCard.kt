package com.kafpin.jwtauth.ui.screens.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.models.InvoiceStatus
import com.kafpin.jwtauth.models.InvoicePreview

@Preview
@Composable
fun InvoicePreviewCard(
    invoicePreview: InvoicePreview = InvoicePreview(id = 10, status = 2),
    onClickMoreInfo: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Row(
            modifier = modifier.padding(vertical = 16.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text(
                    text = "Id: ${invoicePreview.id.toString()}",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = "Статус: ${InvoiceStatus.fromId(invoicePreview.status ?: 1)?.ruName}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column() {
                TextButton(
                    onClick = onClickMoreInfo,
//                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                ) {
                    Text("Подробней")
                }
            }
        }
        HorizontalDivider(
//            color = Color.Black,
            thickness = 1.dp,
            modifier = Modifier.align(Alignment.BottomCenter) // Размещаем Divider внизу
        )
    }
}