package com.example.socialmediaapp.ui.screens

import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.socialmediaapp.Screen
import com.example.socialmediaapp.viewmodels.AuthState
import com.example.socialmediaapp.viewmodels.FirebaseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun HomeScreen(firebaseViewModel: FirebaseViewModel,navHostController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

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
                            navHostController.navigate(Screen.LoginScreen)
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
            NavigationBar(
                modifier = Modifier
                    .height(56.dp),
                containerColor = Color(0xFFFFFFFF),
                tonalElevation = 4.dp,
                contentColor = Color(0xFF000000)


            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        /*TODO*/
                    },
                    modifier = Modifier
                        .clip(CircleShape)
                        .fillMaxSize(),

                    icon = {
                        Icon(
                            Icons.Filled.Home,
                            contentDescription = null
                        )
                    },

                    //colors = NavigationBarItemDefaults.colors(Color(0xFF5871B4), Color(0xFF000000), Color(0xFFAD5454)


                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /*TODO*/ },
                    icon = {
                        Icon(
                            Icons.Rounded.Call,
                            contentDescription = null
                        )
                    },
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        /*TODO*/
                    },
                    icon = {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = null
                        )
                    },
                )

            }

        },// Bottom bar

    ) { innerPadding ->

        HomeScreenBodyContent(Modifier.padding(innerPadding))

    }
}
