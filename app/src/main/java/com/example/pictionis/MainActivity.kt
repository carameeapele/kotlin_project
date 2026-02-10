package com.example.pictionis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.authentication.navigation.AUTH_ROUTE
import com.example.authentication.navigation.authenticationNavGraph
import com.example.designsystem.theme.PictionisTheme
import com.example.game.navigation.GAME_ROUTE
import com.example.game.navigation.gameNavGraph
import com.example.lobby.navigation.MAIN_ROUTE
import com.example.lobby.navigation.mainNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PictionisTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PictionisApp()
                }
            }
        }
    }
}

@Composable
fun PictionisApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AUTH_ROUTE
    ) {
        authenticationNavGraph(
            navController = navController,
            onNavigateToLobby = {
                navController.navigate(MAIN_ROUTE) {
                    popUpTo(AUTH_ROUTE) { inclusive = true }
                }
            }
        )

        mainNavGraph(
            navController = navController,
            onNavigateToGame = { gameId ->
                navController.navigate("$GAME_ROUTE/$gameId")
            },
            onLogout = {
                navController.navigate(AUTH_ROUTE) {
                    popUpTo(0) { inclusive = true }
                }
            }
        )

        gameNavGraph(
            navController = navController,
            onNavigateBack = {
                navController.popBackStack()
            },
            onGameEnd = {
                navController.navigate(MAIN_ROUTE) {
                    popUpTo(GAME_ROUTE) { inclusive = true }
                }
            }
        )
    }
}