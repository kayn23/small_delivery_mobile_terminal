package com.kafpin.jwtauth.ui

sealed class AppDestinations(val title: String, val route: String) {
    object SignIn: AppDestinations("Sign in", "signin")
    object Home: AppDestinations("Account home", "account_home")
    object StockSelect: AppDestinations("Stock select", "stock_select")
    object CreateShipping: AppDestinations("Shipping create", "shipping_create")
    data class ShippingDetails(val id: String): AppDestinations("Shipping details", "shipping/$id")

    companion object {
        fun fromRoute(route: String?): AppDestinations? {
            return when {
                route == SignIn.route -> SignIn
                route == Home.route -> Home
                route == StockSelect.route -> StockSelect
                route == CreateShipping.route -> CreateShipping
                route?.startsWith("shipping/") == true -> {
                    val id = route.substringAfter("shipping/")
                    ShippingDetails(id)
                }
                else -> null
            }
        }
    }
}