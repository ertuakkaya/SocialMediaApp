package com.example.socialmediaapp.ui.screens

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column



import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import com.example.socialmediaapp.R
import com.example.socialmediaapp.Screen
import com.example.socialmediaapp.data.entitiy.Comment
import com.example.socialmediaapp.data.entitiy.Post
import com.example.socialmediaapp.ui.viewmodels.AuthViewModel
import com.example.socialmediaapp.ui.viewmodels.FirestoreViewModel
import com.example.socialmediaapp.ui.viewmodels.PostViewModel
import com.example.socialmediaapp.util.PostState
import compose.icons.FeatherIcons
import compose.icons.feathericons.Home
import compose.icons.feathericons.Send

import kotlin.math.absoluteValue

import com.example.socialmediaapp.util.Result
import compose.icons.feathericons.MessageCircle
import compose.icons.feathericons.Plus
import compose.icons.feathericons.PlusCircle
import compose.icons.feathericons.User


@Composable
fun Components(){

    //




}



@Composable
//@Preview
fun Post(
    post: Post,
    postViewModel: PostViewModel,
    firestoreViewModel: FirestoreViewModel,
    authViewModel: AuthViewModel,
) {


    var userLiked by remember { mutableStateOf(postViewModel.GetLikesByUserID()) }
    //var likeCount by remember { mutableStateOf(post.likedBy.size) }
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

    // userData is currentUser : User
    val userData by firestoreViewModel.userData.collectAsState()
    val isLoading by firestoreViewModel.isLoading.collectAsState()
    val error by firestoreViewModel.error.collectAsState()

    var userID = authViewModel.getCurrentUser()?.uid ?: "N/A"
    var currentUserID = userData?.userID?: "N/A"


    var isCommentsExpanded by remember { mutableStateOf(false) }
    val commentState by postViewModel.commentsState.collectAsState()




    Log.d("Post", "Post: $userLiked")
    // User data is loading
    LaunchedEffect(key1 = true) {

        val user = firestoreViewModel.getUserFromFirestore(userID)

    }


    /**
     * currentUser is the currently logged in user of the app.
     */
    LaunchedEffect(Unit) {
        postViewModel.getUser(post.userId)

        ////
        postViewModel.updatePostState(post.id) {
            it.copy(likeCount = post.likedBy.size, commentCount = post.comments.size)
        }
    }

    val currentUser by postViewModel.user.collectAsState()
    Log.d("postViewModel.user.collectAsState() ", "User: $currentUser")




    Log.d("current user ID", "Current User ID: $currentUserID ")


    val postStates by postViewModel.postStates.collectAsState()
    val currentPostState = postStates[post.id] ?: PostState()

    val isPostLiked = currentPostState.isLiked
    val likeCount = currentPostState.likeCount

    val commentCount11 = currentPostState.commentCount

    //val currentCommentCount = currentPostState.commentCount

    LaunchedEffect(post.id, currentUserID) {
        postViewModel.checkIfUserLikedThePost(post.id, currentUserID)
    }


    var isExpanded by remember { mutableStateOf(false) }



    val posts by postViewModel.posts.collectAsState()
    val currentPost = posts.find {it ->
        it.id == post.id
    } ?: return





    Log.d("CommentSection", "Comment Count: ${currentPostState.commentCount}")

    LaunchedEffect (post.id){
        postViewModel.fetchCommentCount(post.id)

    }
    val commentCount1 by postViewModel.commentCount.collectAsState()
    Log.d("CommentSection", "commentCount1: $commentCount1")

    when (val state = commentState) {
        is Result.Loading -> Unit
        is Result.Success -> if (state.data.isEmpty()) {
        } else {
            commentCount = state.data.size
        }
        is Result.Failure -> Log.d("CommentSection", "Error: ${state.exception.message}")
    }


    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .animateContentSize()
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
                // Profile pic
                ProfileImage(profileImageUrl = profileImageUrl)

                Spacer(modifier = Modifier.width(16.dp))

                //Text(text = username)
                Text(text = username)

            }


            // Post image

            PostImage(postImageUrl =postImageUrl)

            // Post text
            Text(text = if (postText.isNotEmpty()) postText else "Lorem ipsum dolor sit amet, consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.")


            Column {// Ana Column

                Box(){// Comment ve like butonları
                    Row (
                        modifier = Modifier

                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ){


                        // Comment Button
                        Box(
                            modifier = Modifier
                                .clickable {
                                    isExpanded = !isExpanded
                                    if (isExpanded) {
                                        postViewModel.loadComments(post.id)
                                    }
                                }
                                .weight(1f),




                            ) {
                            Row (
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Icon(
                                    imageVector = FeatherIcons.MessageCircle,
                                    contentDescription = "Comments"
                                )
                                Spacer(modifier = Modifier.width(8.dp))

                                // Comment Count Text
                                Log.d("CommentSection", "post.commentCount ${post.commentCount}")
                                Text(text = "${post.commentCount} comments")
                            }
                        }


                        // Like icon and Text Row
                        Box(
                            modifier = Modifier
                                //.size(50.dp)
                                .weight(1f),
                            content = {
                                LikeSeciton(
                                    isPostLiked = isPostLiked,
                                    likeCount = likeCount,
                                    postViewModel = postViewModel,
                                    post = post,
                                    currentUserID = currentUserID
                                )
                            }
                        )



                    }// Row // Comment icon , comment count text, like count text , like button
                } // Comment and Like Row

                //DividerComponent(verticalPadding = 8)

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(animationSpec = tween(500)) +
                            slideInVertically(animationSpec = tween(500)) +
                            scaleIn(animationSpec = tween(500)),
                    exit = fadeOut(animationSpec = tween(300)) +
                            slideOutVertically(animationSpec = tween(300)) +
                            scaleOut(animationSpec = tween(300))
                ) {

                    Column {
                        when (val state = commentState) {
                            is Result.Loading -> LoadingIndicator()
                            is Result.Success -> if (state.data.isEmpty()) {
                                Text(text = "No comments yet")
                            } else {
                                CommentListComponent(
                                    postViewModel = postViewModel,
                                    comments = state.data
                                )
                            }

                            is Result.Failure -> ErrorMessage(
                                state.exception.message ?: "An unknown error occurred"
                            )
                        }

                        AddComment(
                            postViewModel = postViewModel,
                            post = post,
                            user_id = currentUserID
                        )
                    }

                }
            }

            // Comment icon , comment count text, like count text , like button


            //DividerComponent(verticalPadding = 1)
            Log.d("Post" , "Current user : $currentUser")


            // Yeni eklenen CommentSection
            CommentSection(
                postViewModel = postViewModel,
                post = post,
                onLoadComment = {
                    postViewModel.loadComments(post.id)
                },
                user_id = currentUserID
            )


        }// Card Column
    }

}


