package com.example.socialmediaapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.socialmediaapp.Screen
import com.example.socialmediaapp.ui.viewmodels.AuthState
import com.example.socialmediaapp.ui.viewmodels.AuthViewModel
import com.example.socialmediaapp.ui.viewmodels.FirebaseViewModel
import com.example.socialmediaapp.ui.viewmodels.FirestoreViewModel
import com.example.socialmediaapp.ui.viewmodels.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun HomeScreen(
    firebaseViewModel: FirebaseViewModel,
    navHostController: NavHostController,
    postViewModel: PostViewModel,
    firestoreViewModel: FirestoreViewModel,
    authViewModel: AuthViewModel
) {

    // scroll behavior for the top app bar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // observe the auth state
    val authState = firebaseViewModel.authState.observeAsState()
    val context = LocalContext.current


    // Based on the auth state value, navigate to the login screen or show an error message
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navHostController.navigate(Screen.LoginScreen) // navigate to login
            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier,

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    Color(0xFFFFFFFF), Color(0xFFFFFFFF), Color(
                        0xFFFFFFFF
                    )
                ),

                navigationIcon = {
                    IconButton(
                        onClick = {
                            firebaseViewModel.signOut()
                                  },
                        modifier = Modifier
                            .size(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications, // Icon için uygun bir image vector seçin
                            contentDescription = "Logout",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },

                title = {
                    Text(
                        text = "Connected",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier,

                        textAlign = TextAlign.Center
                    )
                },
                actions = {

                    IconButton(
                        onClick = { firebaseViewModel.signOut() },
                        modifier = Modifier
                            .size(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications, // Icon için uygun bir image vector seçin
                            contentDescription = "Logout",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,



                )

        },// Top bar
        bottomBar = {
            BottomBarComponent(navController =navHostController)

        },// Bottom bar

    ) { innerPadding ->

        HomeScreenBodyContent(
            Modifier.padding(innerPadding),
            postViewModel = postViewModel,
            firestoreViewModel = firestoreViewModel,
            authViewModel = authViewModel
        )
        //PostListScreen( modifier = Modifier.padding(innerPadding),postViewModel = postViewModel)

    }
}


@Composable
fun HomeScreenBodyContent(modifier: Modifier = Modifier,postViewModel: PostViewModel,firestoreViewModel: FirestoreViewModel,authViewModel: AuthViewModel) {

    // Observe the posts from the postViewModel
    val posts by postViewModel.posts.collectAsState()



    Surface (
        color = Color(0xFFFFFFFF),
        modifier = modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 0.dp, bottom = 0.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {

            items(posts.size) { index ->
                Post(
                    post = posts[index],
                    postViewModel = postViewModel,
                    firestoreViewModel = firestoreViewModel,
                    authViewModel = authViewModel
                )
            }

        }
    }
}
