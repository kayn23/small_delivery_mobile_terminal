package com.kafpin.jwtauth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kafpin.jwtauth.data.TokenManager
import com.kafpin.jwtauth.ui.screens.HomeScreen
import com.kafpin.jwtauth.ui.screens.InvoiceDetailsScreen
import com.kafpin.jwtauth.ui.screens.LoginScreen
import com.kafpin.jwtauth.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    tokenManager: TokenManager
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
            HomeScreen(viewModel = hiltViewModel(), onClick = {id -> navController.navigate(
                AppDestinations.InvoiceDetails(id).route)})
        }

        composable(AppDestinations.InvoiceDetails("{id}").route) {
            InvoiceDetailsScreen(navController)
        }
    }
}