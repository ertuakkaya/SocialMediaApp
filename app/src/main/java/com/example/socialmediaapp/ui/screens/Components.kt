package com.example.socialmediaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Call
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


@Composable
fun Components(){

    //




}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun HomeScreen() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier,

                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    Color(0xFFFFFFFF), Color(0xFFFFFFFF), Color(
                        0xFFFFFFFF
                    )
                ),

                title = {
                    Text(
                        text = "Connected",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier,

                        textAlign = TextAlign.Center
                    )
                },
                actions = {

                    IconButton(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .size(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications, // Icon için uygun bir image vector seçin
                            contentDescription = "Logout",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,




            )

        },// Top bar
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .height(56.dp),
                containerColor = Color(0xFFFFFFFF),
                tonalElevation = 4.dp,
                contentColor = Color(0xFF000000)


            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        /*TODO*/
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
                NavigationBarItem(
                    selected = true,
                    onClick = { /*TODO*/ },
                    icon = {
                        Icon(
                            Icons.Rounded.Call,
                            contentDescription = null
                        )
                    },
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        /*TODO*/
                    },
                    icon = {
                        Icon(
                            Icons.Rounded.Add,
                            contentDescription = null
                        )
                    },
                )

            }

        },// Bottom bar

    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
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
@Preview
fun Post(){

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
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .weight(1f)
                ){
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
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
                        Text(text = "12")
                    }
                }
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

                        Text(text = "12")
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier
                                .size(50.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FavoriteBorder, // Icon için uygun bir image vector seçin
                                contentDescription = "Logout",
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
fun BottomAppBarContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
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

    }
}



