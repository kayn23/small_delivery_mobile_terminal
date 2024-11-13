package com.kafpin.jwtauth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kafpin.jwtauth.AppDestinations
import com.kafpin.jwtauth.AppNavHost
import com.kafpin.jwtauth.R
import com.kafpin.jwtauth.data.Role
import com.kafpin.jwtauth.data.RoleManager
import com.kafpin.jwtauth.data.TokenManager
import com.kafpin.jwtauth.ui.screens.components.Header
import kotlinx.coroutines.launch

@Composable
fun MyApplication(
    tokenManager: TokenManager,
    roleManager: RoleManager,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    // Notes: Get the name of the current screen check for null
    val currentScreen =
        AppDestinations.fromRoute(backStackEntry?.destination?.route) ?: AppDestinations.Home

    // Notes: Boolean to check if we can nagigate back. Check stack
    val canNavigateBack = navController.previousBackStackEntry != null

    fun signOut() {
        coroutineScope.launch {
            tokenManager.clearToken()
            navController.navigate(AppDestinations.SignIn.route) {
                popUpTo(AppDestinations.Home.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    val role by roleManager.roleFlow.collectAsState(initial = null)

    Scaffold(
        floatingActionButton = {
            if (role == Role.Admin) {
                FloatingActionButton(
                    onClick = { /* Действие при нажатии */ },
                    containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Image(
                        modifier = Modifier.width(40.dp),
                        painter = painterResource(id = R.drawable.qr_code), // Используем изображение QR-кода
                        contentDescription = "QR Code"
                    )
                }
            }
        },
        topBar = {
            Box() {
                Header(
                    currentScreen = currentScreen,
                    canNavigateBack = canNavigateBack,
                    onNavigateUpClicked = { navController.navigateUp() },
                    tokenManager = tokenManager,
                    signout = {
                        signOut()
                    }
                )

                /* HorizontalDivider(
                    thickness = 2.dp,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )*/
            }
        }
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            color = MaterialTheme.colorScheme.background

        ) {
            Column {
                AppNavHost(
                    navController = navController,
                    modifier = modifier.padding(8.dp),
                    tokenManager,
                    roleManager,
                )
            }
        }
    }
}