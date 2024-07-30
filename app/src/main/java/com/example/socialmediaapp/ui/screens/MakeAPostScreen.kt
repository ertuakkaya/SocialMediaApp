package com.example.socialmediaapp.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.socialmediaapp.R
import com.example.socialmediaapp.Screen
import com.example.socialmediaapp.ui.viewmodels.AuthViewModel
import com.example.socialmediaapp.ui.viewmodels.FirebaseStorageViewModel
import com.example.socialmediaapp.ui.viewmodels.FirestoreViewModel
import com.example.socialmediaapp.ui.viewmodels.MakeAPostViewModel
import com.example.socialmediaapp.ui.viewmodels.PostViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.util.UUID
import com.example.socialmediaapp.data.entitiy.Post
import compose.icons.FeatherIcons
import compose.icons.feathericons.Camera
import compose.icons.feathericons.Check
import compose.icons.feathericons.Send
import compose.icons.feathericons.Upload


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MakeAPostScreen(
    navController: NavHostController,
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
        bottomBar = {
            BottomBarComponent(navHostController = navController)
        }
    ){innerPadding ->
        MakeAPostBody(
            Modifier.padding(innerPadding),
            firebaseStorageViewModel = firebaseStorageViewModel,
            makeAPostViewModel = makeAPostViewModel,
            postViewModel = postViewModel,
            firestoreViewModel = firestoreViewModel,
            authViewModel = authViewModel,
            navController = navController

        )
    }
}


@Composable
fun SuccesDialogComponent(
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        title = {
            Text("Post Succesful")
        },
        icon = {
            Icon(imageVector = FeatherIcons.Check, contentDescription = "")
        },

        onDismissRequest = {
           onDismissRequest()
        },

        confirmButton = {

        }
    )
}


@Composable
fun MakeAPostBody(
    modifier: Modifier = Modifier,
    firebaseStorageViewModel: FirebaseStorageViewModel,
    makeAPostViewModel: MakeAPostViewModel,
    postViewModel: PostViewModel,
    authViewModel: AuthViewModel,
    firestoreViewModel: FirestoreViewModel,
    navController: NavHostController
) {


    val currentUser = authViewModel.getCurrentUser()
    val userId = currentUser?.uid ?: ""


    // firebase storage
    val uploadStatus by postViewModel.uploadStatus.collectAsState()
    var imageUri by remember { mutableStateOf<Uri?>(null) }



    uploadStatus?.let { status ->
        Log.d("FirebaseStorageViewModel", "Image upload status: $status")
        when { // TODO : Post Succes or Fail Screen
            status.success -> SuccesDialogComponent(onDismissRequest = {
                navController.navigate(Screen.HomeScreen)
            })
            else -> Log.d("FirebaseStorageViewModel", "Image upload fail. ")
        }
    }



    val title = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }

    /// Image Picker
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var selectedImageUris by remember {
        mutableStateOf<List<Uri?>>(emptyList())
    }

    //var imageUrl by remember { mutableStateOf(postViewModel) }

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
            //.padding(16.dp)
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 0.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Horizontal Pager for multiple images
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .height(500.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(Color(0xFFFFFFFF)),
            content = { HorizontalPagerSample(selectedImageUris.ifEmpty { listOf(selectedImageUri).filterNotNull() }) },
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)

        )


        // Button to pick multiple images
        IconButton(
            onClick = {

                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .shadow(4.dp, shape = RoundedCornerShape(16.dp)),
            colors = IconButtonDefaults
                .iconButtonColors(
                    containerColor = Color(0xFFA3A2A2),
//                    contentColor = Color(0xFFFFFFFF),
                    disabledContentColor = Color(0xFF000000),
                    disabledContainerColor = Color(0xFFFFFFFF),
                ),


        ) {
            Row (
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),

                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
            ){
                Icon(
                    imageVector = FeatherIcons.Camera, contentDescription = ""
                )
                Text(text = "Pick Multiple Image")
            }

        }// IconButton //  Button to pick multiple images


        val focusManager = LocalFocusManager.current


        Card (
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable {
                    focusManager.clearFocus()
                },
        ){
            // Content TextField
            TextField(
                value = content.value,
                onValueChange = { content.value = it },
                //label = { Text("Content") },
                placeholder = { Text("What's on your mind?") },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                keyboardActions = KeyboardActions(
                    onDone = {
                        // Hide the keyboard
                      focusManager.clearFocus()
                    },
                    onNext = {
                        focusManager.clearFocus()
                    },
                    onGo = {
                        focusManager.clearFocus()
                    },
                    onSearch = {
                        focusManager.clearFocus()
                    }
                ),


            )
        }




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

        when {
            isLoading -> CircularProgressIndicator()
            error != null -> ErrorMessage(error = "Error: $error")
            else -> Unit
        }

        val postID = UUID.randomUUID().toString()
        val coroutineScope = rememberCoroutineScope()


        // Button to pick multiple images
        IconButton(
            onClick = {
                val filename = postViewModel.generatePostID()



                val imageUrl = firebaseStorageViewModel.uploadStatus.value?.imageUrl

                coroutineScope.launch {
                    val url = postViewModel.uploadFile(selectedImageUri!!, filename, "post_images")

                    postViewModel.createPost(
                        Post(
                            id = postID,
                            userName = localUserData?.userName?: "N/A",
                            profileImageUrl = localUserData?.profileImageUrl?: "N/A",
                            postImageUrl = if (url != null) url.toString() else "N/A", // imageUrl?: "N/A",
                            postText = content.value,
                            timestamp = Timestamp.now(),
                            likeCount = 0,
                            commentCount = 0,
                            likedBy = emptyList(),
                            comments = listOf(),
                            userId =  localUserData?.userID?: "N/A"
                        )
                    )
                    postViewModel.loadPosts()
                }
            },
            enabled = selectedImageUri != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .height(45.dp)
                .shadow(4.dp, shape = RoundedCornerShape(16.dp)),
            colors = IconButtonDefaults
                .iconButtonColors(
                    containerColor = Color(0xFFA3A2A2),
//                    contentColor = Color(0xFFFFFFFF),
                    disabledContentColor = Color(0xFF000000),
                    disabledContainerColor = Color(0xFFFFFFFF),
                ),


            ) {
            Row (
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),

                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
            ){
                Icon(
                    imageVector = FeatherIcons.Upload, contentDescription = ""
                )
                Text(text = "Submit Post")
            }

        }// IconButton //  Button to pick multiple images

//        Button(
//            onClick = {
//                val filename = postViewModel.generatePostID()
//
//
//
//                val imageUrl = firebaseStorageViewModel.uploadStatus.value?.imageUrl
//
//                coroutineScope.launch {
//                    val url = postViewModel.uploadFile(selectedImageUri!!, filename, "post_images")
//
//                    postViewModel.createPost(
//                        Post(
//                            id = postID,
//                            userName = localUserData?.userName?: "N/A",
//                            profileImageUrl = localUserData?.profileImageUrl?: "N/A",
//                            postImageUrl = if (url != null) url.toString() else "N/A", // imageUrl?: "N/A",
//                            postText = content.value,
//                            timestamp = Timestamp.now(),
//                            likeCount = 0,
//                            commentCount = 0,
//                            likedBy = emptyList(),
//                            comments = listOf(),
//                            userId =  localUserData?.userID?: "N/A"
//                        )
//                    )
//                    postViewModel.loadPosts()
//                }
//            },
//            enabled = selectedImageUri != null,
//            modifier = Modifier.fillMaxWidth(),
//            elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 24.dp)
//        ) {
//            Text("Post")
//        } // Button



    }
}// Body Column
