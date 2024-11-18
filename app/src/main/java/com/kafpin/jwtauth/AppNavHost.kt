package com.kafpin.jwtauth

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kafpin.jwtauth.data.RoleManager
import com.kafpin.jwtauth.data.StockInfoManager
import com.kafpin.jwtauth.data.TokenManager
import com.kafpin.jwtauth.ui.screens.InvoiceDetailsScreen
import com.kafpin.jwtauth.ui.screens.LoginScreen
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen
import com.kafpin.jwtauth.ui.screens.ShippingListScreen
import com.kafpin.jwtauth.ui.screens.StockSearchScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    tokenManager: TokenManager,
    roleManager: RoleManager,
    stockInfoManager: StockInfoManager
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val token = tokenManager.getToken()
            if (token != null) {
                navController.navigate(AppDestinations.Home.route) {
                    popUpTo(AppDestinations.SignIn.route) { inclusive = true }
                }
            }
        }
    }

    NavHost(navController, startDestination = AppDestinations.SignIn.route, modifier) {
        composable(AppDestinations.SignIn.route) {
            // TODO need logic to check if user is authorized
            LoginScreen(hiltViewModel(), onSigned = {
                navController.navigate(AppDestinations.Home.route) {
                    popUpTo(AppDestinations.SignIn.route) { inclusive = true }
                }
            })
        }

        composable(AppDestinations.Home.route) {
            ShippingListScreen(viewModel = hiltViewModel(), onStockNav = {
                navController.navigate(AppDestinations.StockSelect.route)
            }, onSelectShipping = { id ->
                navController.navigate(
                    AppDestinations.ShippingDetails(id).route
                )
            },
                roleManager = roleManager,
                stockInfoManager = stockInfoManager
            )
        }

        composable(AppDestinations.StockSelect.route) {
            StockSearchScreen(backNav = {
                navController.navigate(AppDestinations.Home.route) {
                    popUpTo(AppDestinations.Home.route) { inclusive = true }
                }
            }, stockInfoManager = stockInfoManager)
        }

        composable(AppDestinations.ShippingDetails("{id}").route) {
            ShippingInfoScreen(navController = navController)
        }

        composable(AppDestinations.InvoiceDetails("{id}").route) {
            InvoiceDetailsScreen(navController)
        }
    }
}