/**
 * LikeSection is a composable function that displays the like section of a post.
 * @param isPostLiked: Boolean is a boolean value that indicates whether the post is liked by the current user.
 * @param likeCount: Int is the number of likes on the post.
 * @param postViewModel: PostViewModel is the ViewModel class that contains the logic for the Post screen.
 * @param post: Post is the post whose likes are to be displayed.
 * @param currentUserID: String is the ID of the currently logged in user.
 *
 */
@Composable
fun LikeSeciton(
    isPostLiked: Boolean,
    likeCount: Int,
    postViewModel: PostViewModel,
    post: Post,
    currentUserID: String
) {


    // animation scale
    val scale by animateFloatAsState(
        targetValue = if (isPostLiked) 1.3f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (isPostLiked!!) {
                if (likeCount > 1) "You & ${likeCount - 1} Liked" else "You Liked"
            } else {
                "$likeCount Like"
            },
            fontSize = 12.sp,
        )

        IconButton(
            onClick = {
                postViewModel.likeOrUnlikePost(post.id, currentUserID)

            },
            modifier = Modifier.size(50.dp)
        ) {

            Icon(
                painter = if (isPostLiked) painterResource(id = R.drawable.like_filled)
                else painterResource(id = R.drawable.like_unfilled),
                contentDescription = null,
                tint = if (isPostLiked) Color.Red else Color.Black,
                modifier = Modifier
                    .size(32.dp)
                    .scale(scale) // Animation scale
            )
        }
    }


}



@Composable
fun CommentSectionPost(
    userCommented: Boolean,
    commentCount: Int,
    postViewModel: PostViewModel,
    post: Post
){



    var isExpanded by remember { mutableStateOf(false) }

    val commentState by postViewModel.commentsState.collectAsState()




    var isClicked by remember {
        mutableStateOf(false)
    }

    // Comment icon and Text Row
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isExpanded = !isExpanded
                if (isExpanded) {
                    postViewModel.loadComments(post.id)
                }
            },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ){
        // Comment icon
        IconButton(
            onClick = {
                isClicked = !isClicked
            },
            modifier = Modifier
                .size(50.dp),
            content = {
                Icon(
                    imageVector = Icons.Default.Person, // Icon için uygun bir image vector seçin
                    contentDescription = "Logout",
                    modifier = Modifier.size(32.dp)
                )
                if (isClicked){




                }else{
                    return@IconButton
                }



            }
        )

        Text(
            text = if (userCommented) {
                if (commentCount > 1) "You and ${commentCount - 1} Commented" else "You Commented"
            } else {
                "$commentCount Comment"
            },
        )
    }// Comment icon and Text Row
}







