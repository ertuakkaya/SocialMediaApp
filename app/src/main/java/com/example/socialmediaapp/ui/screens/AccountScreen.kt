package com.example.socialmediaapp.ui.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.socialmediaapp.Screen
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.ui.viewmodels.AuthState
import com.example.socialmediaapp.ui.viewmodels.AuthViewModel
import com.example.socialmediaapp.ui.viewmodels.FirebaseViewModel
import com.example.socialmediaapp.ui.viewmodels.FirestoreViewModel
import com.example.socialmediaapp.ui.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun AccountScreen(
    user: User,
    firebaseViewModel: FirebaseViewModel,
    navHostController: NavHostController,
    firestoreViewModel: FirestoreViewModel,
    authViewModel: AuthViewModel,
    userViewModel: UserViewModel,
) {


    val authState = firebaseViewModel.authState.observeAsState()
    val context = LocalContext.current


    // Launch effect, bu sayfa açıldığında çalışacak
    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> navHostController.navigate(Screen.LoginScreen)
            is AuthState.Authenticated -> Unit // TODO: navigate to home
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    //val user by firestoreViewModel.getUserFromFirestore(currentUserID).observeAsState(null)




    Scaffold(
        modifier = Modifier,

        bottomBar = {
            BottomBarComponent(navHostController = navHostController)

        },// Bottom bar

    ) { innerPadding ->



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
                userData != null -> AccountScreenBodyContent(
                userData!!,
                Modifier.padding(innerPadding),
                onSignOut = { firebaseViewModel.signOut() },
                firestoreViewModel = firestoreViewModel,
                firebaseViewModel = firebaseViewModel,
                userViewModel = userViewModel
            )
            else -> Text("No user data available")
        }


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
        Text(text = "Name: ${user.name ?: "N/A"}")
        Text(text = "Profile Image URL: ${user.profileImageUrl ?: "N/A"}")
        Text(text = "Bio: ${user.userID ?: "N/A"}")
        // You can add AsyncImage for profile picture if needed
    }
}


@Composable
fun AccountScreenBodyContent(
    user: User?,
    modifier: Modifier,
    onSignOut: () -> Unit,
    firestoreViewModel : FirestoreViewModel,
    firebaseViewModel: FirebaseViewModel,
    userViewModel: UserViewModel




){


    var currentUser by remember {
        mutableStateOf(user)
    }

    var isUploading by remember {
        mutableStateOf(false)
    }

    var userProfileImageUrl by remember {
        mutableStateOf<String>(user?.profileImageUrl ?: "")
    }

    /// Image Picker
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val coroutineScope = rememberCoroutineScope()



    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let { selectedUri ->
                isUploading = true
                coroutineScope.launch {
                    val updatedUser = uploadProfileImage(selectedUri, currentUser, userViewModel, firestoreViewModel)
                    currentUser = updatedUser
                    isUploading = false
                }
            }
        }
    )










    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (currentUser != null) {
            // Profile Image
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
            ) {
                if (isUploading) {
                    CircularProgressIndicator( ///////// Oploading Indicator
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (currentUser?.profileImageUrl != null) {

                    ProfileImageComponent(user = currentUser!!)
                } else {
                    Button(
                        onClick = {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Profile Image")
                    }
                }
            } // Profile Image Box


            Spacer(modifier = Modifier.height(16.dp))

            // Name (if available)
            currentUser?.name?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // User Name
            Text(
                text = "@${currentUser?.userName}" ?: "No Username",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            DividerComponent()

            // Photos Card
            ElevatedCard(

                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(180.dp)
                    .fillMaxWidth()

            ) {
                // Card Content
                Column {
                    // Photos Text
                    Text(
                        text = "Photos: ",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(8.dp),
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                    )

                }
            }



            Spacer(modifier = Modifier.height(8.dp))

            // Email
            Text(
                text = user?.email ?: "Email N/A",
                style = MaterialTheme.typography.bodyLarge
            )


        } else {
            // User data not available
            CircularProgressIndicator()

        }

        DividerComponent()

        // Sign Out Button
        SignOutButton(
            onItemClick = { firebaseViewModel.signOut() }
        )




    }// Account Screen Column

}


@Composable
fun DividerComponent()
{
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 16.dp),
        thickness = 1.dp,
        color = Color.Gray
    )

}

@Composable
fun SignOutButton( onItemClick : () -> Unit){
    ElevatedButton(
        onClick = { onItemClick()  },
        shape = MaterialTheme.shapes.medium,
    ) {
        Text("Sign Out")
    }
}

/*
@Composable
fun AccountScreenBodyContentOld(
    user: User?,
    modifier: Modifier,

    onSignOut: () -> Unit,
    firestoreViewModel : FirestoreViewModel,
    firebaseViewModel: FirebaseViewModel,
    userViewModel: UserViewModel




    ){


    var userProfileImageUrl by remember {
        mutableStateOf<String>(user?.profileImageUrl ?: "")
    }

    /// Image Picker
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
        }
    )

    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        if (user != null) {
            // Profile Image
            if (user.profileImageUrl != null) {
                AsyncImage(
                    model = user.profileImageUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

            } else {
                // show add profile image button
                Button(
                    onClick = {
                        //TODO: Add profile

                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )

                        val filename = UUID.randomUUID()
                        selectedImageUri?.let { uri ->
                            Log.d("AccountScreenBodyContent", "Selected Image URI: $uri")

                            //gs://socialmediaapp-76cee.appspot.com/user_profile_images
                            coroutineScope.launch {
                                val imageUrl = userViewModel.uploadProfilePicture(
                                    uri,
                                    filename.toString(),
                                    "user_profile_images"
                                )
                                Log.d("AccountScreenBodyContent imageUrl", ": $imageUrl")
                                user.profileImageUrl = imageUrl.toString()

                                //////////////////
                                firestoreViewModel.updateUserInFirestore(user.userID!!, user)

                                Log.d(
                                    "AccountScreen Profile Updated ",
                                    "AccountScreenBodyContent: $user"
                                )


                            }


                        }

                        ////




                    },
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                ) {
                    Icon(imageVector = Icons.Default.Add , contentDescription = "Add Profile Image")
                }
            }



            Spacer(modifier = Modifier.height(16.dp))

            // Name (if available)
            user.name?.let { name ->
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineLarge,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // User Name
            Text(
                text = "@${user.userName}" ?: "No Username",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))


            // Photos Card
            ElevatedCard(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(180.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)

            ) {
                // Card Content
                Column {
                    // Photos Text
                   Text(
                       text = "Photos: ",
                       style = MaterialTheme.typography.headlineSmall,
                       modifier = Modifier.padding(8.dp),
                       fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
                   )

                }
            }



            Spacer(modifier = Modifier.height(8.dp))

            // Email
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyLarge
            )

            // profile image url
            Text(
                text = userProfileImageUrl ?: "No Profile Image",
                style = MaterialTheme.typography.bodyLarge
            )



            Spacer(modifier = Modifier.height(8.dp))



            // Sign Out Button
            Button(
                onClick = onSignOut,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Sign Out")
            }
        } else {

            // User data not available

            // Show loading or error state
            CircularProgressIndicator()
        }
    }



}

 */

@Composable
fun ProfileImageComponent(user : User){
    AsyncImage(
        model = user.profileImageUrl,
        contentDescription = "Profile Picture",
        modifier = Modifier
            .size(150.dp)
            .border(2.dp, Color.LightGray, CircleShape)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}








