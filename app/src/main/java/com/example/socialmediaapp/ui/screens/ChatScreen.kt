package com.example.socialmediaapp.ui.screens

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.clip

import androidx.compose.ui.unit.dp
import com.example.socialmediaapp.data.entitiy.ChatMessage
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.ui.viewmodels.ChatViewModel
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import kotlinx.coroutines.launch
import java.util.Locale



@Composable
fun ChatScreenTopBar(user : User, onBackClick : () -> Unit){

   user.let { notNullUser ->
       Row (
           horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
           verticalAlignment = Alignment.CenterVertically
       ){

           IconButton(
               onClick = {
                    onBackClick()
               }) {
               Icon(FeatherIcons.ArrowLeft, contentDescription = "Back")
           }


            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(2.dp, Color.Gray, CircleShape)
                    .clip(CircleShape),
                content = {
                    ProfileImage(profileImageUrl = notNullUser.profileImageUrl!!)
                }
            )

           Text(
               text = notNullUser.userName!!,
               fontSize = MaterialTheme.typography.headlineSmall.fontSize
           )
       }
   }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    partnerUserId: String,
    onBackClick: () -> Unit,
) {


    Log.d("ChatScreen", "partnerUserId: $partnerUserId")

    LaunchedEffect(partnerUserId) {
        //viewModel.loadChatMessages(partnerUserId)
        viewModel.loadChatPartner(partnerUserId)
    }

    val chatPartnerUser by viewModel.chatPartner.collectAsState()


    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())



    chatPartnerUser?.let { user ->
        Scaffold(
            modifier = Modifier,
                //.nestedScroll(scrollBehavior.nestedScrollConnection),
                
            topBar = {
//                NavigationCompoent(user = user, onBackClick = onBackClick)
                TopAppBar(
                    navigationIcon = {
                        ChatScreenTopBar(
                            user = user,
                            onBackClick = {
                                try {
                                    onBackClick()
                                } catch (e: Exception) {
                                    Log.d("ChatScreen", "Error: ${e.message}")

                                }
                            }
                        )
                    },
                    title = {
                    },
                    modifier = Modifier
                    ,
                    //scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(colorScheme.secondary)
                )

            },

            content = { paddingValue ->
                ChatScreenBody(
                    modifier = Modifier.padding(paddingValue),
                    viewModel = viewModel,
                    partnerUserId = partnerUserId
                )
            },






        )
    }


}


@Composable
fun ChatScreenBody(
    modifier: Modifier,
    viewModel: ChatViewModel,
    partnerUserId: String
) {
    val messages by viewModel.messages.collectAsState()
    val chatPartner by viewModel.chatPartner.collectAsState()
    val isCurrentUserMessage by viewModel.isCurrentUserMessage.collectAsState()

    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(partnerUserId) {
        viewModel.loadChatMessages(partnerUserId)
        viewModel.loadChatPartner(partnerUserId)
    }

    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Log.d("ChatScreen", "partnerId: $partnerUserId")

    Column(
        modifier = Modifier
            .padding(vertical = 32.dp, horizontal = 8.dp)
            //.border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            state = listState
        ) {
            items(messages) { message ->
                ChatMessageItem(
                    message = message,
                    isCurrentUser = viewModel.isCurrentUserMessage(message)
                )
            }
        }

        ChatInput(
            onSendMessage = {
                viewModel.sendMessage(partnerUserId, it)
                coroutineScope.launch {
                    listState.animateScrollToItem(messages.size)
                }
            }
        )
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage, isCurrentUser: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {


        Row (
            modifier = Modifier
                .background(
                    if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                    RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)

        ){

            Box(
                modifier = Modifier
                    ,
                content ={
                    Text(text = message.message, color = Color.White)
                }
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Bottom),
                content ={
                    Text(
                        text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(java.util.Date(message.timestamp)),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
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