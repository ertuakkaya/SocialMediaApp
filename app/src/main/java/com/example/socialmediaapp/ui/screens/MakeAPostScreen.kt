package com.example.socialmediaapp.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.socialmediaapp.R
import com.example.socialmediaapp.ui.viewmodels.AuthViewModel
import com.example.socialmediaapp.ui.viewmodels.FirebaseStorageViewModel
import com.example.socialmediaapp.ui.viewmodels.FirestoreViewModel
import com.example.socialmediaapp.ui.viewmodels.MakeAPostViewModel
import com.example.socialmediaapp.ui.viewmodels.PostViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MakeAPostScreen(
    navController: NavController,
    firebaseStorageViewModel: FirebaseStorageViewModel,
    makeAPostViewModel: MakeAPostViewModel,
    postViewModel: PostViewModel,
    authViewModel: AuthViewModel,
    firestoreViewModel: FirestoreViewModel
) {

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier,

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    Color(0xFFFFFFFF), Color(0xFFFFFFFF), Color(0xFF000000)
                ),

                title = {
                    Text(
                        text = "Make a post",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier,

                        textAlign = TextAlign.Center
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
    ){innerPadding ->
        MakeAPostBody(
            Modifier.padding(innerPadding),
            firebaseStorageViewModel = firebaseStorageViewModel,
            makeAPostViewModel = makeAPostViewModel,
            postViewModel = postViewModel,
            firestoreViewModel = firestoreViewModel,
            authViewModel = authViewModel

        )
    }
}