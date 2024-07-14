package com.example.socialmediaapp.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.socialmediaapp.Screen
import com.example.socialmediaapp.ui.viewmodels.FirebaseViewModel
import com.example.socialmediaapp.ui.viewmodels.ProfileViewModel

@Composable
fun AccountScreen(firebaseViewModel: FirebaseViewModel, navHostController: NavHostController){

    Scaffold(
        modifier = Modifier,

        bottomBar = {
            BottomBarComponent(navHostController = navHostController)

        },// Bottom bar

    ) { innerPadding ->

        AccountScreenBodyContent(Modifier.padding(innerPadding), firebaseViewModel = firebaseViewModel, profileViewModel = ProfileViewModel())

    }

}

@Composable
fun AccountScreenBodyContent(padding: Modifier, firebaseViewModel: FirebaseViewModel, profileViewModel: ProfileViewModel) {

    /*
    var userName by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val userProfile = firebaseViewModel.userProfile.collectAsState()




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        userProfile?.let { profile ->
            AsyncImage(
                model = profile.value?.photoUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Name: ${profile.value?.name ?: "N/A"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Email: ${profile.value?.email ?: "N/A"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Provider: ${profile.value?.providerId}")
        } ?: Text("User profile not available")
    }
    */



    /*
    var userName by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    Column {
        TextField(value = userName, onValueChange = { userName = it }, label = { Text("Kullanıcı Adı") })
        Button(onClick = {
            // Resim seçici açılacak
        }) {
            Text("Profil Resmi Seç")
        }
        Button(onClick = {
            profileViewModel.updateProfile(userName, imageUri)
        }) {
            Text("Profili Güncelle")
        }
    }
    */


}


