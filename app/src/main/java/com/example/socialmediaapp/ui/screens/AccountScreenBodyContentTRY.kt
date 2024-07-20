package com.example.socialmediaapp.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.ui.viewmodels.FirebaseViewModel
import com.example.socialmediaapp.ui.viewmodels.FirestoreViewModel
import com.example.socialmediaapp.ui.viewmodels.UserViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun AccountScreenBodyContentTRY(
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

//    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia(),
//        onResult = { uri ->
//            selectedImageUri = uri
//            uri?.let { selectedUri ->
//                coroutineScope.launch {
//                    uploadProfileImage(selectedUri, user, userViewModel, firestoreViewModel)
//                }
//            }
//        }
//    )

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








//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//
//        if (user != null) {
//
//            // Profile Image
//            if (user.profileImageUrl != null) {
//                AccountScreenAvatarImage(user = user)
//            } else {
//                // show add profile image button
//                Button(
//                    onClick = {
//                        //TODO: Add profile
//
//                        singlePhotoPickerLauncher.launch(
//                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                        )
//
//                    },
//                    modifier = Modifier
//                        .size(120.dp)
//                        .clip(CircleShape),
//                ) {
//                    Icon(imageVector = Icons.Default.Add , contentDescription = "Add Profile Image")
//
//                }
//            }
//
//
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Name (if available)
//            user.name?.let { name ->
//                Text(
//                    text = name,
//                    style = MaterialTheme.typography.headlineLarge,
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//
//            // User Name
//            Text(
//                text = "@${user.userName}" ?: "No Username",
//                style = MaterialTheme.typography.bodyMedium,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            ElevatedCard(
//
//                modifier = Modifier
//                    .padding(horizontal = 8.dp)
//                    .height(180.dp)
//                    .fillMaxWidth()
//
//            ) {
//                // Card Content
//                Column {
//                    // Photos Text
//                    Text(
//                        text = "Photos: ",
//                        style = MaterialTheme.typography.headlineSmall,
//                        modifier = Modifier.padding(8.dp),
//                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle
//                    )
//
//                }
//            }
//
//
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Email
//            Text(
//                text = user.email,
//                style = MaterialTheme.typography.bodyLarge
//            )
//
//            // profile image url
//            Text(
//                text = userProfileImageUrl ?: "No Profile Image",
//                style = MaterialTheme.typography.bodyLarge
//            )
//
//            Button(
//                onClick = { },)
//            {
//
//            }
//
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//
//
//
//
//            // Sign Out Button
//            Button(
//                onClick = onSignOut,
//                modifier = Modifier.padding(top = 16.dp)
//            ) {
//                Text("Sign Out")
//            }
//        } else {
//
//            // User data not available
//
//            // Show loading or error state
//            CircularProgressIndicator()
//        }
//    }

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
                    .size(120.dp)
                    .clip(CircleShape)
            ) {
                if (isUploading) {
                    CircularProgressIndicator( ///////// Oploading Indicator
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (currentUser?.profileImageUrl != null) {
                    AsyncImage(
                        model = currentUser?.profileImageUrl,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
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
            }

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

            // profile image url
            Text(
                text = userProfileImageUrl ?: "No Profile Image",
                style = MaterialTheme.typography.bodyLarge
            )

        } else {
            // User data not available
            CircularProgressIndicator()
        }
    }

}


@Composable
fun LoadingScreen()
{

}

@Composable
fun AccountScreenAvatarImage(user: User)
{
    AsyncImage(
        model = user.profileImageUrl,
        contentDescription = "Profile Picture",
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}



suspend fun uploadProfileImage(
    uri: Uri,
    user: User?,
    userViewModel: UserViewModel,
    firestoreViewModel: FirestoreViewModel
): User? {
    val filename = UUID.randomUUID().toString()
    Log.d("AccountScreenBodyContent", "Selected Image URI: $uri")

    val imageUrl = userViewModel.uploadProfilePicture(
        uri,
        filename,
        "user_profile_images"
    )
    Log.d("AccountScreenBodyContent imageUrl", ": $imageUrl")

    return user?.let { nonNullUser ->
        val updatedUser = nonNullUser.copy(profileImageUrl = imageUrl.toString())
        firestoreViewModel.updateUserInFirestore(updatedUser.userID!!, updatedUser)
        Log.d("AccountScreen Profile Updated", "AccountScreenBodyContent: $updatedUser")
        updatedUser
    }
}