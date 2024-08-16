package com.example.socialmediaapp.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.ui.viewmodels.ChatSelectViewModel

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.socialmediaapp.R
import com.example.socialmediaapp.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatSelectScreen(
    viewModel: ChatSelectViewModel,
    navController: NavController,
) {



    LaunchedEffect (Unit){
        viewModel.getUsers()
    }
    val users by viewModel.users.collectAsState()

    val userList = users

    Scaffold (
        //snackbarHost = { SnackbarHost(hostState = snackbarHostState) },

        topBar = {
            TopAppBar(
                modifier = Modifier,

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    Color(0xFFFFFFFF), Color(0xFFFFFFFF), Color(0xFF000000)
                ),

                title = {
                    Text(
                        text = "Select a user to chat with",
                        //fontSize = 32.sp,
                        //fontWeight = FontWeight.Bold,
                        modifier = Modifier,
                        //textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // Navigate back
                            navController.navigateUp()
                        },
                        modifier = Modifier.size(50.dp)
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.back_arrow),
                            contentDescription = "",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                //scrollBehavior = scrollBehavior,




            )

        },// Top bar
        bottomBar = {
            BottomBarComponent(navController = navController)
        }
    ) { innerPadding ->
        // inner Box
        Box(
            modifier = Modifier
                //.fillMaxWidth()
                .padding(innerPadding),
            )
        {
            Box(modifier = Modifier.padding(start = 16.dp, end = 16.dp)) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {



                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(users) { user ->
                            UserCardComponent(user = user, onClick = {
                                // Navigate to chat screen
                                Log.d("ChatSelectScreen Onclick", "User: $user")
                                user.userID?.let {
                                    navController.navigate(Screen.ChatScreen(user.userID!!))
                                }
                            })
                        }

                    }
                }
            }
        }

    }

    Log.d("ChatSelectScreen", "Users: $users")

}


@Composable
fun UserCardComponent(
    user : User,
    onClick : () -> Unit = { }

) {



    Log.d("UserCardComponent", "User: $user")

    user?.let {
        // Profile pic , username ,

        ElevatedCard (
            elevation = CardDefaults.cardElevation(8.dp),
            modifier = Modifier.clickable {
                // Navigate to chat screen
                onClick()
            }
        ){
            Row(
                modifier = Modifier

                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){

                // Profile pic
                user.profileImageUrl?.let {
                    ProfileImage(profileImageUrl = user.profileImageUrl!!)
                }


                Spacer(modifier = Modifier.width(16.dp))

                //Text(text = username)
                Text(text = user.userName!!)


            }
        }

    }



}