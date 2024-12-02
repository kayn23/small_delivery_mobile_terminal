package com.kafpin.jwtauth.ui

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
import com.kafpin.jwtauth.data.dataStore.Role
import com.kafpin.jwtauth.data.dataStore.RoleManager
import com.kafpin.jwtauth.data.dataStore.TokenManager
import com.kafpin.jwtauth.ui.screens.CreateShippingScreen.CreateShippingScreen
import com.kafpin.jwtauth.ui.screens.LoginScreen.LoginScreen
import com.kafpin.jwtauth.ui.screens.ShippingInfoScreen.ShippingInfoScreen
import com.kafpin.jwtauth.ui.screens.ShippingListScreen.ShippingListScreen
import com.kafpin.jwtauth.ui.screens.StockSearchScreen.StockSearchScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    tokenManager: TokenManager,
    roleManager: RoleManager,
) {
    val coroutineScope = rememberCoroutineScope()
    val role by roleManager.roleFlow.collectAsState(null)

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
            LoginScreen(hiltViewModel(), onSigned = {
                navController.navigate(AppDestinations.Home.route) {
                    popUpTo(AppDestinations.SignIn.route) { inclusive = true }
                }
            })
        }

        composable(AppDestinations.Home.route) {
            when (role) {
                Role.Client -> {}
                Role.Admin -> {
                    StockSearchScreen()
                }

                Role.Courier -> {
                    ShippingListScreen(
                        viewModel = hiltViewModel(),
                        onSelectShipping = { id ->
                            navController.navigate(
                                AppDestinations.ShippingDetails(id).route
                            )
                        },
                        onCreateShippingClick = {
                            navController.navigate(AppDestinations.CreateShipping.route)
                        },
                    )
                }

                null -> {}
            }

        }

        composable(AppDestinations.CreateShipping.route) {
            CreateShippingScreen(roleManager, onCreated = { shipping ->
                navController.navigate(AppDestinations.ShippingDetails(shipping.id.toString()).route) {
                    popUpTo(AppDestinations.CreateShipping.route) { inclusive = true }
                }
            })
        }

        composable(AppDestinations.ShippingDetails("{id}").route) {
            ShippingInfoScreen(navController = navController, onDeleted = {
                navController.navigate(AppDestinations.Home.route) {
                    popUpTo(AppDestinations.Home.route) { inclusive = true }
                }
            })
        }
    }
}