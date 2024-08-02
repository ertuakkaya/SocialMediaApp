package com.example.socialmediaapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmediaapp.data.entitiy.ChatMessage
import com.example.socialmediaapp.data.entitiy.User
import com.example.socialmediaapp.data.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor( private val chatRepository: FirestoreRepository) : ViewModel()
{


    init {
        val user_id = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    }

    val currentUserID111 = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    //val currentUserID: String = "Jp1QgCWzMZeZXxFoYlqAijIPaYO2"

    val currentUserID: String = currentUserID111

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _chatPartner = MutableStateFlow<User?>(null)
    val chatPartner: StateFlow<User?> = _chatPartner.asStateFlow()


    /**
     *  Loading Chat Messages from Firestore
     *  @param chatPartnerID: String is the ID of the user with whom the current user is chatting.
     */

//    fun loadChatMessages(chatPartnerID: String){
//        viewModelScope.launch {
//            chatRepository.getMessages(currentUserID,chatPartnerID).collect{ messageList ->
//                _messages.value = messageList.sortedBy { it.timestamp }
//            }
//        }
//    }


    private val _isCurrentUserMessage = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val isCurrentUserMessage: StateFlow<Map<String, Boolean>> = _isCurrentUserMessage.asStateFlow()

    fun loadChatMessages(partnerId: String) {
        viewModelScope.launch {
            chatRepository.getMessages(currentUserID, partnerId).collect { messageList ->
                _messages.value = messageList.sortedBy { it.timestamp }
                _isCurrentUserMessage.value = messageList.associate {
                    it.id to (it.sender_id == currentUserID)
                }
            }
        }
    }

    /**
     * Load Chat Partner from Firestore
     * @param chatPartnerID: String is the ID of the user with whom the current user is chatting.
     */
    fun loadChatPartner(chatPartnerID: String){
        viewModelScope.launch {
           _chatPartner.value = chatRepository.getChatPartner(chatPartnerID)
        }
    }

    /**
     * Send Message to the Chat Partner
     * @param reveiverID: String is the ID of the user to whom the message is to be sent.
     * @param message: String is the message to be sent.
     */

    fun sendMessage(reveiverID: String, message: String){
        viewModelScope.launch {
            val message = ChatMessage(
                id = "",
                sender_id = currentUserID,
                receiver_id = reveiverID,
                message = message,
                timestamp = System.currentTimeMillis()
            )
            chatRepository.sendMessage(message)
        }
    }




}