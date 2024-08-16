package com.example.socialmediaapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.ui.screens.AccountScreen
import com.example.socialmediaapp.ui.screens.ChatScreen
import com.example.socialmediaapp.ui.screens.ChatSelectScreen
import com.example.socialmediaapp.ui.viewmodels.ChatViewModel
import com.example.socialmediaapp.ui.screens.HomeScreen
import com.example.socialmediaapp.ui.screens.LoadingScreen
import com.example.socialmediaapp.ui.screens.LoginScreen
import com.example.socialmediaapp.ui.screens.MakeAPostScreen
import com.example.socialmediaapp.ui.screens.SignupScreen
import com.example.socialmediaapp.ui.viewmodels.AuthViewModel
import com.example.socialmediaapp.ui.viewmodels.ChatSelectViewModel
import com.example.socialmediaapp.ui.viewmodels.FirebaseStorageViewModel
import com.example.socialmediaapp.ui.viewmodels.FirebaseViewModel
import com.example.socialmediaapp.ui.viewmodels.FirestoreViewModel
import com.example.socialmediaapp.ui.viewmodels.MakeAPostViewModel
import com.example.socialmediaapp.ui.viewmodels.PostViewModel
import com.example.socialmediaapp.ui.viewmodels.UserViewModel
import kotlinx.serialization.Serializable

@Composable
fun AppNavHost(
    modifier: Modifier,
    firebaseViewModel: FirebaseViewModel ,
    authViewModel: AuthViewModel,
    firestoreViewModel: FirestoreViewModel,
    postViewModel: PostViewModel,
    firebaseStorageViewModel: FirebaseStorageViewModel,
    makeAPostViewModel: MakeAPostViewModel,
    userViewModel: UserViewModel,
    chatViewModel: ChatViewModel,
    chatSelectViewModel: ChatSelectViewModel,


) {


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
            HomeScreen(
                firebaseViewModel = firebaseViewModel,
                navHostController = navController,
                postViewModel = postViewModel,
                firestoreViewModel = firestoreViewModel,
                authViewModel = authViewModel
            )
        }
        composable<Screen.AccountScreen> {
            AccountScreen(
                firebaseViewModel = firebaseViewModel,
                navHostController = navController,
                user = User("", ""),
                authViewModel = authViewModel,
                firestoreViewModel = firestoreViewModel,
                userViewModel = userViewModel,
            )
        }
        composable<Screen.MakeAPostScreen> {
            MakeAPostScreen(
                navController = navController,
                firebaseStorageViewModel = firebaseStorageViewModel,
                makeAPostViewModel = makeAPostViewModel,
                postViewModel = postViewModel,
                authViewModel = authViewModel,
                firestoreViewModel = firestoreViewModel
            )
        }

        composable<Screen.LoadingScreen> {
            LoadingScreen()
        }

        composable<Screen.ChatScreen> {
            val dash = it.toRoute<Screen.ChatScreen>()
            ChatScreen(
                viewModel = chatViewModel,
                dash.partnerUserId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable<Screen.ChatSelectScreen> {
            ChatSelectScreen(
                viewModel = chatSelectViewModel,
                navController = navController,
            )

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

    @Serializable
    object MakeAPostScreen : Screen()

    @Serializable
    object LoadingScreen : Screen()

    @Serializable
    data class ChatScreen(
        val partnerUserId: String
    ) : Screen()

    @Serializable
    object ChatSelectScreen : Screen()


}

