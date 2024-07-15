package com.example.socialmediaapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.ui.viewmodels.AuthViewModel
import com.example.socialmediaapp.ui.viewmodels.FirebaseViewModel
import com.example.socialmediaapp.ui.viewmodels.FirestoreViewModel

@Composable
fun AccountScreen(user: User,firebaseViewModel: FirebaseViewModel, navHostController: NavHostController , firestoreViewModel: FirestoreViewModel , authViewModel: AuthViewModel){


    //val user by firestoreViewModel.getUserFromFirestore(currentUserID).observeAsState(null)




//    Scaffold(
//        modifier = Modifier,
//
//        bottomBar = {
//            BottomBarComponent(navHostController = navHostController)
//
//        },// Bottom bar
//
//    ) { innerPadding ->
//
//
//        //val user = firestoreViewModel.getUserFromFirestore(firebaseViewModel.getCurrentUser()?.uid ?: "")
//
//        AccountScreenBodyContent(
//            modifier = Modifier.padding(innerPadding),
//            user = firestoreViewModel.ge,
//            onSignOut = { firebaseViewModel.signOut() }
//        )



//    }

    val currentUserID = firebaseViewModel.getCurrentUser()?.uid ?: ""

    val userData by firestoreViewModel.userData.collectAsState()
    val isLoading by firestoreViewModel.isLoading.collectAsState()
    val error by firestoreViewModel.error.collectAsState()

    LaunchedEffect(currentUserID) {
        firestoreViewModel.getUserFromFirestore(currentUserID)
    }

    when {
        isLoading -> LoadingIndicator()
        error != null -> ErrorMessage(error!!)
        userData != null -> UserProfile(userData!!)
        else -> Text("No user data available")
    }

}




@Composable
fun LoadingIndicator() {
    CircularProgressIndicator()
}

@Composable
fun ErrorMessage(error: String) {
    Text(text = error, color = Color.Red)
}

@Composable
fun UserProfile(user: User) {
    Column {
        Text("Username: ${user.userName ?: "N/A"}")
        Text("Email: ${user.email}")
        Text("Name: ${user.name ?: "N/A"}")
        // You can add AsyncImage for profile picture if needed
    }
}




@Composable
fun AccountScreenBodyContent(
    modifier: Modifier,
    user: User?,
    onSignOut: () -> Unit,
    firestoreViewModel : FirestoreViewModel,
    firebaseViewModel: FirebaseViewModel,


    ) {
    /*
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (user != null) {
            // Profile Image
            AsyncImage(
                model = user.profileImageUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            // User Name
            Text(
                text = user.userName ?: "No Username",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Email
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Name (if available)
            user.name?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Sign Out Button
            Button(
                onClick = onSignOut,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Sign Out")
            }
        } else {
            // Show loading or error state
            CircularProgressIndicator()
        }
    }
    */


    val currentUserID = firebaseViewModel.getCurrentUser()?.uid ?: ""

    ///
    val userData by firestoreViewModel.userData.collectAsState()
    val isLoading by firestoreViewModel.isLoading.collectAsState()
    val error by firestoreViewModel.error.collectAsState()
    ///

    LaunchedEffect(currentUserID) {
        firestoreViewModel.getUserFromFirestore(currentUserID)
    }


    when {
        isLoading -> LoadingIndicator()
        error != null -> ErrorMessage(error!!)
        userData != null -> UserProfile(userData!!)
        else -> Text("No user data available")
    }

}