/**
 * CommentSection is a composable function that displays the comment section of a post.
 * @param postViewModel: PostViewModel is the ViewModel class that contains the logic for the Post screen.
 * @param post: Post is the post whose comments are to be displayed.
 * @param currentUser: User is the currently logged in user of the app.
 * @see PostViewModel.addComment
 */


@Composable
fun CommentSection(
    postViewModel: PostViewModel,
    post: Post,
    onLoadComment: () -> Unit,
    user_id: String
) {
    var isExpanded by remember { mutableStateOf(false) }
    val commentState by postViewModel.commentsState.collectAsState()

    val postStates by postViewModel.postStates.collectAsState()
    val currentPostState = postStates[post.id] ?: PostState()


    val posts by postViewModel.posts.collectAsState()
    val currentPost = posts.find {it ->
        it.id == post.id
    } ?: return


    var commentCount =


        Log.d("CommentSection", "Comment Count: ${currentPostState.commentCount}")

    LaunchedEffect (post.id){
        postViewModel.fetchCommentCount(post.id)

    }
    val commentCount1 by postViewModel.commentCount.collectAsState()
    Log.d("CommentSection", "commentCount1: $commentCount1")

    when (val state = commentState) {
        is Result.Loading -> Unit
        is Result.Success -> if (state.data.isEmpty()) {
        } else {
            commentCount = state.data.size
        }
        is Result.Failure -> Log.d("CommentSection", "Error: ${state.exception.message}")
    }




//    Column {
//        Row(
//            modifier = Modifier
//                .clickable {
//                    isExpanded = !isExpanded
//                    if (isExpanded) {
//                        onLoadComment()
//                    }
//                }
//                .padding(vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(){
//                Row {
//                    Icon(
//                        imageVector = FeatherIcons.MessageCircle,
//                        contentDescription = "Comments"
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    // Comment Count Text
//                    Log.d("CommentSection", "post.commentCount ${post.commentCount}")
//                    Text(text = "${post.commentCount} comments")
//                }
//            }
//
//
//            if (isExpanded) {
//                Column {
//                    when (val state = commentState) {
//                        is Result.Loading -> LoadingIndicator()
//                        is Result.Success -> if (state.data.isEmpty()) {
//                            Text(text = "No comments yet")
//                        } else {
//                            CommentListComponent(postViewModel = postViewModel, comments = state.data)
//                        }
//                        is Result.Failure -> ErrorMessage(state.exception.message ?: "An unknown error occurred")
//                    }
//
//                    AddComment(postViewModel = postViewModel, post = post, user_id = user_id)
//                }
//
//
//
//            }
//        }
//
//
//    }
}




@Composable
fun CommentListComponent(
    postViewModel: PostViewModel,
    comments: List<Comment>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .heightIn(max = 150.dp)
    ) {
        items(comments) { comment ->
            CommentComponent(comment = comment)
        }
    }
}




@Composable
fun CommentComponent(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        //.padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(
            profileImageUrl = comment.profileImageUrl,
            modifier = Modifier
                .size(40.dp)
        )
        Column {
            Text(text = comment.userName, fontWeight = FontWeight.Bold)
            Text(text = comment.commentText)
        }
    }
}



/**
 * AddComment is a composable function that displays the comment section of a post.
 * @param postViewModel: PostViewModel is the ViewModel class that contains the logic for the Post screen.
 * @param post: Post is the post whose comments are to be displayed.
 * @see PostViewModel.addComment
 * @see PostViewModel.commentsState
 */
