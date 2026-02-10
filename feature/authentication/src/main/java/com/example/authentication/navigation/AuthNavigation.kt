package com.example.authentication.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.authentication.screen.AuthenticationRoute

const val AUTH_ROUTE = "authentication"

fun NavGraphBuilder.authenticationNavGraph(
    navController: NavHostController,
    onNavigateToLobby: () -> Unit
) {
    composable(route = AUTH_ROUTE) {
        AuthenticationRoute(navigateToLobby = onNavigateToLobby)
    }
}