package com.example.socialmediaapp.util

data class PostState(
    val isLiked: Boolean = false,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
)

