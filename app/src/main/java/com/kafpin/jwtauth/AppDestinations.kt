package com.kafpin.jwtauth

sealed class AppDestinations(val title: String, val route: String) {
    object SignIn: AppDestinations("Sign in", "signin");
    object Home: AppDestinations("Account home", "account_home");
    data class InvoiceDetails(val id: String): AppDestinations("Invoice details", "invoice/$id");

    companion object {
        fun fromRoute(route: String?): AppDestinations? {
            return when {
                route == SignIn.route -> SignIn
                route == Home.route -> Home
                route?.startsWith("invoice/") == true -> {
                    val id = route.substringAfter("invoice/")
                    InvoiceDetails(id)
                }
                else -> null
            }
        }
    }
}