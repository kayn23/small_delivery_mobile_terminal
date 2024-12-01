package com.kafpin.jwtauth.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kafpin.jwtauth.ui.AppDestinations
import com.kafpin.jwtauth.R
import com.kafpin.jwtauth.data.dataStore.TokenManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Header(
    currentScreen: AppDestinations,
    canNavigateBack: Boolean,
    onNavigateUpClicked: () -> Unit,
    tokenManager: TokenManager,
    signout: () -> Unit = {}
) {
    val token by tokenManager.tokenFlow.collectAsState(initial = null)
    TopAppBar(
        title = {
            Text(
                text = currentScreen.title,
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(
                    onClick = onNavigateUpClicked
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.btn_try_again)
                    )
                }
            }
        },
        actions = {
            Box(modifier = Modifier.padding(10.dp).clickable { signout() }) {
                if (token != null) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                        contentDescription = "Выход",
                        tint = MaterialTheme.colorScheme.primary // Установите цвет по вашему выбору
                    )
                }
            }
        }

    )
}