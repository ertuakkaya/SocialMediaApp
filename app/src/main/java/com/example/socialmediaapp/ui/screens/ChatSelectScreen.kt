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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.socialmediaapp.Screen

@Composable
fun ChatSelectScreen(
    viewModel: ChatSelectViewModel,
    navController: NavController
) {


    LaunchedEffect (Unit){
        viewModel.getUsers()
    }
    val users by viewModel.users.collectAsState()

    val userList = users



    Log.d("ChatSelectScreen", "Users: $users")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 36.dp, horizontal = 16.dp),

    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Select a user to chat with",
                fontSize = 24.sp,
            )


            LazyColumn (
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                items(users) { user ->
                    UserCardComponent(user = user, onClick = {
                        // Navigate to chat screen
                        Log.d("ChatSelectScreen Onclick", "User: $user")
                        user.userID?.let { // TODO: Handle null
                            navController.navigate(Screen.ChatScreen(user.userID!!))
                        }
                    })
                }

            }
        }
    }




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