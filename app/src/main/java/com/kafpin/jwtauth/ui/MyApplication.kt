package com.kafpin.jwtauth.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kafpin.jwtauth.data.dataStore.RoleManager
import com.kafpin.jwtauth.data.dataStore.StockInfoManager
import com.kafpin.jwtauth.data.dataStore.TokenManager
import com.kafpin.jwtauth.ui.components.Header
import kotlinx.coroutines.launch

@Composable
fun MyApplication(
    tokenManager: TokenManager,
    roleManager: RoleManager,
    stockInfoManager: StockInfoManager,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        AppDestinations.fromRoute(backStackEntry?.destination?.route) ?: AppDestinations.Home

    val canNavigateBack = navController.previousBackStackEntry != null

    fun signOut() {
        coroutineScope.launch {
            tokenManager.clearToken()
            roleManager.clearRole()
            stockInfoManager.clearStockInfo()
            navController.navigate(AppDestinations.SignIn.route) {
                popUpTo(AppDestinations.Home.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    Scaffold(
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