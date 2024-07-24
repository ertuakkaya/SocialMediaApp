package com.example.socialmediaapp.data.entitiy

import com.google.firebase.Timestamp

data class Post(val id: String = "",
                val userId: String = "",
                val userName: String = "",
                val profileImageUrl: String = "",
                val postImageUrl: String = "",
                var postText: String = "",
                val commentCount: Int = 0,
                val likeCount: Int = 0,
                val timestamp: Timestamp = Timestamp.now(),
                val likedBy: List<Like> = emptyList(),
                val comments: List<Comment> = emptyList(),



                )





