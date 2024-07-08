package com.example.socialmediaapp.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.socialmediaapp.R


@Composable
fun Components(){

    //




}


@Composable
fun HomeScreenBodyContent(modifier: Modifier = Modifier) {
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
            items(10) {
                Post()
            }

        }
    }
}

@Composable
//@Preview
fun Post(){

    var userLiked by remember { mutableStateOf(true) }
    var likeCount by remember { mutableStateOf(0) }
    var userCommented by remember { mutableStateOf(false) }
    var commentCount by remember { mutableStateOf(0) }

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
                    model = "https://picsum.photos/400/400",
                    contentDescription = "Translated description of what the image contains",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),




                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(text = "username")


            }


            // Post image
            AsyncImage(
                model = "https://picsum.photos/400/400",
                contentDescription = "Translated description of what the image contains",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.FillWidth
            )
            // Post text
            Text(text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.")

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
                                /*TODO*/
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MakeAPostScreen(){

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
                        onClick = { /* Yapılacak işlem */ },
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
        MakeAPostBody(Modifier.padding(innerPadding))
    }
}

@Composable
fun MakeAPostBody(modifier: Modifier = Modifier) {
    val title = remember { mutableStateOf("") }
    val content = remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
        )

        OutlinedTextField(
            value = content.value,
            onValueChange = { content.value = it },
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            keyboardActions = KeyboardActions(onDone = { /* Handle send post */ })
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { /* Handle add image */ }) {
                Icon(imageVector = Icons.Default.Face, contentDescription = "Add Image")
            }
            IconButton(onClick = { /* Handle add video */ }) {
                Icon(imageVector = Icons.Default.Info, contentDescription = "Add Video")
            }
            IconButton(onClick = { /* Handle add audio */ }) {
                Icon(imageVector = Icons.Default.AccountBox, contentDescription = "Add Audio")
            }
        }

        Button(
            onClick = { /* Handle send post */ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Post")
        }
    }
}








