package com.example.socialmediaapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.socialmediaapp.ui.screens.AccountScreen
import com.example.socialmediaapp.ui.screens.HomeScreen
import com.example.socialmediaapp.ui.screens.LoginScreen
import com.example.socialmediaapp.ui.screens.SignupScreen
import com.example.socialmediaapp.ui.viewmodels.FirebaseViewModel
import kotlinx.serialization.Serializable

@Composable
fun AppNavHost(modifier: Modifier,firebaseViewModel: FirebaseViewModel) {

    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen
    ) {
        composable<Screen.LoginScreen> {
            LoginScreen(navController = navController,firebaseViewModel = firebaseViewModel)
        }

        composable<Screen.SignupScreen> {
            SignupScreen(navController = navController,firebaseViewModel = firebaseViewModel)
        }
        composable<Screen.HomeScreen> {
            HomeScreen(firebaseViewModel = firebaseViewModel,navHostController = navController)
        }
        composable<Screen.AccountScreen> {
            AccountScreen(firebaseViewModel = firebaseViewModel,navHostController = navController)
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

    @Serializable
    object HomeScreen : Screen()

    @Serializable
    object AccountScreen : Screen()
}