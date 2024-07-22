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