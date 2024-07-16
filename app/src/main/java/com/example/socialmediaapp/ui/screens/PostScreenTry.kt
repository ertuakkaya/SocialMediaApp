package com.example.socialmediaapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.socialmediaapp.data.entitiy.Post
import com.example.socialmediaapp.ui.viewmodels.PostViewModel


// Example usage in a Composable
@Composable
fun PostListScreen(modifier: Modifier,postViewModel: PostViewModel) {
    val posts by postViewModel.posts.collectAsState()



    LazyColumn {
        items(posts.size) { index ->
            PostItem(post = posts[index], onLikeClick = { postViewModel.likePost(posts[index].id) })
        }
    }
}

@Composable
fun PostItem(post: Post, onLikeClick: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = post.profileImageUrl,
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = post.userName, fontWeight = FontWeight.Bold)

        }
        Spacer(modifier = Modifier.height(8.dp))
        AsyncImage(
            model = post.postImageUrl,
            contentDescription = "Post image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = post.postText)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "${post.likeCount} likes")
            Text(text = "${post.commentCount} comments")
        }
        Button(onClick = onLikeClick) {
            Text("Like")
        }
    }
}