package com.example.socialmediaapp.data.entitiy

import com.google.firebase.Timestamp

data class ChatMessage(
    val id: String = "",
    val sender_id: String = "",
    val receiver_id: String = "",
    val message: String = "",
    val timestamp: Long = 0
    //val timestamp: Timestamp = Timestamp.now()
)
