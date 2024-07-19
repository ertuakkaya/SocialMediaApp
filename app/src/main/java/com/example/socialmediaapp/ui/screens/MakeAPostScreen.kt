package com.example.socialmediaapp.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.util.UUID


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


@Composable
fun MakeAPostBody(
    modifier: Modifier = Modifier,
    firebaseStorageViewModel: FirebaseStorageViewModel,
    makeAPostViewModel: MakeAPostViewModel,
    postViewModel: PostViewModel,
    authViewModel: AuthViewModel,
    firestoreViewModel: FirestoreViewModel
) {


    val currentUser = authViewModel.getCurrentUser()
    val userId = currentUser?.uid ?: ""


    // firebase storage
    val uploadStatus by firebaseStorageViewModel.uploadStatus.collectAsState()
    var imageUri by remember { mutableStateOf<Uri?>(null) }


    val title = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }

    /// Image Picker
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var selectedImageUris by remember {
        mutableStateOf<List<Uri?>>(emptyList())
    }

    var imageUrl by remember { mutableStateOf(postViewModel) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
        }
    )

    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            selectedImageUris = uris
        }
    )
    //// Image Picker


    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(Color(0xFFFFFFFF)),
            //content = { HorizontalPagerSample( if (selectedImageUris.isNotEmpty()) selectedImageUris else selectedImageUri)}
            content = { HorizontalPagerSample(selectedImageUris.ifEmpty { listOf(selectedImageUri).filterNotNull() }) }

        )

        Button(
            onClick = {

                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Text(text = "Pick Multiple Image")
        }



        uploadStatus?.let { status ->
            when { // TODO : Post Succes or Fail Screen
                status.success -> Log.d(
                    "FirebaseStorageViewModel",
                    "Image uploaded successfully. URL: ${status.imageUrl}"
                )

                else -> Log.d("FirebaseStorageViewModel", "Image upload fail. ")
            }
        }


        // Content TextField
        OutlinedTextField(
            value = content.value,
            onValueChange = { content.value = it },
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            keyboardActions = KeyboardActions(onDone = { /* Handle send post */ })
        )

        val scope = rememberCoroutineScope()


        val userData by firestoreViewModel.userData.collectAsState()
        val isLoading by firestoreViewModel.isLoading.collectAsState()
        val error by firestoreViewModel.error.collectAsState()


        LaunchedEffect(key1 = true) {
            // Assuming you have a way to obtain the current user's ID
            //val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            userId?.let { userId ->
                firestoreViewModel.getUserFromFirestore(userId)
            }
        }

        val localUserData = userData
        if (localUserData != null) {

            Text(text = "User: ${localUserData.name}") // Assuming User has a name field
            Text(text = "User: ${localUserData.userID}") // Assuming User has a name field
            Text(text = "User: ${localUserData.email}") // Assuming User has a name field
        } else {
            Text(text = "No user data available")
        }

        when {
            isLoading -> CircularProgressIndicator()
            error != null -> Text(text = "Error: $error")
            //userData != null -> Text(text = "User: ${userData.name}") // Assuming User has a name field
            else -> Text(text = "No user data available")
        }

        val postID = UUID.randomUUID().toString()








        val coroutineScope = rememberCoroutineScope()


        // Post Button
        selectedImageUri?.let { uri ->
            Button(
                onClick = {
                    val filename = postViewModel.generatePostID()



                    val imageUrl = firebaseStorageViewModel.uploadStatus.value?.imageUrl

                    coroutineScope.launch {
                        val url = postViewModel.uploadFile(uri, filename, "post_images")

                        postViewModel.createPost(
                            com.example.socialmediaapp.data.entitiy.Post(
                            id = postID,
                            userName = localUserData?.userName?: "N/A",
                            profileImageUrl = localUserData?.profileImageUrl?: "N/A",
                            postImageUrl = if (url != null) url.toString() else "N/A", // imageUrl?: "N/A",
                            postText = content.value,
                            timestamp = Timestamp.now(),
                            likeCount = 0,
                            commentCount = 0,
                            likedBy = listOf(),
                            comments = listOf(),
                            userId =  localUserData?.userID?: "N/A"
                        )
                        )

                        postViewModel.loadPosts()
                    }






                }, ////////////
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Post")
            }
        }


    }
}// Body Column
