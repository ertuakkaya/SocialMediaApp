package com.example.socialmediaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.socialmediaapp.ui.theme.SocialMediaAppTheme
import com.example.socialmediaapp.ui.viewmodels.AuthViewModel
import com.example.socialmediaapp.ui.viewmodels.FirebaseStorageViewModel
import com.example.socialmediaapp.ui.viewmodels.FirebaseViewModel
import com.example.socialmediaapp.ui.viewmodels.FirestoreViewModel
import com.example.socialmediaapp.ui.viewmodels.MakeAPostViewModel
import com.example.socialmediaapp.ui.viewmodels.PostViewModel
import com.example.socialmediaapp.ui.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val firebaseViewModel: FirebaseViewModel by viewModels()
    val firestoreViewModel: FirestoreViewModel by viewModels()
    val authViewModel: AuthViewModel by viewModels()
    val postViewModel: PostViewModel by viewModels()
    val firebaseStorageViewModel: FirebaseStorageViewModel by viewModels()
    val makeAPostViewModel: MakeAPostViewModel by viewModels()
    val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SocialMediaAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(
                        modifier = Modifier.padding(innerPadding),
                        firebaseViewModel = firebaseViewModel,
                        authViewModel = authViewModel,
                        firestoreViewModel = firestoreViewModel,
                        postViewModel = postViewModel,
                        firebaseStorageViewModel = firebaseStorageViewModel,
                        makeAPostViewModel = makeAPostViewModel,
                        userViewModel = userViewModel
                    )
                }
            }
        }
    }
}

