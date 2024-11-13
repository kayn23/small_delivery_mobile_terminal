package com.kafpin.jwtauth.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kafpin.jwtauth.R

@Composable
fun FloatingQrButton(
    modifier: Modifier = Modifier,
) {
    // Создаем interactionSource для отслеживания состояний нажатия
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()

    // Используем состояние нажатия для изменения фона
    val buttonBackgroundColor = if (pressed) {
        MaterialTheme.colorScheme.primary // Цвет при нажатии
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer // Цвет по умолчанию
    }
    FloatingActionButton(
        onClick = { /* Действие при нажатии на кнопку */ },
        modifier = modifier,
        containerColor = buttonBackgroundColor,
        interactionSource = interactionSource, // Передаем interactionSource
    ) {
        Image(
            modifier = Modifier.width(40.dp),
            painter = painterResource(id = R.drawable.qr_code), // Используем изображение QR-кода
            contentDescription = "QR Code"
        )
    }
}