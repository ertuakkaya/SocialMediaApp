package com.example.socialmediaapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.ui.screens.LoginScreen
import com.example.socialmediaapp.ui.screens.SignupScreen
import kotlinx.serialization.Serializable

@Composable
fun AppNavHost(modifier: Modifier) {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen
    ) {
        composable<Screen.LoginScreen> {
            LoginScreen(navController = navController)
        }

        composable<Screen.SignupScreen> {
            SignupScreen()
        }
    }
}

/*
@Serializable
object LoginScreen


@Serializable
object SignupScreen


@Serializable
data class ScreenB(
    val name: String?,
    val age: Int
)
 */

sealed class Screen {
    @Serializable
    object LoginScreen : Screen()

    @Serializable
    object SignupScreen : Screen()
}