package com.example.socialmediaapp.ui.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.socialmediaapp.R
import com.example.socialmediaapp.Screen
import com.example.socialmediaapp.data.entitiy.Post
import com.example.socialmediaapp.ui.viewmodels.FirebaseStorageViewModel
import com.example.socialmediaapp.ui.viewmodels.PostViewModel

import com.google.firebase.firestore.firestoreSettings
import kotlin.math.absoluteValue


@Composable
fun Components(){

    //




}


@Composable
fun HomeScreenBodyContent(modifier: Modifier = Modifier,postViewModel: PostViewModel) {

    val posts by postViewModel.posts.collectAsState()


    Surface (
        color = Color(0xFFFFFFFF),
        modifier = modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, end = 24.dp, top = 0.dp, bottom = 0.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {

            items(posts.size) { index ->
               Post(post = posts[index], onLikeClick = { postViewModel.likePost(posts[index].id) })
            }

        }
    }
}

@Composable
//@Preview
fun Post(post: Post, onLikeClick: () -> Unit){

    var userLiked by remember { mutableStateOf(true) }
    var likeCount by remember { mutableStateOf(post.likeCount) }
    var userCommented by remember { mutableStateOf(false) }
    var commentCount by remember { mutableStateOf(post.commentCount) }
    var username by remember { mutableStateOf(post.userName) }
    var profileImageUrl by remember { mutableStateOf(post.profileImageUrl) }
    var postImageUrl by remember { mutableStateOf(post.postImageUrl) }
    var postText by remember { mutableStateOf(post.postText) }
    var timestamp by remember { mutableStateOf(post.timestamp) }
    var likedBy by remember { mutableStateOf(post.likedBy) }
    var comments by remember { mutableStateOf(post.comments) }
    var commentText by remember { mutableStateOf("") }


    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(Color(0xFFEEEDEB))
    ) {
        // Card Column
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Profile pic , username ,
            Row(
                modifier = Modifier

                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ){
                AsyncImage(
                    //model = "https://picsum.photos/400/400",
                    model = if (post.profileImageUrl.isNotEmpty()) post.profileImageUrl else "https://picsum.photos/400/400",
                    contentDescription = "Translated description of what the image contains",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),




                )

                Spacer(modifier = Modifier.width(16.dp))

                //Text(text = username)
                Text(text = username)


            }


            // Post image
            AsyncImage(
                //model = "https://picsum.photos/400/400",
                model = if (post.postImageUrl.isNotEmpty()) post.postImageUrl else "https://picsum.photos/400/400",
                contentDescription = "Translated description of what the image contains",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.FillWidth
            )
            // Post text
            Text(text = if (post.postText.isNotEmpty()) post.postText else "Lorem ipsum dolor sit amet, consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.")

            // Comment icon , comment count text, like count text , like button
            Row (
                modifier = Modifier

                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                // Comment icon and Text Row
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .weight(1f)
                ){
                    // Comment icon and Text Row
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        // Comment icon
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .size(50.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person, // Icon için uygun bir image vector seçin
                                contentDescription = "Logout",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Text(
                            text = if (userCommented) {
                                if (commentCount > 1) "You and ${commentCount - 1} Commented" else "You Commented"
                            } else {
                                "$commentCount Comment"
                            },
                        )
                    }// Comment icon and Text Row
                }// Comment icon and Text Row

                // Like icon and Text Row
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .weight(1f)
                ){
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ){


                        Text(
                            text = if (userLiked) {
                                if (likeCount > 1) "You and ${likeCount - 1} Liked" else "You Liked"
                            } else {
                                "$likeCount Like"
                            },
                        )
                        IconButton(
                            onClick = {

                                userLiked = !userLiked

                            },
                            modifier = Modifier
                                .size(50.dp)
                        ) {

                            Icon(
                                painter = if(userLiked) painterResource(id = R.drawable.like_filled) else painterResource(id = R.drawable.like_unfilled),
                                contentDescription = null,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                    }

                }
            }// Row // Comment icon , comment count text, like count text , like button

        }// Card Column
    }

}



