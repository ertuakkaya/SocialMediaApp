package com.example.socialmediaapp.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.ui.viewmodels.FirebaseViewModel
import com.example.socialmediaapp.ui.viewmodels.FirestoreViewModel
import com.example.socialmediaapp.ui.viewmodels.UserViewModel
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun AccountScreenBodyContentClaude(
    user: User?,
    modifier: Modifier,
    onSignOut: () -> Unit,
    firestoreViewModel: FirestoreViewModel,
    firebaseViewModel: FirebaseViewModel,
    userViewModel: UserViewModel
) {
    var userProfileImageUrl by remember {
        mutableStateOf<String>(user?.profileImageUrl ?: "")
    }

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val coroutineScope = rememberCoroutineScope()

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            uri?.let { selectedUri ->
                coroutineScope.launch {
                    uploadProfileImage(selectedUri, user, userViewModel, firestoreViewModel)
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
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Profile Image")
                }
            }

            // ... (rest of the UI code remains the same)
        } else {
            // User data not available
            CircularProgressIndicator()
        }
    }
}

suspend fun uploadProfileImageClaude(
    uri: Uri,
    user: User?,
    userViewModel: UserViewModel,
    firestoreViewModel: FirestoreViewModel
) {
    val filename = UUID.randomUUID().toString()
    Log.d("AccountScreenBodyContent", "Selected Image URI: $uri")

    val imageUrl = userViewModel.uploadProfilePicture(
        uri,
        filename,
        "user_profile_images"
    )
    Log.d("AccountScreenBodyContent imageUrl", ": $imageUrl")

    user?.let { nonNullUser ->
        nonNullUser.profileImageUrl = imageUrl.toString()
        firestoreViewModel.updateUserInFirestore(nonNullUser.userID!!, nonNullUser)
        Log.d("AccountScreen Profile Updated", "AccountScreenBodyContent: $nonNullUser")
    }
}