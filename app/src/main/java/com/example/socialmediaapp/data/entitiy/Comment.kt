package com.example.socialmediaapp.data.entitiy

import com.google.firebase.Timestamp

data class Comment(
    val commentID : String = "",
    val commentText : String = "",
    val createdAt : Timestamp = Timestamp.now(),
    val userID : String = "",
    val userName : String = "",
    val profileImageUrl : String = ""

)