@Composable
fun MakeAPostBody(modifier: Modifier = Modifier , firebaseStorageViewModel: FirebaseStorageViewModel) {

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




        ElevatedCard (
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(Color(0xFFFFFFFF)),
            //content = { HorizontalPagerSample( if (selectedImageUris.isNotEmpty()) selectedImageUris else selectedImageUri)}
            content = { HorizontalPagerSample(selectedImageUris.ifEmpty { listOf(selectedImageUri).filterNotNull() })}

        )

        Button(
            onClick = {
//                multiplePhotoPickerLauncher.launch(
//                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                )
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        ) {
            Text(text = "Pick Multiple Image")
        }



        uploadStatus?.let { status ->
            when { // TODO : Post Succes or Fail Screen
                status.success -> Log.d("FirebaseStorageViewModel", "Image uploaded successfully. URL: ${status.imageUrl}")
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


        selectedImageUri?.let { uri ->
            Button(
                onClick = { firebaseStorageViewModel.uploadPostImage(uri) }, ////////////
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Post")
            }
        }

//        Button(
//            onClick = {
//
//            },
//
//        ) {
//
        }
    }// Body Column




/*
@Composable
fun ImagePicker(){
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var selectedImageUris by remember {
        mutableStateOf<List<Uri?>>(emptyList())
    }



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


    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        item{
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Button(
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                           PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Text(text = "Pick Image")
                }

                Button(
                    onClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Text(text = "Pick Multiple Image")
                }

            }// Row
        }// item


        item{
            AsyncImage(
                model = selectedImageUri,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop

            )
        }// item

        items(selectedImageUris) { uri ->

            AsyncImage(
                model = uri,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop

            )



        }
    }
}
*/

/*
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HorizontalPagerSample() {

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var selectedImageUris by remember {
        mutableStateOf<List<Uri?>>(emptyList())
    }



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


    val pageCount = 5
    val pagerState = rememberPagerState(pageCount = {
        pageCount
    })

    Column {
        HorizontalPager(
            //pageCount = pageCount,
            state = pagerState,
        ) { page ->

//            Box(
//                modifier = Modifier
//                    //.fillMaxSize()
//                    .background(MaterialTheme.colorScheme.primary),
//                contentAlignment = Alignment.Center
//            ) {
//                Text("Page $page", color = Color.White)
//            }


        }
        Spacer(modifier = Modifier.height(16.dp))


        HorizontalPagerIndicator(
            pageCount = pageCount,
            currentPage = pagerState.currentPage,
            targetPage = pagerState.targetPage,
            currentPageOffsetFraction = pagerState.currentPageOffsetFraction
        )
    }
}

 */



@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HorizontalPagerSample(selectedImageUris: List<Uri?>) {
    // Seçilen resimlerin sayısına göre pageCount değerini ayarlayın.
    // Eğer hiç resim seçilmediyse, varsayılan olarak 1 sayfa gösterilsin.
    val pageCount = if (selectedImageUris.isNotEmpty()) selectedImageUris.size else 1
    val pagerState = rememberPagerState(pageCount = {
        pageCount
    })

    Column (
        modifier = Modifier
            //.fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween

    ){
        // Horizontal Pager
        HorizontalPager(
            //pageCount = pageCount,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp),

            pageSpacing = 16.dp
                //.height(300.dp)
        ) { page ->
            // Seçilen resimler listesinden, mevcut sayfaya karşılık gelen resmi göster
            AsyncImage(
                model = selectedImageUris.getOrNull(page) ?: "https://picsum.photos/400/400", // Eğer resim yoksa, varsayılan bir resim göster
                contentDescription = "Selected Image",
                modifier = Modifier
                    .clip(RoundedCornerShape(32.dp))
                    .fillMaxWidth()
                    //.height(300.dp)
                    ,
                contentScale = ContentScale.Crop,
                clipToBounds = true
            )
        }
        //Spacer(modifier = Modifier.height(16.dp))

        // Sayfa göstergesi için HorizontalPagerIndicator kullanabilirsiniz.
        // Bu kısım opsiyoneldir ve sayfa göstergesi eklemek istiyorsanız kullanılabilir.
        HorizontalPagerIndicator(
            pageCount = pageCount,
            currentPage = pagerState.currentPage,
            targetPage = pagerState.targetPage,
            currentPageOffsetFraction = pagerState.currentPageOffsetFraction
        )
    }// Column
}


@Composable
private fun HorizontalPagerIndicator(
    pageCount: Int,
    currentPage: Int,
    targetPage: Int,
    currentPageOffsetFraction: Float,
    modifier: Modifier = Modifier,
    indicatorColor: Color = Color.DarkGray,
    unselectedIndicatorSize: Dp = 8.dp,
    selectedIndicatorSize: Dp = 10.dp,
    indicatorCornerRadius: Dp = 2.dp,
    indicatorPadding: Dp = 2.dp
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .wrapContentSize()
            .height(selectedIndicatorSize + indicatorPadding * 2)
    ) {

        // draw an indicator for each page
        repeat(pageCount) { page ->
            // calculate color and size of the indicator
            val (color, size) =
                if (currentPage == page || targetPage == page) {
                    // calculate page offset
                    val pageOffset =
                        ((currentPage - page) + currentPageOffsetFraction).absoluteValue
                    // calculate offset percentage between 0.0 and 1.0
                    val offsetPercentage = 1f - pageOffset.coerceIn(0f, 1f)

                    val size =
                        unselectedIndicatorSize + ((selectedIndicatorSize - unselectedIndicatorSize) * offsetPercentage)

                    indicatorColor.copy(
                        alpha = offsetPercentage
                    ) to size
                } else {
                    indicatorColor.copy(alpha = 0.1f) to unselectedIndicatorSize
                }

            // draw indicator
            Box(
                modifier = Modifier
                    .padding(
                        // apply horizontal padding, so that each indicator is same width
                        horizontal = ((selectedIndicatorSize + indicatorPadding * 2) - size) / 2,
                        //vertical = size / 4
                        vertical = 4.dp
                    )
                    .clip(RoundedCornerShape(indicatorCornerRadius))
                    .background(color)
                    .width(size)
                    .height(size / 2)
            )
        }
    }
}




@Composable
fun BottomBarComponent(navHostController: NavHostController) {
    NavigationBar(
        modifier = Modifier
            .height(56.dp),
        containerColor = Color(0xFFFFFFFF),
        tonalElevation = 4.dp,
        contentColor = Color(0xFF000000)


    ) {
        // HomeScreen
        NavigationBarItem(
            selected = true,
            onClick = {
                navHostController.navigate(Screen.HomeScreen)
            },
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxSize(),

            icon = {
                Icon(
                    Icons.Filled.Home,
                    contentDescription = null
                )
            },

            //colors = NavigationBarItemDefaults.colors(Color(0xFF5871B4), Color(0xFF000000), Color(0xFFAD5454)


        )
        // MakeAPostScreen
        NavigationBarItem(
            selected = true,
            onClick = {
                navHostController.navigate(Screen.MakeAPostScreen)
            },
            icon = {
                Icon(
                    Icons.Rounded.Call,
                    contentDescription = null
                )
            },
        )

        // AccountScreen
        NavigationBarItem(
            selected = false,
            onClick = {
                navHostController.navigate(Screen.AccountScreen)
            },
            icon = {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = null
                )
            },
        )

    }

}




