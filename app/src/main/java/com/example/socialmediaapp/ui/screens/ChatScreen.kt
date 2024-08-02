package com.example.socialmediaapp.ui.screens

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.foundation.lazy.items

import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.Screen
import com.example.socialmediaapp.data.entitiy.ChatMessage
import com.example.socialmediaapp.ui.viewmodels.ChatViewModel
import java.util.Locale

@Composable
fun ChatScreen(
    viewModel: ChatViewModel, partnerId: String,
    partnerUserId : String
)
{
    val messages by viewModel.messages.collectAsState()
    val chatPartner by viewModel.chatPartner.collectAsState()
    val isCurrentUserMessage by viewModel.isCurrentUserMessage.collectAsState()

    LaunchedEffect(partnerUserId) {
        viewModel.loadChatMessages(partnerUserId)
        viewModel.loadChatPartner(partnerUserId)
    }

    Log.d("ChatScreen", "partnerId: $partnerUserId")


    Column (modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp))
    {
        chatPartner?.let { partner ->
            Text(text = "Chat with ${partner.userName}", style = MaterialTheme.typography.headlineMedium)
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(messages) { message ->
                ChatMessageItem(
                    message = message,
                    isCurrentUser = isCurrentUserMessage[message.id] ?: false

                )
            }
        }

        ChatInput(onSendMessage = { viewModel.sendMessage(partnerUserId, it) })
    }
}


@Composable
fun ChatMessageItem(message: ChatMessage, isCurrentUser: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier
                .background(
                    if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Text(text = message.message, color = Color.White)
            Text(
                text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(java.util.Date(message.timestamp)),
                //text = message.timestamp.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ChatInput(onSendMessage: (String) -> Unit) {
    var message by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.weight(1f),
            placeholder = { Text("Mesajınızı yazın...") }
        )
        IconButton(
            onClick = {
                if (message.isNotBlank()) {
                    onSendMessage(message)
                    message = ""
                }
            }
        ) {
            Icon(Icons.Default.Send, contentDescription = "Gönder")
        }
    }
}