@Composable
fun AddComment(
    postViewModel: PostViewModel,
    post: Post,
    user_id : String
){


    //var showSnackBar by remember { mutableStateOf(showSnackBar) }

    // User data is loading
    LaunchedEffect(Unit) {
        postViewModel.getUser(user_id)
    }

    val currentUser by postViewModel.user.collectAsState()
    Log.d("CommentSection", "User: $currentUser")



    var commentText by remember { mutableStateOf("") }






    Row (
        modifier = Modifier
            .fillMaxWidth()

            .wrapContentHeight(),

        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box( modifier = Modifier
            .size(40.dp)
            .border(2.dp, Color.Gray, CircleShape)
            .clip(CircleShape),)
        {
            currentUser.let {
                if (it != null) {
                    AsyncImage(
                        model = currentUser?.profileImageUrl,
                        contentDescription = "Translated description of what the image contains",
                        modifier = Modifier
                            .size(50.dp)
                            .border(2.dp, Color.Gray, CircleShape)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
                else{
                    CircularProgressIndicator()
                }
            }
        }



        TextField(
            value = commentText,
            onValueChange = { commentText = it },
            modifier = Modifier
                .weight(7f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            placeholder = { Text(text = "Add a comment") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle.Default.copy(fontSize = 20.sp),
        )




        IconButton(
            onClick = {
                postViewModel.addComment(post.id,  commentText, currentUser!!)
                commentText = ""
                //showSnackBar = true

            },
            modifier = Modifier
                .weight(1f)
        ) {
            Icon(imageVector = FeatherIcons.Send, contentDescription = "Send" )

        }

    }

}

@Composable
fun SnackBarComponent(onDismiss: () -> Unit){
    Snackbar {
        Text(text = "Comment added successfully")
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Close"
            )
        }
    }
}





@Composable
fun ProfileImage(
    profileImageUrl: String,
    modifier: Modifier = Modifier
        .size(50.dp)
        .border(2.dp, Color.Gray, CircleShape)
        .clip(CircleShape)


){
    AsyncImage(
        //model = "https://picsum.photos/400/400",
        // model = if (post.profileImageUrl.isNotEmpty()) post.profileImageUrl else "https://picsum.photos/400/400",////////////////
        model = if (profileImageUrl.isNotEmpty()) profileImageUrl else "https://picsum.photos/400/400",
        contentDescription = "Translated description of what the image contains",
        modifier = modifier
            .size(50.dp)
            .border(2.dp, Color.Gray, CircleShape)
            .clip(CircleShape)
            .shadow(88.dp),
        contentScale = ContentScale.FillBounds
    )


}


@Composable
fun PostImage(postImageUrl : String,
              modifier: Modifier = Modifier){
    // Post image
    AsyncImage(
        //model = "https://picsum.photos/400/400",
        model = if (postImageUrl.isNotEmpty()) postImageUrl else "https://picsum.photos/400/400",
        contentDescription = "Translated description of what the image contains",
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp))
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(4.dp)),
        contentScale = ContentScale.FillWidth,
    )
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HorizontalPagerSample(selectedImageUris: List<Uri?>) {
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
fun BottomBarComponent(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        modifier = Modifier
            .height(60.dp),
        containerColor = Color(0xFFFFFFFF),
        tonalElevation = 16.dp,
        contentColor = Color(0xFF000000)


    ) {
        // HomeScreen
        NavigationBarItem(
            selected = currentRoute.toString() == "com.example.socialmediaapp.Screen.HomeScreen",
            onClick = {
                navController.navigate(Screen.HomeScreen)
                Log.d("BottomBarComponent", "HomeScreen $currentRoute")
            },
            modifier = Modifier
                //.clip(CircleShape)
                .fillMaxSize(),

            icon = {
                Icon(
                    imageVector =  FeatherIcons.Home,
                    contentDescription = null
                )
            },

            //colors = NavigationBarItemDefaults.colors(Color(0xFF5871B4), Color(0xFF000000), Color(0xFFAD5454)


        )
        // MakeAPostScreen
        NavigationBarItem(
            selected = currentRoute.toString() == "com.example.socialmediaapp.Screen.MakeAPostScreen",
            onClick = {
                navController.navigate(Screen.MakeAPostScreen)
            },
            icon = {
                Icon(
                    FeatherIcons.PlusCircle,
                    contentDescription = null
                )
            },
        )

        // AccountScreen
        NavigationBarItem(
            selected = currentRoute.toString() == "com.example.socialmediaapp.Screen.AccountScreen",
            onClick = {
                navController.navigate(Screen.AccountScreen)
            },
            icon = {
                Icon(
                    FeatherIcons.User,
                    contentDescription = null
                )
            },
        )


        // ChatselectScreen
        NavigationBarItem(
            selected = currentRoute.toString() == "com.example.socialmediaapp.Screen.ChatSelectScreen",
            onClick = {
                navController.navigate(Screen.ChatSelectScreen)
            },
            icon = {
                Icon(
                    FeatherIcons.MessageCircle,
                    contentDescription = null
                )
            },
        )

    }

}





