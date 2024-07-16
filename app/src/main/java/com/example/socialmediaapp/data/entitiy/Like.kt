package com.example.socialmediaapp.data.entitiy

import com.google.firebase.Timestamp

data class Like(
    val userId: String = "",
    val createdAt: Timestamp = Timestamp.now()
)
